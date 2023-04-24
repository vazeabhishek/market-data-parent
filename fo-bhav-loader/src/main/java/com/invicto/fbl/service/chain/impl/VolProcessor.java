package com.invicto.fbl.service.chain.impl;

import com.invicto.fbl.model.ContractEodAnalyticsVo;
import com.invicto.fbl.model.SignalEnum;
import com.invicto.fbl.service.calc.Calculator;
import com.invicto.fbl.service.chain.Processor;
import com.invicto.mdp.entity.ContractEodData;
import org.springframework.beans.factory.annotation.Autowired;

public class VolProcessor extends Processor {
    private Calculator volCalc;
    private double posThreshold = 3.0;
    private double negThreshold = -3.0;

    @Autowired
    public VolProcessor(Processor processor, Calculator oiCalc) {
        super.next = processor;
        this.volCalc = oiCalc;
    }

    // 3rd
    @Override
    public void process(ContractEodData latest, ContractEodData prev, ContractEodAnalyticsVo eodAnalyticsVo) {
        if(null != prev) {
            double volDeltaP = volCalc.calculate(latest, prev);
            eodAnalyticsVo.setDeltaVolumePercentage(volDeltaP);
            eodAnalyticsVo.setPriceSignal(SignalEnum.NEUTRAL.name());

            if (volDeltaP > posThreshold)
                eodAnalyticsVo.setPriceSignal(SignalEnum.VOL_BULLISH.name());

            if (volDeltaP < negThreshold)
                eodAnalyticsVo.setPriceSignal(SignalEnum.VOL_BEARISH.name());
        }

    }
}
