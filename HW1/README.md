# Introduction to BigData

## Задания уровня “Beginner”  

1) Пробросить порт (port forwarding) для доступа к HDFS Web UI  
    ```
    ssh -i emr.pem -N -L 50070:ec2-3-249-21-2.eu-west-1.compute.amazonaws.com:50070 hadoop@ec2-3-249-21-2.eu-west-1.compute.amazonaws.com  
    ```
2) Воспользоваться Web UI для того, чтобы найти папку “/data” в HDFS. Сколько подпапок в указанной папке /data?  
    ```
    http://localhost:50070/explorer.html#/data  
    ```
![](beginner_1.png)  
    ```
    Ответ: 1 папка - texts  
    ```

## Задания уровня “Intermediate”

```
ssh -i emr.pem hadoop@ec2-3-249-21-2.eu-west-1.compute.amazonaws.com
```
###  См. флаг “-ls”  
```
hdfs dfs -help ls
```
1. Вывести список всех файлов в /data/texts  
    ```
    hdfs dfs -ls -C /data/texts/  

    /data/texts/twain.txt  
    ```
2. См. п.1 + вывести размер файлов в “human readable” формате (т.е. не в байтах, а например в МБ, когда
размер файла измеряется от 1 до 1024 МБ).  
    ```
    hdfs dfs -ls -h /data/texts/  

    Found 1 items  
    -rw-r--r--   1 hadoop hadoop        714 2020-09-18 20:50 /data/texts/twain.txt  
    ```
3. Команда "hdfs dfs -ls" выводит актуальный размер файла (actual) или же объем пространства, занимаемый с
учетом всех реплик этого файла (total)? В ответе ожидается одно слово: actual или total.  
    ```
    actual  
    ```
### См. флаг “-du“

```
hdfs dfs -help du  
```
1. Приведите команду для получения размера пространства, занимаемого всеми файлами внутри
“/data/texts”. На выходе ожидается одна строка с указанием команды.  
    ```
    hdfs dfs -du -s /data/texts/  

    714  /data/texts/  
    ```

### См. флаги “-mkdir” и “-touchz“

```
hdfs dfs -help mkdir  
hdfs dfs -help touchz  
```
1. Создайте папку в корневой HDFS-папке Вашего пользователя, чтобы избежать конфликтов, на всякий
случай используйте Ваш id (см. grades) в качестве префикса папки.  
    ```
    hdfs dfs -mkdir dimarus  
    ```
2. Создайте в созданной папке новую вложенную папку.  
    ```
    hdfs dfs -mkdir dimarus/dimarus2  
    ```
3. Что такое Trash в распределенной FS? Как сделать так, чтобы файлы удалялись сразу, минуя “Trash”?  
    ```
   При удалении данных командой hdfs dfs -rm в распределенной FS они перемещаются в директорию Trash. Нужно поставить флаг -skipTrash, см. команду hdfs dfs -help rm.  
    ```
4. Создайте пустой файл в подпапке из пункта 2.  
    ```
    hdfs dfs -touchz dimarus/dimarus2/file.txt  
    ```
5. Удалите созданный файл.  
    ```
    hdfs dfs -rm -skipTrash dimarus/dimarus2/file.txt  

    Deleted dimarus/dimarus2/file.txt  
    ```
6. Удалите созданные папки.  
    ```
    hdfs dfs -rmr dimarus  

    rmr: DEPRECATED: Please use '-rm -r' instead.  
    Deleted dimarus  
    ```

### См. флаги “-put”, “-cat”, “-tail”, “-distcp”

1. Используя команду “-distcp” скопируйте рассказ О’Генри “Дары Волхвов” henry.txt из
s3://texts-bucket/henry.txt в новую папку на HDFS  
    ```
    hadoop distcp s3://texts-bucket/henry.txt dimarus/henry.txt  
    ```
2. Выведите содержимое HDFS-файла на экран.  
    ```
    hdfs dfs -cat dimarus/henry.txt  
    ```
3. Выведите содержимое нескольких последних строчек HDFS-файла на экран.  
    ```
    hdfs dfs -tail dimarus/henry.txt  

    или для конкретного количества строк  

    hdfs dfs -cat dimarus/henry.txt | tail -n 5  
    ```
4. Выведите содержимое нескольких первых строчек HDFS-файла на экран.  
    ```
    hdfs dfs -cat dimarus/henry.txt | head -n 5  
    ```
5. Переместите копию файла в HDFS на новую локацию.  
    ```
    Создание новой локации  
    hdfs dfs -mkdir dimarus2/  
    Непосредственно перемещение  
    hdfs dfs -cp dimarus/henry.txt dimarus2/henry.txt  
    ```  

```
Удаление всех созданных файлов и директорий, завершение секции “Intermediate”  
hdfs dfs -rm -r -skipTrash dimarus/  
hdfs dfs -rm -r -skipTrash dimarus2/  
```

## Задания уровня “Advanced”

```
Полезные флаги:  
● Для “hdfs dfs”, см. “-setrep -w”  
● hdfs fsck /path -files - blocks -locations  
```
2. Изменить replication factor для файла. Как долго занимает время на увеличение /
уменьшение числа реплик для файла?  
    ```
    Создание копии файла, для которого будет установлено новое количество репликаций  
    hadoop distcp s3://texts-bucket/henry.txt dimarus/henry.txt  

    Установка 2 репликаций прошла за пару секунд  
    hdfs dfs -setrep -w 2 dimarus/henry.txt  

    Replication 2 set: dimarus/henry.txt  
    Waiting for dimarus/henry.txt .... done    

    При дальнейшем уменьшении количества репликаций выводится предупреждение, о том что такая операция выполняется дольше увеличения количества репликаций.  

    hdfs dfs -setrep -w 1 dimarus/henry.txt  

    the waiting time may be long for DECREASING the number of replications
    ```
3. Найдите информацию по файлу, блокам и их расположениям с помощью “hdfs fsck”  
    ```
    hdfs fsck dimarus/henry.txt -files -blocks -locations  

    Connecting to namenode via http://ip-172-31-13-51.eu-west-1.compute.internal:50070/fsck?ugi=hadoop&files=1&blocks=1&locations=1&path=%2Fuser%2Fhadoop%2Fdimarus%2Fhenry.txt
    FSCK started by hadoop (auth:SIMPLE) from /172.31.13.51 for path /user/hadoop/dimarus/henry.txt at Mon Sep 21 19:11:34 UTC 2020
    /user/hadoop/dimarus/henry.txt 11180 bytes, 1 block(s):  OK
    0. BP-693885413-172.31.13.51-1600268446358:blk_1073743676_3464 len=11180 Live_repl=1 [DatanodeInfoWithStorage[172.31.13.237:50010,DS-6dc8ab2f-f15d-4128-85ec-8740e30f8b07,DISK]]

    Status: HEALTHY
    Total size:    11180 B
    Total dirs:    0
    Total files:   1
    Total symlinks:                0
    Total blocks (validated):      1 (avg. block size 11180 B)
    Minimally replicated blocks:   1 (100.0 %)
    Over-replicated blocks:        0 (0.0 %)
    Under-replicated blocks:       0 (0.0 %)
    Mis-replicated blocks:         0 (0.0 %)
    Default replication factor:    1
    Average block replication:     1.0
    Corrupt blocks:                0
    Missing replicas:              0 (0.0 %)
    Number of data-nodes:          2
    Number of racks:               1
    FSCK ended at Mon Sep 21 19:11:34 UTC 2020 in 5 milliseconds


    The filesystem under path '/user/hadoop/dimarus/henry.txt' is HEALTHY  
    ```
4. Получите информацию по любому блоку из п.2 с помощью "hdfs fsck -blockId”.
Обратите внимание на Generation Stamp (GS number).  
    ```
    hdfs fsck -blockId blk_1073743676  

    Connecting to namenode via http://ip-172-31-13-51.eu-west-1.compute.internal:50070/fsck?ugi=hadoop&blockId=blk_1073743676+&path=%2F
    FSCK started by hadoop (auth:SIMPLE) from /172.31.13.51 at Mon Sep 21 19:29:48 UTC 2020

    Block Id: blk_1073743676
    Block belongs to: /user/hadoop/dimarus/henry.txt
    No. of Expected Replica: 1
    No. of live Replica: 1
    No. of excess Replica: 0
    No. of stale Replica: 0
    No. of decommissioned Replica: 0
    No. of decommissioning Replica: 0
    No. of corrupted Replica: 0
    Block replica on datanode/rack: ip-172-31-13-237.eu-west-1.compute.internal/default-rack is HEALTHY
    ```  

```
Удаление всех созданных файлов и директорий, завершение секции “Advanced”  
hdfs dfs -rm -r -skipTrash dimarus/  
```
