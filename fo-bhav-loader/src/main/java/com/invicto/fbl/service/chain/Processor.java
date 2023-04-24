package com.invicto.fbl.service.chain;

import com.invicto.fbl.model.ContractEodAnalyticsVo;
import com.invicto.mdp.entity.ContractEodData;

public abstract class Processor {
    public Processor next;
    public final void execute(ContractEodData latest, ContractEodData prev,ContractEodAnalyticsVo contractEodAnalyticsVo){
        process(latest,prev,contractEodAnalyticsVo);
        if(next != null)
            next.execute(latest,prev,contractEodAnalyticsVo);
    }
    public abstract void process(ContractEodData latest, ContractEodData prev,ContractEodAnalyticsVo eodAnalyticsVo);
}
