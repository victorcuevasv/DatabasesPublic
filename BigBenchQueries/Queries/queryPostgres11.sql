
-- For a given product, measure the correlation of sentiments, including
-- the number of reviews and average review ratings, on product monthly revenues
-- within a given time frame.

-- Resources

--Result  --------------------------------------------------------------------

---------------------------------------------------
-- NOT WORKING (LEFT SEMI JOIN)
---------------------------------------------------

-- the real query part
SELECT CORR(reviews_count,avg_rating)
FROM ( SELECT p.pr_item_sk AS pid, p.r_count AS reviews_count, p.avg_rating AS avg_rating, s.revenue AS m_revenue
       FROM ( SELECT pr_item_sk, count(*) AS r_count, avg(pr_review_rating) AS avg_rating
              FROM product_reviews
              WHERE pr_item_sk IS NOT NULL
              GROUP BY pr_item_sk ) p
              INNER JOIN 
              ( SELECT ws_item_sk, SUM(ws_net_paid) AS revenue
                FROM web_sales ws LEFT SEMIJOIN
                                     ( SELECT d_date_sk
                                       FROM date_dim d
                                       WHERE d.d_date >= '2003-01-02' AND d.d_date <= '2003-02-02') dd 
                                  ON ( ws.ws_sold_date_sk = dd.d_date_sk )
                WHERE ws_item_sk IS NOT NULL
                GROUP BY ws_item_sk) s 
              ON p.pr_item_sk = s.ws_item_sk) q11_review_stats;

--no sorting required, output is a single line

-- cleanup -------------------------------------------------------------
              
          