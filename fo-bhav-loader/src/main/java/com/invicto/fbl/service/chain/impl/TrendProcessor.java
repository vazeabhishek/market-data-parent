package com.invicto.fbl.service.chain.impl;

import com.invicto.fbl.model.ContractEodAnalyticsVo;
import com.invicto.fbl.service.chain.Processor;
import com.invicto.mdp.entity.ContractEodData;


public class TrendProcessor extends Processor {

    public TrendProcessor(Processor processor) {
        super.next = processor;
    }

    //5th
    @Override
    public void process(ContractEodData latest, ContractEodData prev, ContractEodAnalyticsVo eodAnalyticsVo) {
        if (null != prev) {
            if (latest.getHigh() > prev.getHigh() && latest.getClose() > prev.getClose())
                eodAnalyticsVo.setHigherHighCount(true);
            if (latest.getLow() < prev.getLow() && latest.getClose() < prev.getClose())
                eodAnalyticsVo.setLowerLowCount(true);
            if (latest.getLow() > prev.getLow())
                eodAnalyticsVo.setHigherLowCount(true);
            if (latest.getHigh() < prev.getHigh())
                eodAnalyticsVo.setLowerHighCount(true);
        }
    }
}
