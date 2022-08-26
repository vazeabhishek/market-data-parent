package com.invicto.mdp.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "SYMBOL_INTRADAY_15M_SNAP")
@Getter
@Setter
public class SymbolIntraday15mSnap {
    @Id
    @Column(name = "SIS15M_RECORD_NO")
    @GeneratedValue(generator = "cis15m-sequence-generator")
    @GenericGenerator(
            name = "cis15m-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "cis15m_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private long recordNo;
    @Column(name = "NEAR_FUT_VAL")
    private double nearFutValue;
    @Column(name = "FAR_FUT_VAL")
    private double farFutValue;
    @Column(name = "FAR_NXT_FUT_VAL")
    private double farNxtFutValue;
    @Column(name = "UNDERLYING_VALUE")
    private double underlyingValue;
    @Column(name = "LATEST_OI")
    private double openInterestLatest;
    @Column(name = "PREV_OI")
    private double openInterestPrevDay;
    @Column(name = "VOLUME")
    private double volume;
    @Column(name = "P_DELTA_LTP_WRT_PREV")
    private double percentChangeInLtpWrtPrev;
    @Column(name = "OI_CHG_WRT_PREV")
    private double percentChangeInOiWrtPrev;
    @Column(name = "COLLECTION_TIME", columnDefinition = "timestamp without time zone")
    private Timestamp collectionTime;
    @Column(name = "TREND_INDICATOR")
    private long trendIndicator;
    @Column(name = "TREND_STRENGTH")
    private long trendStrength;
    @Column(name = "CUMULATIVE_MARKET_TREND")
    private double cumulativeTrend;
    @Column(name = "COUNTER")
    private int counter;
    @ManyToOne(targetEntity = Symbol.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Symbol symbol;
}
