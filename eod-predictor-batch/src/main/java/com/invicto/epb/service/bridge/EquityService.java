package com.invicto.epb.service.bridge;

import com.invicto.epb.model.vo.EquityDataVo;
import com.invicto.mdp.entity.Equity;
import com.invicto.mdp.entity.EquityEodData;
import com.invicto.mdp.entity.Symbol;
import com.invicto.mdp.repository.EquityEodDataRepository;
import com.invicto.mdp.repository.EquityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
                equityDataVo.setLow(equityEodData.get().getLow());
                equityDataVo.setHigh(equityEodData.get().getHigh());
                return equityDataVo;
            }
        }
        throw new RuntimeException("Equity Data not found for " + symbol.getTicker());
    }

    public double getLatestHigh(Symbol symbol, double currentPrice, double threshold, LocalDate fromDate) {
        Optional<Equity> equityOptional = equityRepository.findBySymbol(symbol);
        double desiredPrice = currentPrice + ((threshold / 100) * currentPrice);
        if (equityOptional.isPresent()) {
            Optional<EquityEodData> equityEodDataOptional = equityEodDataRepository.findFirstByEquityAndHighGreaterThanAndCollectionDateBeforeOrderByCollectionDateDesc(equityOptional.get(), currentPrice, fromDate);
            if (equityEodDataOptional.isPresent()) {
                if (equityEodDataOptional.get().getHigh() > desiredPrice)
                    return equityEodDataOptional.get().getHigh();
            }

        }
        return desiredPrice;
    }

    public double getLatestLow(Symbol symbol, double currentPrice, double threshold, LocalDate fromDate) {
        Optional<Equity> equityOptional = equityRepository.findBySymbol(symbol);
        double desiredPrice = currentPrice - ((threshold / 100) * currentPrice);
        if (equityOptional.isPresent()) {
            Optional<EquityEodData> equityEodDataOptional = equityEodDataRepository.findFirstByEquityAndLowLessThanAndCollectionDateBeforeOrderByCollectionDateDesc(equityOptional.get(), currentPrice, fromDate);
            if (equityEodDataOptional.isPresent()) {
                if (equityEodDataOptional.get().getLow() < desiredPrice)
                    return equityEodDataOptional.get().getLow();
            }

        }
        return desiredPrice;
    }
}
