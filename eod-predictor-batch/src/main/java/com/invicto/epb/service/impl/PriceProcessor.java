package com.invicto.epb.service.impl;

import com.invicto.epb.exceptions.ProcessorException;
import com.invicto.epb.model.vo.EquityDataVo;
import com.invicto.epb.model.vo.PredictionVo;
import com.invicto.epb.model.enums.SignalEnum;
import com.invicto.epb.model.enums.ViolationTypeEnum;
import com.invicto.epb.service.bridge.EquityService;
import com.invicto.epb.service.Processor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PriceProcessor extends Processor {

    private final double PRICE_THRESHOLD_LONG = 3.0;
    private final double PRICE_THRESHOLD_SHORT = -4.0;
    private final String PRICE_VIOLATION_STRING = "delta Price is not greater or lesser than threshold";

    private final EquityService equityService;


    public PriceProcessor(Processor next, EquityService equityService) {
        super(next);
        this.equityService = equityService;
    }

    @Override
    public void process(PredictionVo predictionVo) {
        double pPclose = calculateDeltaPercentToPrevDay(predictionVo);
        switch (predictionVo.getSignal()) {
            case BUY:
                handleBuy(pPclose, predictionVo);
                break;
            case SELL:
                handleSell(pPclose, predictionVo);
                break;
        }
    }

    private double calculateDeltaPercentToPrevDay(PredictionVo predictionVo) {
        EquityDataVo prevDayData = equityService.getPreviousDayUnderlyingValue(predictionVo.getSymbol());
        return ((predictionVo.getPDeltaPrice() - prevDayData.getClose()) / prevDayData.getClose()) * 100;
    }

    private void handleBuy(double pPclose, PredictionVo predictionVo) {
        if (pPclose < PRICE_THRESHOLD_LONG) {
            predictionVo.setSignal(SignalEnum.NEUTRAL);
            predictionVo.setViolationType(ViolationTypeEnum.PRICE_CHECK);
            predictionVo.setViolationText(PRICE_VIOLATION_STRING);
            throw new ProcessorException(PRICE_VIOLATION_STRING);
        }
    }

    private void handleSell(double pPclose, PredictionVo predictionVo) {
        if (pPclose > PRICE_THRESHOLD_SHORT) {
            predictionVo.setSignal(SignalEnum.NEUTRAL);
            predictionVo.setViolationType(ViolationTypeEnum.PRICE_CHECK);
            predictionVo.setViolationText(PRICE_VIOLATION_STRING);
            throw new ProcessorException(PRICE_VIOLATION_STRING);
        }
    }
}
