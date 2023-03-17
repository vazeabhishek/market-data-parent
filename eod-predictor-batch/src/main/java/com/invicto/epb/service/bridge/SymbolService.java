package com.invicto.epb.service.bridge;

import com.invicto.mdp.entity.Symbol;
import com.invicto.mdp.entity.SymbolEodPrediction;
import com.invicto.mdp.entity.SymbolIntraday15mSnap;
import com.invicto.mdp.entity.SymbolIntraday1HSnap;
import com.invicto.mdp.repository.SymbolEodPredictionRepository;
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

    private final SymbolEodPredictionRepository symbolEodPredictionRepository;

    @Autowired
    public SymbolService(SymbolRepository symbolRepository, SymbolIntraday15mSnapRepository symbolIntraday15mSnapRepository,
                         SymbolIntraday1hSnapRepository symbolIntraday1hSnapRepository, SymbolEodPredictionRepository symbolEodPredictionRepository) {
        this.symbolRepository = symbolRepository;
        this.symbolIntraday15mSnapRepository = symbolIntraday15mSnapRepository;
        this.symbolIntraday1hSnapRepository = symbolIntraday1hSnapRepository;
        this.symbolEodPredictionRepository = symbolEodPredictionRepository;
    }

    public List<Symbol> findAllFOSymbols() {
        return symbolRepository.findAllByIsFOIsTrue();
    }

    public List<SymbolIntraday1HSnap> find1hIntradaySnapsBySymbol(Symbol symbol, LocalDate collectionDate) {
        return symbolIntraday1hSnapRepository.findBySymbolAndCollectionDateOrderByCollectionDateAscCollectionTimeAsc(symbol, collectionDate);
    }

    public List<SymbolIntraday15mSnap> find15mIntradaySnapsBySymbol(Symbol symbol, LocalDate collectionDate) {
        return symbolIntraday15mSnapRepository.findBySymbolAndCollectionDateOrderByCollectionDateAscCollectionTimeAsc(symbol, collectionDate);
    }

    public void saveEodPrediction(SymbolEodPrediction eodPrediction) {
        symbolEodPredictionRepository.save(eodPrediction);
    }
}
