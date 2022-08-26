package com.invicto.mdp.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "EQUITY_EOD_DATA")
public class EquityEodData {

    @Id
    @GeneratedValue(generator = "equity-data-sequence-generator")
    @GenericGenerator(
            name = "equity-data-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "equity_data_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    Long id;

    @Column(name = "OPEN")
    private double open;
    @Column(name = "HIGH")
    private double high;
    @Column(name = "LOW")
    private double low;
    @Column(name = "CLOSE")
    private double close;
    @Column(name = "LAST")
    private double last;
    @Column(name = "PREV_CLOSE")
    private double prevClose;
    @Column(name = "TOTAL_TRADED_QTY")
    private double totalTradedQty;
    @Column(name = "TOTAL_TRADED_VOL")
    private double totalTradedVol;
    @Column(name = "COLLECTION_DATE")
    private LocalDate collectionDate;
    @Column(name = "TOTAL_TRADES")
    private double totalTrades;
    @Column(name = "ISIN")
    private String ISIN;

    @ManyToOne(targetEntity = Equity.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Equity equity;
}
