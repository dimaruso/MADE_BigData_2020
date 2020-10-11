# Hive_QL  

Для данной задачи мы попрактикуемся в интерактивной среде Hue, где будем работать с Hive QL, создавая различные запросы.Веб интерфейс Hue доступен по [ссылке](https://demo.gethue.com/hue/accounts/login?next=/). В работе, мы будем использовать две таблицы: sample_07 и sample_08, которые представляют информацию о внутреннем устройстве отделов двух компаний с указанием количества сотрудников и их зарплат.  

Файл со всеми запросами: [queries.sql](queries.sql), результаты в директории [/query_results](query_results)  

1. Вывести название компании с максимальным количеством сотрудников и зарплатой по отделам. Названия компаний - названия файлов датасетов.  

    ``` sql
    CREATE VIEW COMPANIES AS
    SELECT
        emp_07,
        sal_07,
        emp_08,
        sal_08
    FROM
        (
            (
            SELECT
                42 AS prim_key,
                max(total_emp) AS emp_07,
                max(salary) AS sal_07
            FROM
                sample_07
            ) s07
        LEFT JOIN
            (
            SELECT
                42 AS prim_key,
                max(total_emp) AS emp_08,
                max(salary) AS sal_08
            FROM
                sample_08
            ) s08
        ON s07.prim_key = s08.prim_key
        )
    ;

    SELECT
        CASE
            WHEN emp_07 > emp_08 AND sal_07 > sal_08 THEN 'sample_07'
            WHEN emp_08 > emp_07 AND sal_08 > sal_07 THEN 'sample_08'
            ELSE 'equivalent'
        END AS company
    FROM
        COMPANIES;
    ```

2. Вывести список ID отделов обоих компаний, количество сотрудников в которых превышает 50000 человек.  

    ``` sql
    SELECT
        code
    FROM
        sample_07
    WHERE
        total_emp > 50000 AND description != "All Occupations";
    UNION
    SELECT
        code
    FROM
        sample_08
    WHERE
        total_emp > 50000 AND description != "All Occupations";
    ```

3. Для прошлого задания посчитать долю сотрудников отдела от всех сотрудников конкретной компании.  

    ``` sql
    SELECT
        7 sample, total_emp / sum(total_emp) OVER() part
    FROM
        sample_07
    WHERE
        description != "All Occupations" AND total_emp > 50000
    UNION ALL
    SELECT
        8 sample, total_emp / sum(total_emp) OVER() part
    FROM
        sample_08
    WHERE
        description != "All Occupations" AND total_emp > 50000;
    ```

4. Проверить, существуют ли отделы, работающие в одной компании, но отсутствующие в другой  

    ``` sql
    SELECT
        sample_07.code, sample_08.code
    FROM
        sample_07
    FULL JOIN
        sample_08
    ON
        sample_07.code = sample_08.code
    WHERE
        sample_07.code IS NULL OR sample_08.code IS NULL;
    ```

5. Отсортировать отделы компаний по суммарной зарплате по убыванию. Использовать информацию по отделам обеих компаний суммарно.  

    ``` sql
    SELECT
        sample_07.salary + sample_08.salary AS salary_sum
    FROM
        sample_07
    LEFT JOIN
        sample_08
    ON
        sample_07.description = sample_08.description
    ORDER BY
        sum DESC;
    ```
