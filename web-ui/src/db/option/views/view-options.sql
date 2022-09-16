create or replace view view_options
as

select e.option_record_no,e.last_updated_time,e.identifier,e1.ltp,e1.sentiment,e1.signal
from option.option e
left join view_option_latest15mA e1
on e.option_record_no = e1.option_option_record_no
order by e.last_updated_time desc