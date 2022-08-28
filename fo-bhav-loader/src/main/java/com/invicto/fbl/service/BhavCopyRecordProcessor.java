package com.invicto.fbl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invicto.fbl.model.EquityDerivativeCsvRecord;
import com.invicto.mdp.entity.Contract;
import com.invicto.mdp.entity.ContractEodAnalytics;
import com.invicto.mdp.entity.ContractEodData;
import com.invicto.mdp.entity.Symbol;
import com.invicto.mdp.repository.ContractEodAnalyticsRepository;
import com.invicto.mdp.repository.ContractEodDataRepository;
import com.invicto.mdp.repository.ContractRepository;
import com.invicto.mdp.repository.SymbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Component
public class BhavCopyRecordProcessor {

    private final ContractRepository contractRepository;
    private final ContractEodDataRepository contractEodDataRepository;
    private final ContractEodAnalyticsRepository contractEodAnalyticsRepository;
    private final SymbolRepository symbolRepository;

    @Autowired
    public BhavCopyRecordProcessor(ContractRepository contractRepository, ContractEodDataRepository contractEodDataRepository,
                                   ContractEodAnalyticsRepository contractEodAnalyticsRepository, SymbolRepository symbolRepository) {
        this.contractRepository = contractRepository;
        this.contractEodDataRepository = contractEodDataRepository;
        this.contractEodAnalyticsRepository = contractEodAnalyticsRepository;
        this.symbolRepository = symbolRepository;
    }

    public void process(EquityDerivativeCsvRecord record) {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Processing " + record.getSymbol());
        try {
            Symbol symbol = findAndSaveSymbol(record.getSymbol());
            Contract contract = findAndSaveContract(symbol, record.getExpiryDt(), record.getInstrument());
            ContractEodData old = contractEodDataRepository.findTop1ByContractOrderByCollectionDateDesc(contract);
            ContractEodData latest = saveContractEod(contract, record);
            saveContractEodAnalytics(contract, latest, old);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    private Symbol findAndSaveSymbol(String symbol) {
        Optional<Symbol> symbolOptional = symbolRepository.findByTicker(symbol);
        if (symbolOptional.isPresent()) {
            Symbol existSymbol = symbolOptional.get();
            if (existSymbol.isFO() == false) {
                existSymbol.setFO(true);
                existSymbol = symbolRepository.save(existSymbol);
            }
            return existSymbol;
        } else {
            Symbol symbolEntity = new Symbol();
            symbolEntity.setFO(true);
            symbolEntity.setTicker(symbol);
            symbolEntity = symbolRepository.save(symbolEntity);
            return symbolEntity;
        }
    }

    private Contract findAndSaveContract(Symbol symbol, LocalDate expiry, String instrument) {
        Optional<Contract> contractOptional = contractRepository.findBySymbolAndExpiryDate(symbol, expiry);
        if (contractOptional.isPresent())
            return contractOptional.get();
        else {
            Contract contract = new Contract();
            contract.setExpiryDate(expiry);
            contract.setSymbol(symbol);
            contract.setInstrument(instrument);
            return contractRepository.save(contract);
        }
    }

    private ContractEodData saveContractEod(Contract contract, EquityDerivativeCsvRecord record) {
        ContractEodData contractEodData = new ContractEodData();
        contractEodData.setContract(contract);
        contractEodData.setClose(record.getClose());
        contractEodData.setHigh(record.getHigh());
        contractEodData.setLow(record.getLow());
        contractEodData.setOpen(record.getOpen());
        contractEodData.setOpenInterest(record.getOi());
        contractEodData.setVolume(record.getContracts());
        contractEodData.setCollectionDate(record.getTimestamp());
        return contractEodDataRepository.save(contractEodData);
    }

    private void saveContractEodAnalytics(Contract contract,
                                          ContractEodData latest, ContractEodData old) {

        if (Objects.nonNull(old)) {
            ContractEodAnalytics contractEodAnalytics = new ContractEodAnalytics();
            contractEodAnalytics.setContract(contract);
            contractEodAnalytics.setDeltaCloseP(getDeltaPercentage(latest.getClose(), old.getClose()));
            contractEodAnalytics.setDeltaVolumeP(getDeltaPercentage(latest.getVolume(), old.getVolume()));
            contractEodAnalytics.setDeltaOiP(getDeltaPercentage(latest.getOpenInterest(), old.getOpenInterest()));
            contractEodAnalytics.setBuyWickP(calculateBuyWickPercentage(latest.getHigh(), latest.getLow(), latest.getOpen(), latest.getClose()));
            contractEodAnalytics.setSellWickP(calculateSellWickPercentage(latest.getHigh(), latest.getLow(), latest.getOpen(), latest.getClose()));
            if (contractEodAnalytics.getDeltaVolumeP() > 10.0 && contractEodAnalytics.getDeltaOiP() > 0.0
                    && contractEodAnalytics.getDeltaCloseP() > 0.0 && latest.getHigh() > old.getHigh()
                    && contractEodAnalytics.getSellWickP() < 30)

                contractEodAnalytics.setSignal("LONG_BUILD_UP");

            if (contractEodAnalytics.getDeltaVolumeP() > 10.0 && contractEodAnalytics.getDeltaOiP() < 0.0 && contractEodAnalytics.getDeltaCloseP() < 0.0 && latest.getLow() < old.getLow() && contractEodAnalytics.getBuyWickP() < 30)
                contractEodAnalytics.setSignal("SHORT_BUILD_UP");

            Optional<ContractEodAnalytics> optionalContractDataAnalytics = contractEodAnalyticsRepository.findTop1ByContractOrderByAnalyticsDateDesc(contract);
            if (optionalContractDataAnalytics.isPresent()) {
                ContractEodAnalytics latestAnalytics = optionalContractDataAnalytics.get();
                if (latest.getHigh() > old.getHigh())
                    contractEodAnalytics.setHigherHighCount(latestAnalytics.getHigherHighCount() + 1);
                if (latest.getLow() < old.getLow())
                    contractEodAnalytics.setLowerLowCount(latestAnalytics.getLowerLowCount() + 1);
                if (latest.getHigh() < old.getHigh())
                    contractEodAnalytics.setLowerHighCount(latestAnalytics.getLowerHighCount() + 1);
                if (latest.getLow() > old.getLow())
                    contractEodAnalytics.setHigherLowCount(latestAnalytics.getHigherLowCount() + 1);
                if (latest.getClose() > old.getClose() && latest.getHigh() > old.getHigh() && latest.getVolume() > old.getVolume() && latest.getLow() > old.getLow())
                    contractEodAnalytics.setBuyersWonCount(latestAnalytics.getBuyersWonCount() + 1);
                else
                    contractEodAnalytics.setBuyersWonCount(latestAnalytics.getBuyersWonCount());
                if (latest.getClose() < old.getClose() && latest.getLow() < old.getLow() && latest.getVolume() > old.getVolume() && latest.getHigh() < old.getHigh())
                    contractEodAnalytics.setSellersWonCount(latestAnalytics.getSellersWonCount() + 1);
                else
                    contractEodAnalytics.setSellersWonCount(latestAnalytics.getSellersWonCount());
            }
            contractEodAnalytics.setAnalyticsDate(latest.getCollectionDate());
            contractEodAnalytics.setContract(contract);
            contractEodAnalyticsRepository.save(contractEodAnalytics);
        }
    }

    private double getDeltaPercentage(double num, double deno) {
        return 100 * ((num - deno) / deno);
    }

    private double calculateSellWickPercentage(double high, double low, double open, double close) {
        double wickSize = 0.0;
        double candleSize = 0.0;
        if (close > open) {
            wickSize = high - close;
            candleSize = close - open;
        }
        if (open > close) {
            wickSize = high - open;
            candleSize = open - close;
        }
        if (wickSize > 0.0 && candleSize > 0.0)
            return (wickSize / candleSize) * 100;
        else
            return 0.0;

    }

    private void populateDefault(ContractEodData eodData) {
        eodData = new ContractEodData();
        eodData.setCollectionDate(LocalDate.now().minusDays(1));
        eodData.setVolume(1);
        eodData.setOpenInterest(1);
        eodData.setOpen(1);
        eodData.setLow(1);
        eodData.setClose(1);
        eodData.setHigh(1);
    }

    private double calculateBuyWickPercentage(double high, double low, double open, double close) {
        double wickSize = 0.0;
        double candleSize = 0.0;
        if (close > open) {
            wickSize = open - low;
            candleSize = close - open;
        }
        if (open > close) {
            wickSize = close - low;
            candleSize = open - close;
        }
        if (wickSize > 0.0 && candleSize > 0.0)
            return (wickSize / candleSize) * 100;
        else
            return 0.0;

    }
}
