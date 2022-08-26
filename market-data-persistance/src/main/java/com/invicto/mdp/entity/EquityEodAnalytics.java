package com.invicto.mdp.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "EQUITY_EOD_ANALYTICS")
public class EquityEodAnalytics {

    @Id
    @GeneratedValue(generator = "equity-analytics-sequence-generator")
    @GenericGenerator(
            name = "equity-analytics-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "equity_analytics_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    Long id;

    @Column(name = "DELTA_VOL_P")
    private double deltaVolP;
    @Column(name = "DELTA_PRICE_P")
    private double deltaPriceP;
    @Column(name = "IS_VOL_UP_BY_5P")
    private boolean isVolUp5p = false;
    @Column(name = "IS_VOL_UP_BY_10P")
    private boolean isVolUp10p = false;
    @Column(name = "IS_BUY_CONTENDER")
    private boolean isBuyContender = false;
    @Column(name = "IS_STRONG_BUY_CONTENDER")
    private boolean isStrongBuyContender = false;
    @Column(name = "IS_SELL_CONTENDER")
    private boolean isSellContender = false;
    @Column(name = "IS_STRONG_SELL_CONTENDER")
    private boolean isStrongSellContender = false;
    @Column(name = "COLLECTION_DATE")
    private LocalDate collectionDate;

    @ManyToOne(targetEntity = Equity.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Equity equity;
}
