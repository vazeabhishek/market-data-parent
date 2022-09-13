package com.invicto.mdp.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "SYMBOL")
public class Symbol {
    @Id
    @GeneratedValue(generator = "symbol-sequence-generator")
    @GenericGenerator(
            name = "symbol-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "symbol_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "TICKER")
    private String ticker;
    @Column(name = "isFO")
    private boolean isFO;
    @OneToMany(mappedBy = "symbol",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Contract> contractList;
    @OneToMany(mappedBy = "symbol",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Equity> equitiyList;
    @OneToMany(mappedBy = "symbol",cascade = CascadeType.REFRESH,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<SymbolIntraday1HSnap> intraday1hSnaps;
    @OneToMany(mappedBy = "symbol",cascade = CascadeType.REFRESH,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<SymbolIntraday15mSnap> intraday15mSnaps;
    @OneToMany(mappedBy = "symbol",cascade = CascadeType.REFRESH,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Notification> notifications;
}
