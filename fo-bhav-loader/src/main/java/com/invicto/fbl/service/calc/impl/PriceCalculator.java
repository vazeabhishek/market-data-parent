package com.invicto.fbl.service.calc.impl;

import com.invicto.fbl.helper.MathsHelper;
import com.invicto.fbl.model.ContractEodAnalyticsVo;
import com.invicto.fbl.service.calc.Calculator;
import com.invicto.mdp.entity.ContractEodData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

public class PriceCalculator implements Calculator {
    private MathsHelper mathsHelper;

    public PriceCalculator(MathsHelper mathsHelper) {
        this.mathsHelper = mathsHelper;
    }

    @Override
    public double calculate(ContractEodData latest, ContractEodData previous) {
        if(null == previous)
            return 0.0;
        double delta = mathsHelper.calculateDelta(latest.getClose(), previous.getClose());
        return mathsHelper.calculatePercentage(delta, previous.getClose());
    }
}
