package com.invicto.fbl.service.chain.impl;

import com.invicto.fbl.model.ContractEodAnalyticsVo;
import com.invicto.fbl.model.SignalEnum;
import com.invicto.fbl.service.calc.Calculator;
import com.invicto.fbl.service.chain.Processor;
import com.invicto.mdp.entity.ContractEodData;

public class OiProcessor extends Processor {


    private Calculator oiCalc;
    private double posThreshold = 3.0;
    private double negThreshold = -3.0;

    public OiProcessor(Processor next, Calculator oiCalc) {
        super.next = next;
        this.oiCalc = oiCalc;
    }

    // 1st
    @Override
    public void process(ContractEodData latest, ContractEodData prev, ContractEodAnalyticsVo eodAnalyticsVo) {
        if(null != prev) {
            double oiDeltaP = oiCalc.calculate(latest, prev);
            eodAnalyticsVo.setDeltaOiPercentage(oiDeltaP);
            eodAnalyticsVo.setOiSignal(SignalEnum.NEUTRAL.name());

            if (oiDeltaP > posThreshold)
                eodAnalyticsVo.setOiSignal(SignalEnum.OI_BULLISH.name());

            if (oiDeltaP < negThreshold)
                eodAnalyticsVo.setOiSignal(SignalEnum.OI_BEARISH.name());
        }

    }
}
