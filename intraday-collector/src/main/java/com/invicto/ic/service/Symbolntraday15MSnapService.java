package com.invicto.ic.service;

import com.invicto.ic.model.EquityVo;
import com.invicto.mdp.entity.Symbol;
import com.invicto.mdp.entity.SymbolIntraday15mSnap;
import com.invicto.mdp.repository.SymbolIntraday15mSnapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class Symbolntraday15MSnapService {

    private final SymbolIntraday15mSnapRepository repository;

    @Autowired
    public Symbolntraday15MSnapService(SymbolIntraday15mSnapRepository repository) {
        this.repository = repository;
    }

    public void save(Symbol symbol, EquityVo equityVo, LocalDate snapDate, LocalTime snapTime) {
        SymbolIntraday15mSnap symbolIntraday15mSnap = new SymbolIntraday15mSnap();
        symbolIntraday15mSnap.setSymbol(symbol);
        symbolIntraday15mSnap.setCollectionDate(snapDate);
        symbolIntraday15mSnap.setCollectionTime(snapTime);
        symbolIntraday15mSnap.setOi(equityVo.getLatestOI());
        symbolIntraday15mSnap.setPrevOi(equityVo.getPrevOI());
        symbolIntraday15mSnap.setChgInOi((equityVo.getChangeInOI() / (double) equityVo.getPrevOI()) * 100);
        symbolIntraday15mSnap.setVolume(equityVo.getVolume());
        symbolIntraday15mSnap.setUnderlyingValue(equityVo.getUnderlyingValue());
        repository.save(symbolIntraday15mSnap);
    }
}
