package com.invicto.fbl.service.chain.impl;

import com.invicto.fbl.model.ContractEodAnalyticsVo;
import com.invicto.fbl.service.chain.Processor;
import com.invicto.mdp.entity.ContractEodData;
import lombok.extern.slf4j.Slf4j;

import static com.invicto.fbl.model.SignalEnum.*;

@Slf4j
public class SignalsProcessor extends Processor {

    //6th
    @Override
    public void process(ContractEodData latest, ContractEodData prev, ContractEodAnalyticsVo eodAnalyticsVo) {
        if(null != prev) {
            eodAnalyticsVo.setSignal(NEUTRAL.name());
            if (OI_BULLISH.name().equalsIgnoreCase(eodAnalyticsVo.getOiSignal()) &&
                    PRICE_BULLISH.name().equalsIgnoreCase(eodAnalyticsVo.getPriceSignal()) &&
                    VOL_BULLISH.name().equalsIgnoreCase(eodAnalyticsVo.getVolSignal())) {
                eodAnalyticsVo.setSignal(LONG_BUILD_UP.name());
                log.info("LONG {}",latest.getContract().getSymbol());
            }

            if (OI_BEARISH.name().equalsIgnoreCase(eodAnalyticsVo.getOiSignal()) &&
                    PRICE_BEARISH.name().equalsIgnoreCase(eodAnalyticsVo.getPriceSignal()) &&
                    VOL_BEARISH.name().equalsIgnoreCase(eodAnalyticsVo.getVolSignal())) {
                eodAnalyticsVo.setSignal(SHORT_BUILD_UP.name());
                log.info("SHORT {}",latest.getContract().getSymbol());
            }
        }
    }
}
