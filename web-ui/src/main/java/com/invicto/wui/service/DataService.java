package com.invicto.wui.service;

import com.invicto.mdp.entity.SymbolEodPrediction;
import com.invicto.mdp.repository.ContractEodAnalyticsRepository;
import com.invicto.mdp.repository.EquityEodDataRepository;
import com.invicto.mdp.repository.SymbolEodPredictionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DataService {

    private SymbolEodPredictionRepository symbolEodPredictionRepository;
    private ContractEodAnalyticsRepository contractEodAnalyticsRepository;
    private EquityEodDataRepository equityEodDataRepository;

    public List<SymbolEodPrediction> getPrediction(LocalDate date) {
        return symbolEodPredictionRepository.findAllByPredictionDateAndSignalIsNot(date, "NEUTRAL");
    }

    public void getLongsForNDays(Long days) {
        contractEodAnalyticsRepository.findTop10BySignalOrderByAnalyticsDateDesc("LONG_BUILD_UP");
    }
}
