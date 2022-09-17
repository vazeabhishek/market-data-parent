package com.invicto.wui.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EquityVo implements Comparable<EquityVo>{
    private String ticker;
    private double open;
    private double close;
    private double high;
    private double low;
    private double prevClose;
    private double vol;
    private LocalDate collectionDate;

    @Override
    public int compareTo(EquityVo o) {
        EquityVo other = (EquityVo) o;
        if (other.collectionDate.isBefore(this.collectionDate)) {
            return 1;
        }
        if (other.collectionDate.isAfter(this.collectionDate)) {
            return -1;
        }
        return 0;
    }
}
