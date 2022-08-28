package com.invicto.fbl.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class EquityDerivativeCsvRecord {
    private String instrument;
    private String symbol;
    private LocalDate expiryDt;
    private double strikePrice;
    private String optionTYpe;
    private double open;
    private double high;
    private double low;
    private double close;
    private double settle;
    private double contracts;
    private double valInLakh;
    private double oi;
    private double coi;
    private LocalDate timestamp;

    public void setExpiryDt(String expiryDt) {
        this.expiryDt = LocalDate.parse(expiryDt);
    }

    public void setExpiryDt(String expiryDt, DateTimeFormatter formatter) {
        this.expiryDt = LocalDate.parse(expiryDt, formatter);
    }

    public void setStrikePrice(String strikePrice) {
        this.strikePrice = Double.parseDouble(strikePrice);
    }

    public void setOpen(String open) {
        this.open = Double.parseDouble(open);
    }

    public void setHigh(String high) {
        this.high = Double.parseDouble(high);
    }

    public void setLow(String low) {
        this.low = Double.parseDouble(low);
    }

    public void setClose(String close) {
        this.close = Double.parseDouble(close);
    }

    public void setSettle(String settle) {
        this.settle = Double.parseDouble(settle);
    }

    public void setContracts(String contracts) {
        this.contracts = Double.parseDouble(contracts);
    }

    public void setValInLakh(String valInLakh) {
        this.valInLakh = Double.parseDouble(valInLakh);
    }

    public void setOi(String oi) {
        this.oi = Double.parseDouble(oi);
    }

    public void setCoi(String coi) {
        this.coi = Double.parseDouble(coi);
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = LocalDate.parse(timestamp);
    }

    public void setTimestamp(String timestamp, DateTimeFormatter formatter) {
        this.timestamp = LocalDate.parse(timestamp, formatter);
    }

}
