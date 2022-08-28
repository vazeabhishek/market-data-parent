package com.invicto.epb.service;

import com.invicto.mdp.entity.Symbol;
import com.invicto.mdp.entity.SymbolIntraday15mSnap;
import com.invicto.mdp.entity.SymbolIntraday1HSnap;
import com.invicto.mdp.repository.SymbolIntraday15mSnapRepository;
import com.invicto.mdp.repository.SymbolIntraday1hSnapRepository;
import com.invicto.mdp.repository.SymbolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class SymbolService {

    private final SymbolRepository symbolRepository;
    private final SymbolIntraday15mSnapRepository symbolIntraday15mSnapRepository;
    private final SymbolIntraday1hSnapRepository symbolIntraday1hSnapRepository;

    @Autowired
    public SymbolService(SymbolRepository symbolRepository, SymbolIntraday15mSnapRepository symbolIntraday15mSnapRepository, SymbolIntraday1hSnapRepository symbolIntraday1hSnapRepository) {
        this.symbolRepository = symbolRepository;
        this.symbolIntraday15mSnapRepository = symbolIntraday15mSnapRepository;
        this.symbolIntraday1hSnapRepository = symbolIntraday1hSnapRepository;
    }

    List<Symbol> findAllFOSymbols(){
        return symbolRepository.findAllByIsFOIsTrue();
    }

    List<SymbolIntraday1HSnap> find1hIntradaySnapsBySymbol(Symbol symbol){
        return symbolIntraday1hSnapRepository.findBySymbolAndCollectionDate(symbol, LocalDate.now());
    }

    List<SymbolIntraday15mSnap> find15mIntradaySnapsBySymbol(Symbol symbol){
        return symbolIntraday15mSnapRepository.findBySymbolAndCollectionDate(symbol, LocalDate.now());
    }
}
