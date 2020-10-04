# Заданиe на MapReduce  

1. Загрузите [датасет](https://www.kaggle.com/dgomonov/new-york-city-airbnb-open-data) по ценам на жилье Airbnb, доступный на kaggle.com.  

    ```
    Скачан и сохранен архив с данными archive.zip

    Затем перенесен на кластер с помощью команды:
    scp -i emr.pem /archive.zip/AB_NYC_2019.csv hadoop@ec2-3-249-21-2eu-west-1.compute.amazonaws.com:./hw2_data
    hadoop distcp s3://hw2_data/AB_NYC_2019.csv /dirus/AB_NYC_2019.csv
    ```

2. Подсчитайте среднее значение и дисперсию по признаку ”price” стандартными способами (”чистый код” или использование библиотек). Не учитывайте пропущенные значения при подсчете статистик.  

    В [ноутбуке](main.ipynb) по всем данным локально получены значения:  
    mean = 152.72, var = 57672.84  

3. Используя Python или Java, реализуйте скрипт mapper и reducer для расчета каждой из двух величин. В итоге у вас должно получиться 4 скрипта: 2 mapper и 2 reducer для каждой величины.  

    Реализованы скрипты:  
    [mapper](mapper_mean.py) и [reducer](reducer_mean.py) - Для расчета среднего  
    [mapper](mapper_var.py) и [reducer](reducer_var.py) - Для расчета дисперсии  

    Так же локально выполнены команды:  
    `cat AB_NYC_2019.csv | python mapper_mean.py | python reducer_mean.py`  
    mean = 152.72

    `cat AB_NYC_2019.csv | python mapper_var.py | python reducer_var.py`  
    var = 57672.84  

    Их результаты полностью сходятся с тем, что получено в ноутбуке без использования MapReduce  

    Скрипты загружены на кластер с помощью команд:  
    ```
    scp -i emr.pem mapper_mean.py hadoop@ec2-3-249-21-2.eu-west-1.compute.amazonaws.com:./dirus/mapper_mean.py  

    scp -i emr.pem reducer_mean.py hadoop@ec2-3-249-21-2.eu-west-1.compute.amazonaws.com:./dirus/reducer_mean.py  

    scp -i emr.pem mapper_var.py hadoop@ec2-3-249-21-2.eu-west-1.compute.amazonaws.com:./dirus/mapper_var.py  

    scp -i emr.pem reducer_var.py hadoop@ec2-3-249-21-2.eu-west-1.compute.amazonaws.com:./dirus/reducer_var.py  
    ```
    Запуск распределенных вычислений выполняется с помощью команд:  
    ```
    ssh -i emr.pem hadoop@ec2-3-249-21-2.eu-west-1.compute.amazonaws.com  
    cd dirus
    hadoop jar /usr/lib/hadoop-mapreduce/hadoop-streaming.jar -files mapper_mean.py,reducer_mean.py -mapper "python mapper_mean.py" -reducer "python reducer_mean.py" -input first_1000.csv -output mean  

    hadoop jar /usr/lib/hadoop-mapreduce/hadoop-streaming.jar -files mapper_var.py,reducer_var.py -mapper "python mapper_var.py" -reducer "python reducer_var.py" -input first_1000.csv -output var
    ```  

    Получены значения:  

    mean = 152.72, var = 57672.84  

4. Проверьте правильность подсчета статистик методом map-reduce в сравнении со стандартным подходом  

    Статистики, полученные методом map-reduce, практически не отличаются от тех же статистик, полученных с помощью стандартного подхода.  

5. Результаты сравнения (то есть, подсчета двумя разными способами) для среднего значения и дисперсии запишите в файл .txt. В итоге, у вас должно получиться две пары значений (стандартного расчета и map-reduce)- одна пара для среднего, другая - для дисперсии.  

    Результат сравнения записан в следующий [файл](result.txt)

6. Итоговый файл архива с выполненным заданием должен включать в себя сам код, а также результаты его работы.  

    В качестве альтернативы архиву, представлен данный репозиторий.  
