package com.invicto.fbl.service.chain.impl;

import com.invicto.fbl.helper.MathsHelper;
import com.invicto.fbl.model.ContractEodAnalyticsVo;
import com.invicto.fbl.service.chain.Processor;
import com.invicto.mdp.entity.ContractEodData;
import org.springframework.beans.factory.annotation.Autowired;

public class WickProcessor extends Processor {

    private MathsHelper mathsHelper;

    @Autowired
    public WickProcessor(Processor processor,MathsHelper mathsHelper) {
        super.next = processor;
        this.mathsHelper = mathsHelper;
    }

    // 4th
    @Override
    public void process(ContractEodData latest, ContractEodData prev, ContractEodAnalyticsVo eodAnalyticsVo) {
            double candleBody = Math.abs(latest.getClose() - latest.getOpen());
            if (latest.getClose() > latest.getOpen()) {
                double sellWick = mathsHelper.calculatePercentage((latest.getHigh() - latest.getClose()), candleBody);
                double buyWick = mathsHelper.calculatePercentage((latest.getOpen() - latest.getLow()), candleBody);

                eodAnalyticsVo.setBuyWickPercentage(buyWick);
                eodAnalyticsVo.setSellWickPercentage(sellWick);
            }

            if (latest.getOpen() > latest.getClose()) {
                double sellWick = mathsHelper.calculatePercentage((latest.getHigh() - latest.getOpen()), candleBody);
                double buyWick = mathsHelper.calculatePercentage((latest.getClose() - latest.getLow()), candleBody);
                eodAnalyticsVo.setBuyWickPercentage(buyWick);
                eodAnalyticsVo.setSellWickPercentage(sellWick);
            }
    }
}
