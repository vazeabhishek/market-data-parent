package com.invicto.ic.service;

import com.invicto.ic.model.EquityVo;
import com.invicto.mdp.entity.Symbol;
import com.invicto.mdp.entity.SymbolIntraday1HSnap;
import com.invicto.mdp.repository.SymbolIntraday1hSnapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

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
        symbolIntraday1HSnap.setCollectionTime(Timestamp.valueOf(LocalDateTime.now()));
        symbolIntraday1HSnap.setVolume(equityVo.getVolume());
        symbolIntraday1HSnap.setUnderlyingValue(equityVo.getUnderlyingValue());

        List<SymbolIntraday1HSnap> intraday1HSnaps = symbol.getIntraday1hSnaps();
        if(intraday1HSnaps == null)
            intraday1HSnaps = new LinkedList<>();
        intraday1HSnaps.add(symbolIntraday1HSnap);
        repository.save(symbolIntraday1HSnap);
    }

}
