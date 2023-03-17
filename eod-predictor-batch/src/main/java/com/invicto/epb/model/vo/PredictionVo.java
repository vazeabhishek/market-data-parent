package com.invicto.epb.model.vo;

import com.invicto.epb.model.enums.SignalEnum;
import com.invicto.epb.model.enums.ViolationTypeEnum;
import com.invicto.mdp.entity.Symbol;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PredictionVo {
    private Symbol symbol;
    private double pDeltaOi; // Percentage of OI change against previous day
    private double pDeltaPrice;// Percentage of Closing Price change against previous day
    private double pDeltaVol; // Percentage of Volume against prev day
    private SignalEnum signal;
    private ViolationTypeEnum violationType;
    private String violationText;
}
