package com.invicto.wui.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PredictionVO {
    private String symbol;
    private String signal;
    private double predictedPrice;
    private double target;
    private double stopLoss;
}
