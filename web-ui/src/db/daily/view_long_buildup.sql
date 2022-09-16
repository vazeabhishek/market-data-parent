create or replace view view_long_buildup
as
select
    ct.id,
    ct.expiry_dt,
    ct.symbol,
    cda.analytics_date,
    cda.lower_low_count,
    cda.higher_high_count,
    cda.sellers_won_count,
    cda.buyers_won_count
from
    dailybhav.contract ct
join dailybhav.contract_data_analytics cda
on ct.id = cda.contract_id
where cda.signar = 'LONG_BUILD_UP'
and cda.higher_high_count > 1
order by analytics_date desc