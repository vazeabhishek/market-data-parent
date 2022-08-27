package com.invicto.ic.service;

import com.invicto.mdp.entity.Symbol;
import com.invicto.mdp.repository.SymbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SymbolService {

    @Autowired
    private SymbolRepository symbolRepository;


    public Symbol saveTicker(String symbol){
        Optional<Symbol> symbolOptional = findSymbolByTicker(symbol);
        if(symbolOptional.isPresent())
            return symbolOptional.get();
        else
        {
            Symbol newSymbol = new Symbol();
            newSymbol.setTicker(symbol);
            newSymbol = symbolRepository.save(newSymbol);
            return newSymbol;
        }
    }

    public Optional<Symbol> findSymbolByTicker(String symbol){
        return symbolRepository.findByTicker(symbol);
    }
}
