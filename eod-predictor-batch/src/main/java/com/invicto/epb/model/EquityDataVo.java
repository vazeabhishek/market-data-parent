package com.invicto.epb.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EquityDataVo {
    private double close;
    private double volume;
    private double high;
    private double low;
}
