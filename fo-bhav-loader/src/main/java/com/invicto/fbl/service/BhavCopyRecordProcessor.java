package com.invicto.fbl.service;

import com.invicto.fbl.exception.RuleViolationException;
import com.invicto.fbl.model.ContractEodAnalyticsVo;
import com.invicto.fbl.model.EquityDerivativeCsvRecord;
import com.invicto.fbl.model.SignalEnum;
import com.invicto.fbl.service.chain.Processor;
import com.invicto.mdp.entity.Contract;
import com.invicto.mdp.entity.ContractEodAnalytics;
import com.invicto.mdp.entity.ContractEodData;
import com.invicto.mdp.entity.Symbol;
import com.invicto.mdp.repository.ContractEodAnalyticsRepository;
import com.invicto.mdp.repository.ContractEodDataRepository;
import com.invicto.mdp.repository.ContractRepository;
import com.invicto.mdp.repository.SymbolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@Slf4j
public class BhavCopyRecordProcessor {

    private final ContractRepository contractRepository;
    private final ContractEodDataRepository contractEodDataRepository;
    private final ContractEodAnalyticsRepository contractEodAnalyticsRepository;
    private final SymbolRepository symbolRepository;
    private final Processor start;

    @Autowired
    public BhavCopyRecordProcessor(ContractRepository contractRepository, ContractEodDataRepository contractEodDataRepository,
                                   ContractEodAnalyticsRepository contractEodAnalyticsRepository, SymbolRepository symbolRepository, @Qualifier("oiProcessor") Processor start) {
        this.contractRepository = contractRepository;
        this.contractEodDataRepository = contractEodDataRepository;
        this.contractEodAnalyticsRepository = contractEodAnalyticsRepository;
        this.symbolRepository = symbolRepository;
        this.start = start;
    }

    public void process(EquityDerivativeCsvRecord record) {
        log.info("Processing {} for date {}", record.getSymbol(), record.getTimestamp());
        try {
            Symbol symbol = findAndSaveSymbol(record.getSymbol());
            Contract contract = findAndSaveContract(symbol, record.getExpiryDt(), record.getInstrument());
            ContractEodData prev = contractEodDataRepository.findTop1ByContractOrderByCollectionDateDesc(contract);
            ContractEodData latest = saveContractEod(contract, record);
            ContractEodAnalyticsVo contractEodAnalyticsVo = createAnalyticsVo(latest);
            if (prev != null) {
                log.info("Using data for {}", prev.getCollectionDate());
                try {
                    start.execute(latest, prev, contractEodAnalyticsVo);
                } catch (RuleViolationException ex) {
                    log.info(ex.getMessage());
                }
                saveContractEodAnalytics(contractEodAnalyticsVo);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }


    /* Finds if symbol exists and updates if changed */
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

    private void saveContractEodAnalytics(ContractEodAnalyticsVo contractEodAnalyticsVo) {
        ContractEodAnalytics contractEodAnalytics = new ContractEodAnalytics();
        contractEodAnalytics.setContract(contractEodAnalyticsVo.getContract());
        contractEodAnalytics.setAnalyticsDate(contractEodAnalyticsVo.getAnalyticsDate());
        contractEodAnalytics.setSignal(contractEodAnalyticsVo.getSignal());
        contractEodAnalytics.setBuyWickP(contractEodAnalyticsVo.getBuyWickPercentage());
        contractEodAnalytics.setSellWickP(contractEodAnalyticsVo.getSellWickPercentage());
        contractEodAnalytics.setDeltaCloseP(contractEodAnalyticsVo.getDeltaClosePercentage());
        contractEodAnalytics.setDeltaOiP(contractEodAnalyticsVo.getDeltaOiPercentage());
        contractEodAnalytics.setDeltaVolumeP(contractEodAnalyticsVo.getDeltaVolumePercentage());
        contractEodAnalytics.setViolationReason(contractEodAnalyticsVo.getReason());
        contractEodAnalyticsRepository.save(contractEodAnalytics);
    }

    private ContractEodAnalyticsVo createAnalyticsVo(ContractEodData latest) {
        ContractEodAnalyticsVo contractEodAnalyticsVo = new ContractEodAnalyticsVo();
        contractEodAnalyticsVo.setContract(latest.getContract());
        contractEodAnalyticsVo.setSignal(SignalEnum.NEUTRAL.name());
        contractEodAnalyticsVo.setOiSignal(SignalEnum.NEUTRAL.name());
        contractEodAnalyticsVo.setLowerHighCount(false);
        contractEodAnalyticsVo.setHigherLowCount(false);
        contractEodAnalyticsVo.setLowerLowCount(false);
        contractEodAnalyticsVo.setAnalyticsDate(latest.getCollectionDate());
        contractEodAnalyticsVo.setPriceSignal(SignalEnum.NEUTRAL.name());
        contractEodAnalyticsVo.setDeltaOiPercentage(0.0);
        contractEodAnalyticsVo.setSellWickPercentage(0.0);
        contractEodAnalyticsVo.setBuyWickPercentage(0.0);
        contractEodAnalyticsVo.setDeltaClosePercentage(0.0);
        contractEodAnalyticsVo.setDeltaVolumePercentage(0.0);
        return contractEodAnalyticsVo;
    }
}
