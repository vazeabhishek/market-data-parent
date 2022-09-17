create or replace view marketdata.view_short_buildup
as
select s.ticker as symbol,
       c.id as contractId,
       cea.analytics_date,
       c.expiry_dt,
       cea.higher_high_count,
       cea.lower_low_count,
       cea.sellers_won_count,
       cea.buyers_won_count
from contract_eod_analytics cea
         join contract c
              on cea.contract_id  = c.id
         join symbol s
              on c.symbol_id = s.id
where cea.signal  = 'SHORT_BUILD_UP'
and cea.lower_low_count  > 1
order by analytics_date desc