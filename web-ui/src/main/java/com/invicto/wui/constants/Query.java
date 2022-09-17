package com.invicto.wui.constants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Query {

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-YYYY");

    private static final String GET_PREDICTION_QUERY = "SELECT TICKER, PREDICTED_PRICE, SIGNAL,TARGET, STOP_LOSS, PREDICTION_DATE FROM marketdata.view_prediction where PREDICTION_DATE = '{p_date}'";
    private static final String GET_LONG_BUILD_UP_QUERY = "SELECT SYMBOL, CONTRACTID, ANALYTICS_DATE, EXPIRY_DT, HIGHER_HIGH_COUNT, LOWER_LOW_COUNT, SELLERS_WON_COUNT, BUYERS_WON_COUNT FROM  marketdata.view_long_buildup WHERE ANALYTICS_DATE >= '{p_date}'";
    private static final String GET_SHORT_BUILD_UP_QUERY = "SELECT SYMBOL, CONTRACTID, ANALYTICS_DATE, EXPIRY_DT, HIGHER_HIGH_COUNT, LOWER_LOW_COUNT, SELLERS_WON_COUNT, BUYERS_WON_COUNT FROM  marketdata.view_short_buildup WHERE ANALYTICS_DATE >= '{p_date}'";

    private static final String GET_EQUITY_HISTORY = "SELECT TICKER, OPEN, CLOSE, HIGH, LOW, PREV_CLOSE, TOTAL_TRADED_QTY, COLLECTION_DATE FROM marketdata.view_equity_history WHERE TICKER = '{ticker}' AND  COLLECTION_DATE >= '{p_date}'";


    public static String getPredictionQuery(LocalDate date) {
        return GET_PREDICTION_QUERY.replace("{p_date}", date.format(dateTimeFormatter));
    }

    public static String getGetLongBuildUpQuery(LocalDate date) {
        return GET_LONG_BUILD_UP_QUERY.replace("{p_date}", date.format(dateTimeFormatter));
    }

    public static String getGetShortBuildUpQuery(LocalDate date) {
        return GET_SHORT_BUILD_UP_QUERY.replace("{p_date}", date.format(dateTimeFormatter));
    }

    public static String getGetEquityHistory(String symbol) {
        LocalDate fromDate = LocalDate.now().minusDays(30l);
        return GET_EQUITY_HISTORY.replace("{ticker}", symbol).replace("{p_date}", fromDate.format(dateTimeFormatter));
    }
}
