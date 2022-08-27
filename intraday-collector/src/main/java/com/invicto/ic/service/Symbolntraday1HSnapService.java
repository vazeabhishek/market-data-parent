package com.invicto.ic.service;

import com.invicto.ic.model.EquityVo;
import com.invicto.mdp.entity.Symbol;
import com.invicto.mdp.entity.SymbolIntraday1HSnap;
import com.invicto.mdp.repository.SymbolIntraday1hSnapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class Symbolntraday1HSnapService {


    private final SymbolIntraday1hSnapRepository repository;

    @Autowired
    public Symbolntraday1HSnapService(SymbolIntraday1hSnapRepository repository) {
        this.repository = repository;
    }

    public void save(Symbol symbol,EquityVo equityVo){
        SymbolIntraday1HSnap symbolIntraday1HSnap = new SymbolIntraday1HSnap();
        symbolIntraday1HSnap.setSymbol(symbol);
        symbolIntraday1HSnap.setCollectionDate(LocalDate.now());
        symbolIntraday1HSnap.setCollectionTime(LocalTime.now());
        symbolIntraday1HSnap.setOi(equityVo.getLatestOI());
        symbolIntraday1HSnap.setVolume(equityVo.getVolume());
        symbolIntraday1HSnap.setUnderlyingValue(equityVo.getUnderlyingValue());
        repository.save(symbolIntraday1HSnap);
    }
}
