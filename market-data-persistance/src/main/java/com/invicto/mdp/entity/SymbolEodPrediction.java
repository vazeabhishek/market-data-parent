package com.invicto.mdp.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

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

    @Column(name = "SYMBOL_ID")
    private long symbolId;

    @Column(name = "PREDICTION_DATE")
    private LocalDate predictionDate;

    @Column(name = "PREDICTED_PRICE")
    private double predictedPrice;

    @Column(name = "PREDICTTED_DELTA_VOL")
    private double predictedDeltaVolume;

    @Column(name = "PREDICTED_DELTA_OI")
    private double predictedDeltaOI;

    @Column(name = "SIGNAL")
    private String signal;

    @Column(name = "VIOLATION_TYPE")
    private String violationType;

    @Column(name = "VIOLATION_TEXT")
    private String violationText;

    @Column(name = "TARGET")
    private double target;

    @Column(name = "STOP_LOSS")
    private double stopLoss;

}
