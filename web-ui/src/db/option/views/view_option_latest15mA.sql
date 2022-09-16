create or replace view view_option_latest15mA
as
select a1.collection_time,a1.ltp,a1.option_option_record_no,a1.trend,a1.strength,a1.sentiment,a1.signal
from option.option_15m_a a1
join view_option_max_collection_time b
on a1.option_option_record_no = b.option_option_record_no
and a1.collection_time = b.max