-- based on tpc-ds q21

-- For all items whose price was changed on a given date,
-- compute the percentage change in inventory between the 30-day period BEFORE
-- the price change and the 30-day period AFTER the change. Group this
-- information by warehouse.

-- Resources

---------------------------------------------------------------------
-- WORKS, result is different --
---------------------------------------------------------------------


-- In Postgres, datediff is not implemented, consider using instead age and EXTRACT epoch
-- SELECT EXTRACT(epoch FROM age('2010-04-01', '2012-03-05')) / (3600*24);


SELECT w_warehouse_name,
           i_item_id,
           SUM(CASE
                   --WHEN datediff(day, d_date, '2001-05-08') < 0 THEN inv_quantity_on_hand
                   WHEN EXTRACT(epoch FROM age(d_date, '2001-05-08')) / (3600*24) < 0 THEN inv_quantity_on_hand
                   ELSE 0
               END) AS inv_before,
           SUM(CASE
                   --WHEN datediff(day, d_date, '2001-05-08') >= 0 THEN inv_quantity_on_hand
                   WHEN EXTRACT(epoch FROM age(d_date, '2001-05-08')) / (3600*24) >= 0 THEN inv_quantity_on_hand
                   ELSE 0
               END) AS inv_after
FROM inventory inv,
     item i,
     warehouse w,
     date_dim d
WHERE i_current_price BETWEEN 0.98 AND 1.5
  AND i_item_sk = inv_item_sk
  AND inv_warehouse_sk = w_warehouse_sk
  AND inv_date_sk = d_date_sk
  --AND datediff(dd, d_date, '2001-05-08') >= -30
  AND EXTRACT(epoch FROM age(d_date, '2001-05-08')) / (3600*24) >= -30
  --AND datediff(dd, d_date, '2001-05-08') <= 30
  AND EXTRACT(epoch FROM age(d_date, '2001-05-08')) / (3600*24) <= -30
GROUP BY w_warehouse_name,
         i_item_id
HAVING SUM(CASE
               --WHEN datediff(dd, d_date, '2001-05-08') < 0 THEN inv_quantity_on_hand
               WHEN EXTRACT(epoch FROM age(d_date, '2001-05-08')) / (3600*24) < 0 THEN inv_quantity_on_hand
               ELSE 0
           END) > 0
AND SUM(CASE
            --WHEN datediff(dd, d_date, '2001-05-08') >= 0 THEN inv_quantity_on_hand
            WHEN EXTRACT(epoch FROM age(d_date, '2001-05-08')) / (3600*24) >= 0 THEN inv_quantity_on_hand
            ELSE 0
        END) / SUM(CASE
                       --WHEN datediff(dd, d_date, '2001-05-08') < 0 THEN inv_quantity_on_hand
                       WHEN EXTRACT(epoch FROM age(d_date, '2001-05-08')) / (3600*24) < 0 THEN inv_quantity_on_hand
                       ELSE 0
                   END) >= 2.0 / 3.0
AND SUM(CASE
            --WHEN datediff(dd, d_date, '2001-05-08') >= 0 THEN inv_quantity_on_hand
            WHEN EXTRACT(epoch FROM age(d_date, '2001-05-08')) / (3600*24) >= 0 THEN inv_quantity_on_hand
            ELSE 0
        END) / SUM(CASE
                       --WHEN datediff(dd, d_date, '2001-05-08') < 0 THEN inv_quantity_on_hand
                       WHEN EXTRACT(epoch FROM age(d_date, '2001-05-08')) / (3600*24) < 0 THEN inv_quantity_on_hand
                       ELSE 0
                   END) <= 3.0 / 2.0
ORDER BY w_warehouse_name,
         i_item_id 
FETCH FIRST 100 ROWS ONLY;




--Result --------------------------------------------------------------------

-- the real query part
SELECT w_warehouse_name,
  i_item_id,
  --SUM( CASE WHEN datediff(d_date, '2001-05-08') < 0
  SUM( CASE WHEN EXTRACT(epoch FROM age(d_date, '2001-05-08')) / (3600*24) < 0
    THEN inv_quantity_on_hand
    ELSE 0 END
  ) AS inv_before,
  --SUM( CASE WHEN datediff(d_date, '2001-05-08') >= 0
  SUM( CASE WHEN EXTRACT(epoch FROM age(d_date, '2001-05-08')) / (3600*24) >= 0
    THEN inv_quantity_on_hand
    ELSE 0 END
  ) AS inv_after
FROM inventory inv,
  item i,
  warehouse w,
  date_dim d
WHERE i_current_price BETWEEN 0.98 AND 1.5
AND i_item_sk        = inv_item_sk
AND inv_warehouse_sk = w_warehouse_sk
AND inv_date_sk      = d_date_sk
--AND datediff(d_date, '2001-05-08') >= -30
AND EXTRACT(epoch FROM age(d_date, '2001-05-08')) / (3600*24) >= -30
--AND datediff(d_date, '2001-05-08') <= 30
AND EXTRACT(epoch FROM age(d_date, '2001-05-08')) / (3600*24) <= 30

GROUP BY w_warehouse_name, i_item_id
HAVING inv_before > 0
AND inv_after / inv_before >= 2.0 / 3.0
AND inv_after / inv_before <= 3.0 / 2.0
ORDER BY w_warehouse_name, i_item_id
FETCH FIRST 100 ROWS ONLY;


---- cleanup ----------------



