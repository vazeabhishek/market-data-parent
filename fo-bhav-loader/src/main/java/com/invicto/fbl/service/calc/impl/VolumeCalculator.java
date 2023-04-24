package com.invicto.fbl.service.calc.impl;

import com.invicto.fbl.helper.MathsHelper;
import com.invicto.fbl.service.calc.Calculator;
import com.invicto.mdp.entity.ContractEodData;

public class VolumeCalculator implements Calculator {

    private MathsHelper mathsHelper;

    public VolumeCalculator(MathsHelper mathsHelper) {
        this.mathsHelper = mathsHelper;
    }

    @Override
    public double calculate(ContractEodData latest, ContractEodData previous) {
        if(null == previous)
            return 0.0;
        double deltaVolume = mathsHelper.calculateDelta(latest.getVolume(), previous.getVolume());
        return mathsHelper.calculatePercentage(deltaVolume, previous.getVolume());
    }
}
