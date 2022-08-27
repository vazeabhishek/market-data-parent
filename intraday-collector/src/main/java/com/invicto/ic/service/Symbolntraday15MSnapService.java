package com.invicto.ic.service;

import com.invicto.ic.model.EquityVo;
import com.invicto.mdp.entity.Symbol;
import com.invicto.mdp.entity.SymbolIntraday15mSnap;
import com.invicto.mdp.entity.SymbolIntraday1HSnap;
import com.invicto.mdp.repository.SymbolIntraday15mSnapRepository;
import com.invicto.mdp.repository.SymbolIntraday1hSnapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class Symbolntraday15MSnapService {

    private final SymbolIntraday15mSnapRepository repository;

    @Autowired
    public Symbolntraday15MSnapService(SymbolIntraday15mSnapRepository repository) {
        this.repository = repository;
    }

    public void save(Symbol symbol,EquityVo equityVo){
        SymbolIntraday15mSnap symbolIntraday15mSnap = new SymbolIntraday15mSnap();
        symbolIntraday15mSnap.setSymbol(symbol);
        symbolIntraday15mSnap.setCollectionTime(Timestamp.valueOf(LocalDateTime.now()));
        symbolIntraday15mSnap.setVolume(equityVo.getVolume());
        symbolIntraday15mSnap.setUnderlyingValue(equityVo.getUnderlyingValue());
        repository.save(symbolIntraday15mSnap);
    }
}
