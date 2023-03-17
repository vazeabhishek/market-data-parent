package com.invicto.epb.service;

import com.invicto.epb.model.vo.PredictionVo;

import java.util.Objects;

public abstract class Processor {
    protected Processor next;

    protected Processor(Processor next) {
        this.next = next;
    }

    public void execute(PredictionVo predictionVo) {
        process(predictionVo);
        if (Objects.nonNull(next))
            next.execute(predictionVo);
    }
    protected abstract void process(PredictionVo predictionVo);
}
