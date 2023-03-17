package com.invicto.epb.service.impl;

import com.invicto.epb.exceptions.ProcessorException;
import com.invicto.epb.model.vo.PredictionVo;
import com.invicto.epb.model.enums.SignalEnum;
import com.invicto.epb.model.enums.ViolationTypeEnum;
import com.invicto.epb.service.Processor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OIProcessor extends Processor {

    private final double OI_THRESHOLD_LONG = 3.0;
    private final double OI_THRESHOLD_SHORT = -3.0;
    private final String OI_VIOLATION_STRING = "delta OI is not greater or lesser than threshold";

    public OIProcessor(Processor next) {
        super(next);
    }

    @Override
    public void process(PredictionVo predictionVo) {
        setPotentialSignal(predictionVo);
        if (predictionVo.getSignal() == SignalEnum.NEUTRAL) {
            predictionVo.setViolationType(ViolationTypeEnum.OI_CHECK);
            predictionVo.setViolationText(OI_VIOLATION_STRING);
            throw new ProcessorException(OI_VIOLATION_STRING);
        }
    }

    private void setPotentialSignal(PredictionVo predictionVo) {
        predictionVo.setSignal(SignalEnum.NEUTRAL);
        if (predictionVo.getPDeltaOi() >= OI_THRESHOLD_LONG)
            predictionVo.setSignal(SignalEnum.BUY);

        if (predictionVo.getPDeltaOi() <= OI_THRESHOLD_SHORT)
            predictionVo.setSignal(SignalEnum.SELL);
    }

}
