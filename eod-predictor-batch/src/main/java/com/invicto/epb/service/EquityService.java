package com.invicto.epb.service;

import com.invicto.epb.model.EquityDataVo;
import com.invicto.mdp.entity.Equity;
import com.invicto.mdp.entity.EquityEodData;
import com.invicto.mdp.entity.Symbol;
import com.invicto.mdp.repository.EquityEodDataRepository;
import com.invicto.mdp.repository.EquityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class EquityService {

    private EquityRepository equityRepository;
    private EquityEodDataRepository equityEodDataRepository;

    @Autowired
    public EquityService(EquityRepository equityRepository, EquityEodDataRepository equityEodDataRepository) {
        this.equityRepository = equityRepository;
        this.equityEodDataRepository = equityEodDataRepository;
    }

    public EquityDataVo getPreviousDayUnderlyingValue(Symbol symbol) {
        Optional<Equity> equity = equityRepository.findBySymbol(symbol);
        if (equity.isPresent()) {
            Optional<EquityEodData> equityEodData = equityEodDataRepository.findTop1ByEquityOrderByCollectionDateDesc(equity.get());
            if (equityEodData.isPresent()) {
                EquityDataVo equityDataVo = new EquityDataVo();
                equityDataVo.setClose(equityEodData.get().getClose());
                equityDataVo.setVolume(equityEodData.get().getTotalTradedVol());
                return equityDataVo;
            }
        }
        throw new RuntimeException("Equity Data not found for " + symbol.getTicker());
    }
}
