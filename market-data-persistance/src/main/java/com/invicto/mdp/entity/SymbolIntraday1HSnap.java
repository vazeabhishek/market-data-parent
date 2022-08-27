package com.invicto.mdp.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "SYMBOL_INTRADAY_1H_SNAP")
@Getter
@Setter
public class SymbolIntraday1HSnap {
    @Id
    @Column(name = "SIS1H_RECORD_NO")
    @GeneratedValue(generator = "sis1h-sequence-generator")
    @GenericGenerator(
            name = "sis1h-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "sis1h_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private long recordNo;
    @Column(name = "UNDERLYING_VALUE")
    private double underlyingValue;
    @Column(name = "TOTAL_OI")
    private double openInterestLatest ;
    @Column(name = "VOLUME")
    private double volume;
    @Column(name = "COLLECTION_TIME", columnDefinition = "timestamp without time zone")
    private Timestamp collectionTime;
    @Column(name = "COUNTER")
    private int counter;
    @ManyToOne(targetEntity = Symbol.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Symbol symbol;
}
