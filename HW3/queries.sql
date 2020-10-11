-- 1

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

-- 2

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

-- 3

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

-- 4

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

-- 5

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
