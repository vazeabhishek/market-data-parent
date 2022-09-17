create or replace view marketdata.view_equity_history
as
select s.ticker,
       eed."open",
       eed."close",
       eed.high,
       eed.low,
       eed.prev_close,
       eed.total_traded_qty,
       eed.collection_date
from marketdata.equity_eod_data eed
join marketdata.equity e
on eed.equity_id  = e.id
join marketdata.symbol s
on e.symbol_id = s.id
order by eed.collection_date  desc