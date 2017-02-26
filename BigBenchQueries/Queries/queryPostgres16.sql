
-- based on tpc-ds q40
-- Compute the impact of an item price change on the
-- store sales by computing the total sales for items in a 30 day period before and
-- after the price change. Group the items by location of warehouse where they
-- were delivered from.

-- Resources


SELECT w_state,
           i_item_id,
           SUM(CASE
                   WHEN (CAST(d_date AS DATE) < CAST('2001-03-16' AS DATE)) THEN ws_sales_price - COALESCE(wr_refunded_cash,0)
                   ELSE 0.0
               END) AS sales_before,
           SUM(CASE
                   WHEN (CAST(d_date AS DATE) >= CAST('2001-03-16' AS DATE)) THEN ws_sales_price - COALESCE(wr_refunded_cash,0)
                   ELSE 0.0
               END) AS sales_after
FROM
  (SELECT *
   FROM web_sales ws
   LEFT OUTER JOIN web_returns wr ON (ws.ws_order_number = wr.wr_order_number
                                      AND ws.ws_item_sk = wr.wr_item_sk)) a1
JOIN item i ON a1.ws_item_sk = i.i_item_sk
JOIN warehouse w ON a1.ws_warehouse_sk = w.w_warehouse_sk
JOIN date_dim d ON a1.ws_sold_date_sk = d.d_date_sk
AND CAST(d.d_date AS DATE) >= CAST('2001-03-16' AS DATE) - 30*24*60*60
AND CAST(d.d_date AS DATE) <= CAST('2001-03-16' AS DATE) + 30*24*60*60
GROUP BY w_state,
         i_item_id
ORDER BY w_state,
         i_item_id
FETCH FIRST 100 ROWS ONLY;
    
         
 