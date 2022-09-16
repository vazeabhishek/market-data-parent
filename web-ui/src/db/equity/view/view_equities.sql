create or replace view view_equities
as

select e.equity_record_no,e.last_updated_time,e.symbol,e1.ltp,e1.sentiment,e1.signal
from equity.equity e
left join view_latest15mA e1
on e.equity_record_no = e1.equity_equity_record_no
order by e.last_updated_time desc