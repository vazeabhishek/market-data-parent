create or replace view view_eq_history
as
select e.symbol ,
       eda.collection_date ,
       eda.is_buy_contender,
       eda.is_strong_buy_contender,
       eda.is_sell_contender,
       eda.is_strong_sell_contender,
       eda.delta_vol_p
from dailyequity.equity e
join dailyequity.equity_data_analytics eda
on e.id  = eda.equity_id
order by collection_date desc