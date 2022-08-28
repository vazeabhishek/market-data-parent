package com.invicto.ebl.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EqCsvRecord {
    private String symbol;
    private String series;
    private String open;
    private String high;
    private String low;
    private String close;
    private String last;
    private String prevClose;
    private String totalTradedQty;
    private String totalTradedVol;
    private String timestamp;
    private String totalTrades;
    private String ISIN;
}
