package com.invicto.epb.service.impl;

import com.invicto.epb.model.enums.SignalEnum;
import com.invicto.epb.model.vo.PredictionVo;
import com.invicto.epb.service.Processor;
import com.invicto.epb.service.bridge.EquityService;

public class FinalProcessor extends Processor {

    private final EquityService equityService;

    public FinalProcessor(Processor next, EquityService equityService) {
        super(next);
        this.equityService = equityService;
    }

    @Override
    protected void process(PredictionVo predictionVo) {
        if(predictionVo.getSignal() == SignalEnum.BUY){
            predictionVo.setTarget(equityService.getLatestHigh(predictionVo.getSymbol(),predictionVo.getUnderlyingPrice(),2,predictionVo.getRunDate().minusDays(1)));
            predictionVo.setStopLoss(equityService.getLatestHigh(predictionVo.getSymbol(),predictionVo.getUnderlyingPrice(),1,predictionVo.getRunDate().minusDays(1)));
        }

        if(predictionVo.getSignal() == SignalEnum.SELL){
            predictionVo.setStopLoss(equityService.getLatestHigh(predictionVo.getSymbol(),predictionVo.getUnderlyingPrice(),2,predictionVo.getRunDate().minusDays(1)));
            predictionVo.setTarget(equityService.getLatestHigh(predictionVo.getSymbol(),predictionVo.getUnderlyingPrice(),1,predictionVo.getRunDate().minusDays(1)));
        }
    }
}
