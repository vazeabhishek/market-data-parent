package com.invicto.mdp.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "EQUITY")
public class Equity {

    @Id
    @GeneratedValue(generator = "equity-sequence-generator")
    @GenericGenerator(
            name = "equity-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "equity_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "id", nullable = false)
    Long id;

    @ManyToOne(targetEntity = Symbol.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Symbol symbol;

    @OneToMany(mappedBy = "equity",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<EquityEodData> equityEodData;
    @OneToMany(mappedBy = "equity",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<EquityEodAnalytics> equityEodAnalytics;
}
