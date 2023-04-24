package com.invicto.fbl.model;

import com.invicto.mdp.entity.Contract;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ContractEodAnalyticsVo {
    private Contract contract;
    private double deltaVolumePercentage;
    private double deltaOiPercentage;
    private double deltaClosePercentage;
    private double buyersWonCount ;
    private double sellersWonCount;
    private boolean higherHighCount  = false;
    private boolean higherLowCount = false;
    private boolean lowerLowCount = false;
    private boolean lowerHighCount = false;
    private double buyWickPercentage;
    private double sellWickPercentage;
    private String oiSignal;
    private String priceSignal;
    private String volSignal;
    private String signal;
    private LocalDate analyticsDate;
}
