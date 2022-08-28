package com.invicto.mdp.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "SYMBOL_EOD_PREDICTION")
@Getter
@Setter
public class SymbolEodPrediction {
    @Id
    @Column(name = "SYM_EOD_RECORD_NO")
    @GeneratedValue(generator = "sym-eod-sequence-generator")
    @GenericGenerator(
            name = "sym-eod-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "sym_eod_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private long recordNo;

    @Column(name = "UNDERLYING_VALUE")
    private double underlyingValue;

    @Column(name = "VOLUME")
    private double volume;

    @Column(name = "P_DELTA_VOL")
    private double pDeltaVolume;

    @Column(name = "P_DELTA_OI")
    private double pDeltaOi;

    @Column(name = "P_DELTA_val")
    private double pDeltaVal;

    @Column
    private String signal;
}
