
-- Find the categories with flat or declining sales for in store purchases
-- during a given year for a given store.

------------------------------------------------------
-- DOES NOT WORK (LEFT SEMI JOIN) --
------------------------------------------------------

-- Resources

SELECT * FROM ( SELECT cat, ((count(x) * SUM(xy) - SUM(x) * SUM(y)) / (count(x) * SUM(xx) - SUM(x) * SUM(x)) ) AS slope, (SUM(y) - ((count(x) * SUM(xy) - SUM(x) * SUM(y)) / (count(x) * SUM(xx) - SUM(x)*SUM(x)) ) * SUM(x)) / count(x) AS intercept FROM ( SELECT i.i_category_id AS cat, s.ss_sold_date_sk AS x, SUM(s.ss_net_paid) AS y, s.ss_sold_date_sk * SUM(s.ss_net_paid) AS xy, s.ss_sold_date_sk * s.ss_sold_date_sk AS xx FROM store_sales s LEFT SEMI JOIN ( SELECT d_date_sk FROM date_dim d WHERE d.d_date >= '2001-09-02' AND   d.d_date <= '2002-09-02') dd ON ( s.ss_sold_date_sk=dd.d_date_sk ) INNER JOIN item i ON s.ss_item_sk = i.i_item_sk WHERE i.i_category_id IS NOT NULL AND s.ss_store_sk = 10 GROUP BY i.i_category_id, s.ss_sold_date_sk) temp GROUP BY cat) regression WHERE slope <= 0 ORDER BY cat ;




--Result  --------------------------------------------------------------------
SELECT *
FROM (
  SELECT
    cat,
    --input:
    --SUM(x)as sumX,
    --SUM(y)as sumY,
    --SUM(xy)as sumXY,
    --SUM(xx)as sumXSquared,
    --count(x) as N,

    --formula stage1 (logical):
    --N * sumXY - sumX * sumY AS numerator,
    --N * sumXSquared - sumX*sumX AS denom
    --numerator / denom as slope,
    --(sumY - slope * sumX) / N as intercept
    --
    --formula stage2(inserted hive aggregations): 
    --(count(x) * SUM(xy) - SUM(x) * SUM(y)) AS numerator,
    --(count(x) * SUM(xx) - SUM(x) * SUM(x)) AS denom
    --numerator / denom as slope,
    --(sum(y) - slope * sum(x)) / count(X) as intercept
    --
    --Formula stage 3: (insert numerator and denom into slope and intercept function)
    ((count(x) * SUM(xy) - SUM(x) * SUM(y)) / (count(x) * SUM(xx) - SUM(x) * SUM(x)) ) AS slope,
    (SUM(y) - ((count(x) * SUM(xy) - SUM(x) * SUM(y)) / (count(x) * SUM(xx) - SUM(x)*SUM(x)) ) * SUM(x)) / count(x) AS intercept
  FROM (
    SELECT
      i.i_category_id AS cat, -- ranges from 1 to 10
      s.ss_sold_date_sk AS x,
      SUM(s.ss_net_paid) AS y,
      s.ss_sold_date_sk * SUM(s.ss_net_paid) AS xy,
      s.ss_sold_date_sk * s.ss_sold_date_sk AS xx
    FROM store_sales s
    -- select date range
    LEFT SEMI JOIN (
      SELECT d_date_sk
      FROM date_dim d
      WHERE d.d_date >= '2001-09-02'
      AND   d.d_date <= '2002-09-02'
    ) dd ON ( s.ss_sold_date_sk=dd.d_date_sk )
    INNER JOIN item i ON s.ss_item_sk = i.i_item_sk
    WHERE i.i_category_id IS NOT NULL
    AND s.ss_store_sk = 10 -- for a given store ranges from 1 to 12
    GROUP BY i.i_category_id, s.ss_sold_date_sk
  ) temp
  GROUP BY cat
) regression
WHERE slope <= 0
ORDER BY cat
-- limit not required, number of categories is known to be small and of fixed size across scalefactors
;


