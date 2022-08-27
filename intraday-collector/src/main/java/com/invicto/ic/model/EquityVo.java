package com.invicto.ic.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquityVo {
    private String symbol;
    private int latestOI;
    private int prevOI;
    private int changeInOI;
    private double avgInOI;
    private int volume;
    private double futValue;
    private double optValue;
    private double total;
    private double premValue;
    private int underlyingValue;
}
