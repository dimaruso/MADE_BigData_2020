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

    Установка 5 репликаций прошла более чем за 2 часа  
    hdfs dfs -setrep -w 5 dimarus/henry.txt  

    Replication 5 set: dimarus/henry.txt  
    Waiting for dimarus/henry.txt  .......................................................................................................................................................................................... done  
    ```
3. Найдите информацию по файлу, блокам и их расположениям с помощью “hdfs fsck”  
    ```
    hdfs dfs -fsck  
    ```
4. Получите информацию по любому блоку из п.2 с помощью "hdfs fsck -blockId”.
Обратите внимание на Generation Stamp (GS number).  
    ```
    hdfs dfs -blockId  
    ```
