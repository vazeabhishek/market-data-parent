package com.invicto.epb.service.impl;

import com.invicto.epb.exceptions.ProcessorException;
import com.invicto.epb.model.vo.PredictionVo;
import com.invicto.epb.model.enums.SignalEnum;
import com.invicto.epb.model.enums.ViolationTypeEnum;
import com.invicto.epb.service.ContractService;
import com.invicto.epb.service.Processor;
import com.invicto.mdp.entity.Symbol;

import java.util.Optional;

public class ContractHistoryProcessor extends Processor {

    private final ContractService contractService;

    public ContractHistoryProcessor(Processor next, ContractService contractService) {
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
        Optional<String> longBuildsOptional = fetchLongBuildUps(predictionVo.getSymbol());
        Optional<String> shortBuildsOptional = fetchShortBuildUps(predictionVo.getSymbol());
        if (longBuildsOptional.isEmpty()) {
            predictionVo.setViolationType(ViolationTypeEnum.LONG_BUILD_UP_MISSING);
            predictionVo.setViolationText("No long build ups found");
            predictionVo.setSignal(SignalEnum.NEUTRAL);
            throw new ProcessorException("No long build ups found");
        }
        if (shortBuildsOptional.isPresent()) {
            predictionVo.setViolationType(ViolationTypeEnum.SHORT_BUILD_UP_EXITS);
            predictionVo.setViolationText(shortBuildsOptional.get());
            throw new ProcessorException("short build ups found");
        }
    }


    private void handleSell(PredictionVo predictionVo) {

        Optional<String> longBuildsOptional = fetchLongBuildUps(predictionVo.getSymbol());
        Optional<String> shortBuildsOptional = fetchShortBuildUps(predictionVo.getSymbol());
        if (shortBuildsOptional.isEmpty()) {
            predictionVo.setViolationType(ViolationTypeEnum.SHORT_BUILD_UP_MISSING);
            predictionVo.setViolationText("No short build ups found");
            predictionVo.setSignal(SignalEnum.NEUTRAL);
            throw new ProcessorException("No short build ups found");
        }
        if (longBuildsOptional.isPresent()) {
            predictionVo.setViolationType(ViolationTypeEnum.LONG_BUILD_UP_EXIST);
            predictionVo.setViolationText(shortBuildsOptional.get());
            throw new ProcessorException("long build ups found");
        }

    }

    private Optional<String> fetchLongBuildUps(Symbol symbol) {
        return contractService.fetchLongBuildUps(symbol, 10);
    }

    private Optional<String> fetchShortBuildUps(Symbol symbol) {
        return contractService.fetchShortBuildUps(symbol, 10);
    }
}
