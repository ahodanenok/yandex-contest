-- YandexCup2020.Backend.Final.C

CREATE TABLE operations (
    id BIGINT,
    account_id INT, /* лицевой счет балланса */
    operation_date DATE, /* дата учета операции */
    type VARCHAR(3), /* Дебет или кредит */
    value BIGINT UNSIGNED
);

INSERT INTO OPERATIONS VALUES (1, 1, '2020-04-08', 'DBT', 300);
INSERT INTO OPERATIONS VALUES (2, 1, '2020-04-10', 'DBT', 900);
INSERT INTO OPERATIONS VALUES (3, 1, '2020-04-12', 'KRD', 600);
INSERT INTO OPERATIONS VALUES (4, 2, '2020-03-20', 'DBT', 100);
INSERT INTO OPERATIONS VALUES (5, 2, '2020-04-11', 'DBT', 800);

INSERT INTO INPUT_DATE VALUES ('2020-04-14');

CREATE TABLE tt_result (
    account_id INT, /* лицевой счет баланса */
    penny BIGINT UNSIGNED
);

SET @to_date = (SELECT value FROM input_date LIMIT 1);

INSERT INTO tt_result (
  SELECT
    account_id,
    SUM(
      -- для каждого интервала найти задолженность за этот интервал и добавить в общую пеню
      --FLOOR(DATEDIFF(DAY, start_date, (CASE WHEN end_date = start_date or end_date > @to_date THEN @to_date ELSE end_date END)) * FLOOR(ABS(balance) / 300))
      FLOOR(DATEDIFF(DAY, start_date, (CASE WHEN end_date IS NULL OR end_date > @to_date THEN @to_date ELSE end_date END)) * FLOOR(ABS(balance) / 300))
    ) AS penny
  FROM (
    -- получить все интервалы на которых была задолженность [start_date, end_date) -> -value
    SELECT
      account_id,
      operation_date AS start_date,
      -- yandex выдает ошибку 'Crash' при использовании following
      --MAX(operation_date) OVER (PARTITION BY account_id ORDER BY account_id, operation_date ROWS BETWEEN CURRENT ROW AND 1 FOLLOWING) AS end_date,
      (SELECT operation_date FROM operations WHERE account_id = b.account_id AND operation_date > b.operation_date ORDER BY operation_date LIMIT 1) AS end_date,
      balance
    FROM (
      -- определить какое значение счета было на дату операции
      SELECT
        account_id,
        operation_date,
        SUM(value) OVER (
          -- для всех операций сложить value от первой операции до текущей
          PARTITION BY account_id ORDER BY account_id, operation_date ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
        ) AS balance
      FROM (
        -- объединить все операции за один день в одну строку с итоговым значением (+/-)value
        SELECT
          account_id,
          operation_date,
          SUM(CASE type WHEN 'DBT' THEN -value ELSE value END) AS value
        FROM operations
        GROUP BY account_id, operation_date
      )
      ORDER BY account_id, operation_date
    ) B
    WHERE balance < 0 AND operation_date <= @to_date
  )
  GROUP BY account_id
);
