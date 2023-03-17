package com.invicto.epb.service.bridge;

import com.invicto.mdp.entity.Contract;
import com.invicto.mdp.entity.ContractEodAnalytics;
import com.invicto.mdp.entity.ContractEodData;
import com.invicto.mdp.entity.Symbol;
import com.invicto.mdp.repository.ContractEodAnalyticsRepository;
import com.invicto.mdp.repository.ContractEodDataRepository;
import com.invicto.mdp.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.THURSDAY;
import static java.time.temporal.TemporalAdjusters.lastInMonth;

@Service
public class ContractService {
    private final ContractRepository contractRepository;
    private final ContractEodAnalyticsRepository contractEodAnalyticsRepository;
    private final ContractEodDataRepository contractEodDataRepository;
    private final SymbolService symbolService;
    private final EquityService equityService;

    @Autowired
    public ContractService(ContractRepository contractRepository, ContractEodAnalyticsRepository contractEodAnalyticsRepository,
                           SymbolService symbolService, ContractEodDataRepository contractEodDataRepository,
                           EquityService equityService) {
        this.contractRepository = contractRepository;
        this.contractEodAnalyticsRepository = contractEodAnalyticsRepository;
        this.symbolService = symbolService;
        this.contractEodDataRepository = contractEodDataRepository;
        this.equityService = equityService;
    }

    public Optional<String> fetchLongBuildUps(Symbol symbol, long days) {
        Optional<Contract> optionalContract = contractRepository.findBySymbolAndExpiryDate(symbol, getCurrentMonthlyExpiry());
        if (optionalContract.isPresent()) {
            List<ContractEodAnalytics> list = contractEodAnalyticsRepository.findTop50ByContractOrderByAnalyticsDateDesc(optionalContract.get());
            return Optional.of(list.stream().limit(days).filter(e -> (Objects.nonNull(e.getSignal()) && e.getSignal().contains("LONG"))).map(o -> o.getAnalyticsDate().format(DateTimeFormatter.ISO_DATE)).collect(Collectors.joining(",")));
        }
        return Optional.empty();
    }

    public Optional<String> fetchShortBuildUps(Symbol symbol, long days) {
        Optional<Contract> optionalContract = contractRepository.findBySymbolAndExpiryDate(symbol, getCurrentMonthlyExpiry());
        if (optionalContract.isPresent()) {
            List<ContractEodAnalytics> list = contractEodAnalyticsRepository.findTop50ByContractOrderByAnalyticsDateDesc(optionalContract.get());
            return Optional.of(list.stream().limit(days).filter(e -> (Objects.nonNull(e.getSignal()) && e.getSignal().contains("SHORT"))).map(o -> o.getAnalyticsDate().format(DateTimeFormatter.ISO_DATE)).collect(Collectors.joining(",")));
        }
        return Optional.empty();
    }


    public boolean isLongDiscountedForCurrentExpiry(Symbol symbol) {
        Optional<Symbol> symbolOptional = symbolService.findAllFOSymbols().stream().filter(s -> s.getTicker().equals(symbol.getTicker())).findAny();
        if (symbolOptional.isPresent()) {
            Optional<Contract> optionalContract = contractRepository.findBySymbolAndExpiryDate(symbolOptional.get(), getCurrentMonthlyExpiry());
            if (optionalContract.isPresent()) {
                ContractEodData contractEodData = contractEodDataRepository.findTop1ByContractOrderByCollectionDateDesc(optionalContract.get());
                double latestFutureClose = contractEodData.getClose();
                double latestEqPrice = equityService.getPreviousDayUnderlyingValue(symbolOptional.get()).getClose();
                return latestFutureClose < latestEqPrice;
            }
        }
        return false;
    }

    public boolean isShortPremiumForCurrentExpiry(Symbol symbol) {
        Optional<Symbol> symbolOptional = symbolService.findAllFOSymbols().stream().filter(s -> s.getTicker().equals(symbol.getTicker())).findAny();
        if (symbolOptional.isPresent()) {
            Optional<Contract> optionalContract = contractRepository.findBySymbolAndExpiryDate(symbolOptional.get(), getCurrentMonthlyExpiry());
            if (optionalContract.isPresent()) {
                ContractEodData contractEodData = contractEodDataRepository.findTop1ByContractOrderByCollectionDateDesc(optionalContract.get());
                double latestFutureClose = contractEodData.getClose();
                double latestEqPrice = equityService.getPreviousDayUnderlyingValue(symbolOptional.get()).getClose();
                return latestFutureClose > latestEqPrice;
            }
        }
        return false;
    }

    private LocalDate getCurrentMonthlyExpiry() {
        return getLastThursday(LocalDateTime.now().getMonthValue(), LocalDateTime.now().getYear());

    }

    public LocalDate getLastThursday(int month, int year) {
        LocalDate lastThursday = LocalDate.of(year, month, 1).with(lastInMonth(THURSDAY));
        return lastThursday;
    }
}
