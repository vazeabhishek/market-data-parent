create or replace view marketdata.view_prediction
as
select s.ticker,
       sep.predicted_price,
       sep.signal,
       sep.target,
       sep.stop_loss,
       sep.prediction_date
from marketdata.symbol s
join marketdata.symbol_eod_prediction sep
on s.id = sep.symbol_id