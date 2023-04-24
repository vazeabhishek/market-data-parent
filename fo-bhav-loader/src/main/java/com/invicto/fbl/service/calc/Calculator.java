package com.invicto.fbl.service.calc;

import com.invicto.fbl.model.ContractEodAnalyticsVo;
import com.invicto.mdp.entity.ContractEodData;

public interface Calculator {
    double calculate(ContractEodData latest, ContractEodData previous);
}
