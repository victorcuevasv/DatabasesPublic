SELECT CAST(amc AS float) / CAST(pmc AS float) am_pm_ratio
FROM
  (SELECT COUNT(*) amc
   FROM web_sales ws
   JOIN household_demographics hd ON hd.hd_demo_sk = ws.ws_ship_hdemo_sk
   AND hd.hd_dep_count = 5
   JOIN time_dim td ON td.t_time_sk = ws.ws_sold_time_sk
   AND td.t_hour >= 7
   AND td.t_hour <= 8
   JOIN web_page wp ON wp.wp_web_page_sk = ws.ws_web_page_sk
   AND wp.wp_char_count >= 5000
   AND wp.wp_char_count <= 6000) AT
JOIN
  (SELECT COUNT(*) pmc
   FROM web_sales ws
   JOIN household_demographics hd ON ws.ws_ship_hdemo_sk = hd.hd_demo_sk
   AND hd.hd_dep_count = 5
   JOIN time_dim td ON td.t_time_sk = ws.ws_sold_time_sk
   AND td.t_hour >= 19
   AND td.t_hour <= 20
   JOIN web_page wp ON wp.wp_web_page_sk = ws.ws_web_page_sk
   AND wp.wp_char_count >= 5000
   AND wp.wp_char_count <= 6000) pt ON 1 = 1;
   
   