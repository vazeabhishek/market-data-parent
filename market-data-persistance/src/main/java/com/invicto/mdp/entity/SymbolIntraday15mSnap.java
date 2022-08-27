package com.invicto.mdp.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

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
    @Column(name = "UNDERLYING_VALUE")
    private double underlyingValue;
    @Column(name = "TOTAL_OI")
    private double oi;
    @Column(name = "VOLUME")
    private double volume;
    @Column(name = "COLLECTION_DATE")
    private LocalDate collectionDate;
    @Column(name = "COLLECTION_TIME")
    private LocalTime collectionTime;
    @Column(name = "COUNTER")
    private int counter;
    @ManyToOne(targetEntity = Symbol.class, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Symbol symbol;
}
