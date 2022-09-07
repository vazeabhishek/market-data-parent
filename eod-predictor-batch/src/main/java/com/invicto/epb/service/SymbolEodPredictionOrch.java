package com.invicto.epb.service;

import com.invicto.epb.model.EquityDataVo;
import com.invicto.epb.model.PredictionInput;
import com.invicto.mdp.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SymbolEodPredictionOrch {
    private final SymbolService symbolService;
    private final LinearRegressionPerfomer linearRegressionPerfomer;

    private final ContractService contractService;

    private EquityService equityService;


    public SymbolEodPredictionOrch(SymbolService symbolService, LinearRegressionPerfomer linearRegressionPerfomer, ContractService contractService, EquityService equityService) {
        this.symbolService = symbolService;
        this.linearRegressionPerfomer = linearRegressionPerfomer;
        this.contractService = contractService;
        this.equityService = equityService;
    }

    @Autowired


    public void predictPricesForAllSymbols() {

        List<Symbol> foSymbols = symbolService.findAllFOSymbols();
        for (Symbol sym : foSymbols) {

            List<SymbolIntraday15mSnap> intraday15mSnaps = symbolService.find15mIntradaySnapsBySymbol(sym);

            List<PredictionInput> predictionInputs15m = intraday15mSnaps.stream().sorted(Comparator.comparing(SymbolIntraday15mSnap::getCollectionTime)).map(symbolIntraday15mSnap -> {
                PredictionInput predictionInput = new PredictionInput();
                predictionInput.setUnderlyingValue(symbolIntraday15mSnap.getUnderlyingValue());
                predictionInput.setVolume(symbolIntraday15mSnap.getVolume());
                predictionInput.setOi(symbolIntraday15mSnap.getChgInOi());
                return predictionInput;
            }).collect(Collectors.toList());

            List<Double> xVals = new LinkedList<>();

            for (int i = 0; i < predictionInputs15m.size(); i++) {
                xVals.add(Double.valueOf(i));
            }


            PredictionInput pOutput15m = linearRegressionPerfomer.performLinearRegression(xVals, predictionInputs15m, predictionInputs15m.size() + 2);

            List<SymbolIntraday1HSnap> intraday1hSnaps = symbolService.find1hIntradaySnapsBySymbol(sym);

            List<PredictionInput> predictionInputs1h = intraday1hSnaps.stream().sorted(Comparator.comparing(SymbolIntraday1HSnap::getCollectionTime)).map(symbolIntraday1hSnap -> {
                PredictionInput predictionInput = new PredictionInput();
                predictionInput.setUnderlyingValue(symbolIntraday1hSnap.getUnderlyingValue());
                predictionInput.setVolume(symbolIntraday1hSnap.getVolume());
                predictionInput.setOi(symbolIntraday1hSnap.getChgInOi());
                return predictionInput;
            }).collect(Collectors.toList());

            List<Double> x1Vals = new LinkedList<>();
            for (int i = 0; i < predictionInputs1h.size(); i++) {
                x1Vals.add(Double.valueOf(i));
            }


            PredictionInput pOutput1h = linearRegressionPerfomer.performLinearRegression(x1Vals, predictionInputs1h, predictionInputs1h.size() + 1);

            SymbolEodPrediction symbolEodPrediction = new SymbolEodPrediction();
            symbolEodPrediction.setSymbolId(sym.getId());
            symbolEodPrediction.setPredictedDeltaOI(avg(pOutput15m.getOi(), pOutput1h.getOi()));
            symbolEodPrediction.setPredictedPrice(avg(pOutput15m.getUnderlyingValue(), pOutput1h.getUnderlyingValue()));
            symbolEodPrediction.setPredictedDeltaVolume(avg(pOutput15m.getVolume(), pOutput1h.getVolume()));
            symbolEodPrediction.setPredictionDate(LocalDate.now());
            try {
                EquityDataVo prevDayData = equityService.getPreviousDayUnderlyingValue(sym);

                double pPrevClose = ((symbolEodPrediction.getPredictedPrice() - prevDayData.getClose()) / prevDayData.getClose()) * 100;
                if (isDeltaOiPassesForLong(5.0d, symbolEodPrediction) && isPriceCheckPassForLong(2.0d, pPrevClose, symbolEodPrediction) && !checkShortBuildExists(sym, symbolEodPrediction))
                    symbolEodPrediction.setSignal("BUY");

                else if (isDeltaOiPassesForShort(-5.0d, symbolEodPrediction) && isPriceCheckPassForShort(3.0d, pPrevClose, symbolEodPrediction) && !checkLongBuildExists(sym, symbolEodPrediction))
                    symbolEodPrediction.setSignal("SELL");
                else
                    symbolEodPrediction.setSignal("NEUTRAL");

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            symbolService.saveEodPrediction(symbolEodPrediction);
        }


    }

    private boolean isDeltaOiPassesForLong(double threshHold, SymbolEodPrediction symbolEodPrediction) {
        if (symbolEodPrediction.getPredictedDeltaOI() > threshHold) {
            symbolEodPrediction.setHasRequiredOi(true);
            return true;
        } else {
            symbolEodPrediction.setHasRequiredOi(false);
            return false;
        }
    }

    private boolean isDeltaOiPassesForShort(double threshHold, SymbolEodPrediction symbolEodPrediction) {
        if (symbolEodPrediction.getPredictedDeltaOI() < threshHold) {
            symbolEodPrediction.setHasRequiredOi(true);
            return true;
        } else {
            symbolEodPrediction.setHasRequiredOi(false);
            return false;
        }
    }

    private boolean isPriceCheckPassForLong(double threshHold, double pPclose, SymbolEodPrediction symbolEodPrediction) {
        if (pPclose > threshHold) {
            symbolEodPrediction.setHasRequiredPrice(true);
            return true;
        } else {
            symbolEodPrediction.setHasRequiredPrice(false);
            return false;
        }
    }

    private boolean isPriceCheckPassForShort(double threshHold, double pPclose, SymbolEodPrediction symbolEodPrediction) {
        if (pPclose < threshHold) {
            symbolEodPrediction.setHasRequiredPrice(true);
            return true;
        } else {
            symbolEodPrediction.setHasRequiredPrice(false);
            return false;
        }
    }

    private boolean checkShortBuildExists(Symbol symbol, SymbolEodPrediction symbolEodPrediction) {
        Optional<ContractEodAnalytics> contractEodAnalyticsOptional = contractService.checkShortBuildUpExists(symbol, 10l);
        if (contractEodAnalyticsOptional.isPresent()) {
            symbolEodPrediction.setBuildUpCheckViolated("SHORT");
            symbolEodPrediction.setBuildUpCheckViolationDate(contractEodAnalyticsOptional.get().getAnalyticsDate());
            return true;
        } else
            symbolEodPrediction.setBuildUpCheckViolated("NONE");
        return false;
    }

    private boolean checkLongBuildExists(Symbol symbol, SymbolEodPrediction symbolEodPrediction) {
        Optional<ContractEodAnalytics> contractEodAnalyticsOptional = contractService.checkLongBuildUpExists(symbol, 10l);
        if (contractEodAnalyticsOptional.isPresent()) {
            symbolEodPrediction.setBuildUpCheckViolated("LONG");
            symbolEodPrediction.setBuildUpCheckViolationDate(contractEodAnalyticsOptional.get().getAnalyticsDate());
            return true;
        } else
            symbolEodPrediction.setBuildUpCheckViolated("NONE");
        return false;
    }

    private double avg(double a, double b) {
        return (a + b) / 2;
    }
}
