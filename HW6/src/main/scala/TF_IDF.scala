import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window
import scala.collection.mutable.HashMap

object TF_IDF {
  System.setProperty("hadoop.home.dir", "C:/winutils/")
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("spark_tfidf")
      .getOrCreate()
    import spark.implicits._

    val input : String = "data/tripadvisor_hotel_reviews.csv"
    val output : String = "data"

    val data = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(input)
      .select(regexp_replace(lower(col("Review")), "[^\\w\\s-]", "").as("handled_text"))

    def textToMap(text : String) : HashMap[String, Int] = {
      var newMap = new HashMap[String, Int]()

      for (s <- text.split(" ")) {
          if (newMap.get(s) != None)
            newMap(s) += 1
          else
            newMap += (s -> 1)
      }

      newMap
    }

    val data_map = data
      .map(x => textToMap(x(0).toString()))
      .withColumn("review_id", monotonically_increasing_id())
      .select(col("review_id"), explode(col("value")).as(Array("word", "number")))

    val top100IDF = data_map
      .groupBy(col("word"))
      .agg(count(col("review_id")) as "count")
      .orderBy(desc("count"))
      .limit(100)
      .select(col("word"), log(lit(data.count) / (col("count") + 1) + 1) as "idf")

    broadcast(top100IDF)
    val top100 = top100IDF.select(col("word")).collect.map(_.getString(0))

    val reviewWindow = Window.partitionBy("review_id")

    val top100TF = data_map
      .withColumn("len", sum("number") over reviewWindow)
      .filter(col("word") isin (top100:_*))
      .withColumn("tf", col("number") / col("len"))
      .select(col("word"), col("review_id"), col("tf"))

    val top100TFIDF = top100TF
      .join(top100IDF, "word")
      .withColumn("tfidf", col("tf") * col("idf"))
      .select(col("word"), col("review_id"), col("tfidf"))

    val result = top100TFIDF
      .groupBy("review_id")
      .pivot(col("word"))
      .sum("tfidf")

    result
      .write
      .mode("overwrite")
      .format("parquet")
      .option("header","true")
      .save(output)
  }
}