# Scala

## Linear regression

В рамках данного домашнего задания предлагается реализовать алгоритм линейной регрессии на Scala с использованием библиотеки Breeze.  
Решение можно оформить в одном из двух вариантов:

- в виде ноутбука на Scala
- в виде полноценного проекта на Scala

Решение должно принимать на вход тренировочные и тестовые данные в виде файлов. Выход решения - предсказание модели предлагается также реализовать через файл. В процессе обучения модели рекомендуется организовать валидацию и сохранять вывод ее результатов. В выборе данных и формате датасета не ограничиваем, можно взять простой вариант с 3-4 числовыми фичами.  

## Description

Решение оформлено в виде [проекта Scala](src/main/scala/main.scala). Датасет из 4 признаков генерируются случайным образом по заранее известной модели с добавлением шума. В стандартный поток вывода записаны: log обучения, веса исходной модели, предсказанные веса, MSE на всем датасете, [предсказания модели](src/main/scala/predict.csv) объединенные с реальными метками по всему датасету.  

```text
true_weight:
1.0 -2.0 3.0 4.0

pred_weight:
0.9986405901968335 -1.9809180190979305 3.0196921032148705 4.008955245225868

MSE: 1.9691025443299985
```
