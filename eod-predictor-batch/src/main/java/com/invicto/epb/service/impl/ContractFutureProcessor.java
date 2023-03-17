package com.invicto.epb.service.impl;

import com.invicto.epb.exceptions.ProcessorException;
import com.invicto.epb.model.vo.PredictionVo;
import com.invicto.epb.model.enums.SignalEnum;
import com.invicto.epb.model.enums.ViolationTypeEnum;
import com.invicto.epb.service.bridge.ContractService;
import com.invicto.epb.service.Processor;

public class ContractFutureProcessor extends Processor {

    private final ContractService contractService;

    public ContractFutureProcessor(Processor next, ContractService contractService) {
        super(next);
        this.contractService = contractService;
    }
    @Override
    public void process(PredictionVo predictionVo) {
        switch (predictionVo.getSignal()) {
            case BUY:
                handleBuy(predictionVo);
                break;
            case SELL:
                handleSell(predictionVo);
                break;
        }
    }

    private void handleBuy(PredictionVo predictionVo) {
        if(contractService.isLongDiscountedForCurrentExpiry(predictionVo.getSymbol()))
        {
            predictionVo.setViolationType(ViolationTypeEnum.FUTURE_IS_AT_DISCOUNT);
            predictionVo.setViolationText("Futures are trading at discounted price");
            predictionVo.setSignal(SignalEnum.NEUTRAL);
            throw new ProcessorException("Futures are trading at discounted price");
        }
    }

    private void handleSell(PredictionVo predictionVo) {

        if(contractService.isShortPremiumForCurrentExpiry(predictionVo.getSymbol()))
        {
            predictionVo.setViolationType(ViolationTypeEnum.FUTURE_IS_AT_PREMIUM);
            predictionVo.setViolationText("Futures are trading at premium price");
            predictionVo.setSignal(SignalEnum.NEUTRAL);
            throw new ProcessorException("Futures are trading at premium price");
        }

    }
}
