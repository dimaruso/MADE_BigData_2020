import breeze.linalg._
import scala.util.Random

class LinearRegression() {

  var weight: DenseVector[Double] = DenseVector.ones[Double](0)

  def predict(X: DenseMatrix[Double]): DenseVector[Double] = {
    X * weight
  }

  def loss(X: DenseMatrix[Double], y: DenseVector[Double]): Double = {
    val y_pred = predict(X)
    val diff = y.toArray zip y_pred.toArray map ( z => scala.math.pow(z._1 - z._2, 2))
    val total_err = diff.sum

    total_err / y.length.toDouble
  }

  def grad_step(X: DenseMatrix[Double], y: DenseVector[Double], lr: Double): Unit = {
    val size = y.length
    var grad = DenseVector.zeros[Double](X.cols)
    var pred = DenseVector.zeros[Double](size)
    pred = predict(X)
    grad += pinv(X) * (pred - y)
    weight -= lr * grad
  }

  def fit(X: DenseMatrix[Double], y: DenseVector[Double], lr: Double, max_iter: Int): Unit = {
    weight = DenseVector.ones[Double](X.cols)

    var best_loss = Double.MaxValue
    var err = Double.MaxValue
    for (i <- 0 to max_iter; if best_loss >= err) {
      best_loss = err
      grad_step(X, y, lr)
      err = loss(X, y)
      if (i % (max_iter / 10) == 0) {
        println(s"iter: $i, loss: $err, weight: $weight")
      }
    }
  }

  def get_weight(): DenseVector[Double] ={
    weight
  }
}

object Linear_Regression {
  def main(args: Array[String]) {
    val true_weight = DenseVector[Double](1.0, -2.0, 3.0, 4.0)
    val (x, y) = generate_data(true_weight)//load_data(pth)
    val lr = 0.001
    val max_iter = 50000

    val model = new LinearRegression()
    model.fit(x, y, lr, max_iter)
    val weight = model.get_weight()
    val mse = model.loss(x, y)

    println("\ntrue_weight:")
    for (w <- true_weight){
      print(s"$w ")
    }
    println(s"\n\npred_weight:")
    for (w <- weight){
      print(s"$w ")
    }
    println(s"\n\nMSE: $mse\n")

    val preds = model.predict(x)
    for (i <- 0 to preds.length - 1){
      println(y(i), " ", preds(i))
    }
  }

  def generate_data(weight: DenseVector[Double], size: Int = 40): (DenseMatrix[Double], DenseVector[Double]) = {
    val rand = new Random
    val x_max = 100
    val n_max = 5
    val X = DenseMatrix.fill[Double](size, weight.length)(rand.nextDouble() * x_max)
    val noise = DenseVector.fill[Double](size)(rand.nextDouble() * n_max)
    val y = X * weight + noise
    (X, y)
  }
}