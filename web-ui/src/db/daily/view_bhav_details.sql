create or replace view view_bhav_details
as
select analytics_date,
       delta_close_p,
       delta_oi_p,
       delta_vol_p,
       higher_high_count,
       lower_high_count,
       higher_low_count,
       lower_low_count,
       buyers_won_count,
       sellers_won_count,
       signar
from dailybhav.contract_data_analytics
order by analytics_date desc