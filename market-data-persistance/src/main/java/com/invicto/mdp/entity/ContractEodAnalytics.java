package com.invicto.mdp.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "CONTRACT_EOD_ANALYTICS")
public class ContractEodAnalytics {

    @Id
    @GeneratedValue(generator = "contract-analytics-sequence-generator")
    @GenericGenerator(
            name = "contract-analytics-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "contract_analytics_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    @Column(name = "DELTA_VOL_P")
    private double deltaVolumeP;
    @Column(name = "DELTA_OI_P")
    private double deltaOiP;
    @Column(name = "DELTA_CLOSE_P")
    private double deltaCloseP;
    @Column(name = "BUYERS_WON_COUNT")
    private double buyersWonCount = 0;
    @Column(name = "SELLERS_WON_COUNT")
    private double sellersWonCount = 0;
    @Column(name = "HIGHER_HIGH_COUNT")
    private double higherHighCount = 0;
    @Column(name = "HIGHER_LOW_COUNT")
    private double higherLowCount = 0;
    @Column(name = "LOWER_LOW_COUNT")
    private double lowerLowCount = 0;
    @Column(name = "LOWER_HIGH_COUNT")
    private double lowerHighCount = 0;
    @Column(name = "BUY_WICK_PERCENTAGE")
    private double buyWickP = 0;
    @Column(name = "SELL_WICK_PERCENTAGE")
    private double sellWickP = 0;
    @Column(name = "SIGNAL")
    private String signal;
    @Column(name = "ANALYTICS_DATE")
    private LocalDate analyticsDate;
    @Column(name = "VIOLATION_REASON")
    private String violationReason;
    @ManyToOne(targetEntity = Contract.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Contract contract;
}
