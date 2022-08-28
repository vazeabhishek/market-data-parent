package com.invicto.epb.service;

import com.invicto.epb.model.PredictionInput;
import com.invicto.mdp.entity.Symbol;
import com.invicto.mdp.entity.SymbolEodPrediction;
import com.invicto.mdp.entity.SymbolIntraday15mSnap;
import com.invicto.mdp.entity.SymbolIntraday1HSnap;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SymbolEodPredictionOrch {
    private final SymbolService symbolService;
    private final LinearRegressionPerfomer linearRegressionPerfomer;


    public SymbolEodPredictionOrch(SymbolService symbolService, LinearRegressionPerfomer linearRegressionPerfomer) {
        this.symbolService = symbolService;
        this.linearRegressionPerfomer = linearRegressionPerfomer;
    }

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


            PredictionInput pOutput15m = linearRegressionPerfomer.performLinearRegression(xVals, predictionInputs15m, 25);


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

            PredictionInput pOutput1h = linearRegressionPerfomer.performLinearRegression(x1Vals, predictionInputs1h, 5);

            SymbolEodPrediction symbolEodPrediction = new SymbolEodPrediction();
            symbolEodPrediction.setPDeltaOi(avg(pOutput15m.getOi(), pOutput1h.getOi()));
            symbolEodPrediction.setUnderlyingValue(avg(pOutput15m.getUnderlyingValue(), pOutput1h.getUnderlyingValue()));
            symbolEodPrediction.setVolume(avg(pOutput15m.getVolume(), pOutput1h.getVolume()));

        }

    }

    private double avg(double a, double b) {
        return (a + b) / 2;
    }
}
