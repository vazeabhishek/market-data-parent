package com.invicto.fbl.service.chain.impl;

import com.invicto.fbl.exception.RuleViolationException;
import com.invicto.fbl.model.ContractEodAnalyticsVo;
import com.invicto.fbl.model.SignalEnum;
import com.invicto.fbl.service.calc.Calculator;
import com.invicto.fbl.service.chain.Processor;
import com.invicto.mdp.entity.ContractEodData;
import org.springframework.beans.factory.annotation.Autowired;

public class PriceProcessor extends Processor {
    private Calculator oiCalc;
    private double posThreshold = 3.0;
    private double negThreshold = -3.0;

    public PriceProcessor(Processor next, Calculator oiCalc) {
        super.next = next;
        this.oiCalc = oiCalc;
    }

    // 2nd
    @Override
    public void process(ContractEodData latest, ContractEodData prev, ContractEodAnalyticsVo eodAnalyticsVo) {
        if(null != prev) {
            double priceDeltaP = oiCalc.calculate(latest, prev);
            eodAnalyticsVo.setDeltaClosePercentage(priceDeltaP);
            eodAnalyticsVo.setPriceSignal(SignalEnum.NEUTRAL.name());

            if (priceDeltaP > posThreshold && latest.getHigh() > prev.getHigh())
                eodAnalyticsVo.setPriceSignal(SignalEnum.PRICE_BULLISH.name());

            if (priceDeltaP < negThreshold && latest.getLow() < prev.getLow())
                eodAnalyticsVo.setPriceSignal(SignalEnum.PRICE_BEARISH.name());

            if (eodAnalyticsVo.getPriceSignal().equalsIgnoreCase(SignalEnum.NEUTRAL.name())) {
                eodAnalyticsVo.setReason("Price Check failed " + priceDeltaP);
                throw new RuleViolationException("Price check failed");
            }
        }

    }
}
