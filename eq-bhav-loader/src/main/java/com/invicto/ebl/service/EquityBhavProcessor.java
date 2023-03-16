package com.invicto.ebl.service;

import com.invicto.ebl.model.EqCsvRecord;
import com.invicto.mdp.entity.Equity;
import com.invicto.mdp.entity.EquityEodAnalytics;
import com.invicto.mdp.entity.EquityEodData;
import com.invicto.mdp.entity.Symbol;
import com.invicto.mdp.repository.EquityEodAnalyticsRepository;
import com.invicto.mdp.repository.EquityEodDataRepository;
import com.invicto.mdp.repository.EquityRepository;
import com.invicto.mdp.repository.SymbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.Optional;

@Service
public class EquityBhavProcessor {


    private DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("dd-MMM-yyyy")
            .toFormatter(Locale.ENGLISH);

    private EquityEodAnalyticsRepository equityDataAnalyticsRepo;
    private EquityRepository equityRepo;
    private EquityEodDataRepository dataRepo;

    private SymbolRepository symbolRepository;

    @Autowired
    public EquityBhavProcessor(EquityEodAnalyticsRepository equityDataAnalyticsRepo, EquityRepository equityRepo, EquityEodDataRepository dataRepo, SymbolRepository symbolRepository) {
        this.equityDataAnalyticsRepo = equityDataAnalyticsRepo;
        this.equityRepo = equityRepo;
        this.dataRepo = dataRepo;
        this.symbolRepository = symbolRepository;
    }


    private Symbol findAndSaveSymbol(String symbol) {
        Optional<Symbol> symbolOptional = symbolRepository.findByTickerAndIsFO(symbol,true);
        if (symbolOptional.isPresent()) {
            return symbolOptional.get();
        } else {
            Symbol newSymbol = new Symbol();
            newSymbol.setFO(false);
            newSymbol.setTicker(symbol);
            return symbolRepository.save(newSymbol);
        }
    }


    public void process(EqCsvRecord record) {
        Symbol symbol = findAndSaveSymbol(record.getSymbol());
        Optional<Equity> equity = equityRepo.findBySymbol(symbol);
        if (equity.isPresent()) {
            EquityEodData current = populateEquityData(record);
            current.setEquity(equity.get());
            dataRepo.save(current);
            Optional<EquityEodData> latest = dataRepo.findTop1ByEquityOrderByCollectionDateDesc(equity.get());
            EquityEodAnalytics dataAnalytics = populateEquityAnalyticsData(equity.get(), latest.get(), current);
            equityDataAnalyticsRepo.save(dataAnalytics);
        } else {
            Equity newEquity = new Equity();
            newEquity.setSymbol(symbol);
            equityRepo.save(newEquity);

            EquityEodData equityData = populateEquityData(record);
            equityData.setEquity(newEquity);
            dataRepo.save(equityData);
            EquityEodAnalytics analytics = populateEquityAnalyticsData(newEquity, null, equityData);
            equityDataAnalyticsRepo.save(analytics);
        }
    }

    private EquityEodData populateEquityData(EqCsvRecord record) {
        EquityEodData equityData = new EquityEodData();
        equityData.setClose(Double.parseDouble(record.getClose()));
        equityData.setLast(Double.parseDouble(record.getLast()));
        equityData.setLow(Double.parseDouble(record.getLow()));
        equityData.setOpen(Double.parseDouble(record.getOpen()));
        equityData.setHigh(Double.parseDouble(record.getHigh()));
        equityData.setCollectionDate(LocalDate.parse(record.getTimestamp(), formatter));
        equityData.setISIN(record.getISIN());
        equityData.setPrevClose(Double.parseDouble(record.getPrevClose()));
        equityData.setTotalTradedQty(Double.parseDouble(record.getTotalTradedQty()));
        equityData.setTotalTradedVol(Double.parseDouble(record.getTotalTradedVol()));
        equityData.setTotalTrades(Double.parseDouble(record.getTotalTrades()));
        return equityData;
    }

    private EquityEodAnalytics populateEquityAnalyticsData(Equity equity, EquityEodData latest, EquityEodData current) {

        EquityEodAnalytics equityDataAnalytics = new EquityEodAnalytics();
        equityDataAnalytics.setEquity(equity);
        equityDataAnalytics.setCollectionDate(current.getCollectionDate());
        if (latest != null) {
            System.out.println("using latest data at " + latest.getCollectionDate());
            double deltaPriceP = (100 * (current.getClose() - latest.getClose())) / latest.getClose();
            equityDataAnalytics.setDeltaPriceP(deltaPriceP);

            double deltaVolP = (100 * (current.getTotalTradedQty() - latest.getTotalTradedQty())) / latest.getTotalTradedQty();
            equityDataAnalytics.setDeltaVolP(deltaVolP);

            if (deltaVolP > 5.0)
                equityDataAnalytics.setVolUp5p(true);
            if (deltaVolP > 10.0)
                equityDataAnalytics.setVolUp10p(true);

            if (equityDataAnalytics.isVolUp5p() && equityDataAnalytics.getDeltaPriceP() > 0.0)
                equityDataAnalytics.setBuyContender(true);

            if (equityDataAnalytics.isVolUp5p() && equityDataAnalytics.getDeltaPriceP() > 0.0) {
                equityDataAnalytics.setStrongBuyContender(true);
                equityDataAnalytics.setBuyContender(true);
            }

            if (equityDataAnalytics.isVolUp5p() && equityDataAnalytics.getDeltaPriceP() < 0.0)
                equityDataAnalytics.setSellContender(true);

            if (equityDataAnalytics.isVolUp5p() && equityDataAnalytics.getDeltaPriceP() < 0.0) {
                equityDataAnalytics.setStrongSellContender(true);
                equityDataAnalytics.setSellContender(true);
            }
        } else {
            equityDataAnalytics.setDeltaPriceP(0.0);
            equityDataAnalytics.setDeltaVolP(0.0);
            equityDataAnalytics.setVolUp5p(false);
            equityDataAnalytics.setVolUp10p(false);
            equityDataAnalytics.setStrongBuyContender(false);
            equityDataAnalytics.setStrongBuyContender(false);
            equityDataAnalytics.setSellContender(false);
            equityDataAnalytics.setBuyContender(false);
        }
        return equityDataAnalytics;
    }
}
