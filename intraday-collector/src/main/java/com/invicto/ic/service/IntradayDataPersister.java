package com.invicto.ic.service;

import com.invicto.ic.model.EquitySnapVo;
import com.invicto.ic.model.EquityVo;
import com.invicto.ic.model.SnapType;
import com.invicto.mdp.entity.Symbol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class IntradayDataPersister {

    private final SymbolService symbolService;
    private final Symbolntraday15MSnapService symbolntraday15MSnapService;
    private final Symbolntraday1HSnapService symbolntraday1HSnapService;

    @Autowired
    public IntradayDataPersister(SymbolService symbolService, Symbolntraday15MSnapService symbolntraday15MSnapService, Symbolntraday1HSnapService symbolntraday1HSnapService) {
        this.symbolService = symbolService;
        this.symbolntraday15MSnapService = symbolntraday15MSnapService;
        this.symbolntraday1HSnapService = symbolntraday1HSnapService;
    }

    public void saveSnapshot(EquitySnapVo equitySnapVo) {
        for (EquityVo equityVo : equitySnapVo.getData()) {
            if (equityVo.getSymbol().contentEquals("-")) {
                continue;
            }
            Optional<Symbol> symbolOptional = symbolService.findSymbolByTicker(equityVo.getSymbol());
            if(symbolOptional.isEmpty())
                symbolService.saveTicker(equityVo.getSymbol());
            if (equitySnapVo.getType() == SnapType.H1) {
                save1hSnap(equityVo);
            }
            if (equitySnapVo.getType() == SnapType.M15) {
                save15mSnap(equityVo);
            }
        }
    }

    public void save15mSnap(EquityVo equityVo) {
        Optional<Symbol> symbolOptional = symbolService.findSymbolByTicker(equityVo.getSymbol());
        if (symbolOptional.isPresent())
            symbolntraday15MSnapService.save(symbolOptional.get(), equityVo);
    }

    public void save1hSnap(EquityVo equityVo) {
        Optional<Symbol> symbolOptional = symbolService.findSymbolByTicker(equityVo.getSymbol());
        if (symbolOptional.isPresent())
            symbolntraday1HSnapService.save(symbolOptional.get(), equityVo);
    }
}
