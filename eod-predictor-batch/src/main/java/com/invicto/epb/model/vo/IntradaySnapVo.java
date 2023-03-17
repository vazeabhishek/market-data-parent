package com.invicto.epb.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class IntradaySnapVo {
    private double underlyingValue;
    private double prevOi;
    private double oi;
    private double chgInOi;
    private double volume;
    private LocalDate collectionDate;
    private LocalTime collectionTime;
}
