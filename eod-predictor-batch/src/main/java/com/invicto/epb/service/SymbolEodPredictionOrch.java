package com.invicto.epb.service;

import com.invicto.epb.model.PredictionInput;
import com.invicto.epb.model.vo.IntradaySnapVo;
import com.invicto.epb.model.vo.PredictionVo;
import com.invicto.epb.model.enums.ViolationTypeEnum;
import com.invicto.epb.service.bridge.SymbolService;
import com.invicto.epb.service.impl.OIProcessor;
import com.invicto.mdp.entity.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SymbolEodPredictionOrch {
    private final SymbolService symbolService;
    private final LinearRegressionPerfomer linearRegressionPerfomer;

    private final Processor processor;


    public SymbolEodPredictionOrch(SymbolService symbolService, LinearRegressionPerfomer linearRegressionPerfomer, OIProcessor processor) {
        this.symbolService = symbolService;
        this.linearRegressionPerfomer = linearRegressionPerfomer;
        this.processor = processor;
    }

    public void predictPricesForAllSymbols() {

        List<Symbol> foSymbols = symbolService.findAllFOSymbols();
        LocalDate jobRunDate = LocalDate.now().minusDays(1);
        for (Symbol sym : foSymbols) {

            // 15m linear regression
            List<SymbolIntraday15mSnap> intraday15mSnaps = symbolService.find15mIntradaySnapsBySymbol(sym, jobRunDate);
            List<PredictionInput> predictionInputs15m = intraday15mSnaps.stream().sorted().map(this::mapToIntradaySnap).map(this::mapToPredictionInput).collect(Collectors.toList());
            List<Double> xVals = getXvals(predictionInputs15m.size());
            PredictionInput pOutput15m = linearRegressionPerfomer.performLinearRegression(xVals, predictionInputs15m, predictionInputs15m.size() + 2);

            //1h linear regression
            List<SymbolIntraday1HSnap> intraday1hSnaps = symbolService.find1hIntradaySnapsBySymbol(sym, jobRunDate);
            List<PredictionInput> predictionInputs1h = intraday1hSnaps.stream().sorted().map(this::mapToIntradaySnap).map(this::mapToPredictionInput).collect(Collectors.toList());
            List<Double> x1Vals = getXvals(predictionInputs1h.size());
            PredictionInput pOutput1h = linearRegressionPerfomer.performLinearRegression(x1Vals, predictionInputs1h, predictionInputs1h.size() + 1);


            PredictionVo predictionVo = new PredictionVo();
            predictionVo.setSymbol(sym);
            predictionVo.setRunDate(jobRunDate);
            predictionVo.setUnderlyingPrice(pOutput15m.getUnderlyingValue());
            predictionVo.setPDeltaOi(avg(pOutput15m.getOi(), pOutput1h.getOi()));
            predictionVo.setPDeltaPrice(avg(pOutput15m.getUnderlyingValue(), pOutput1h.getUnderlyingValue()));
            try {
                processor.execute(predictionVo);
            } catch (Exception ex) {
                if (predictionVo.getViolationType() == null) {
                    predictionVo.setViolationType(ViolationTypeEnum.UNKNOWN);
                    predictionVo.setViolationText(ex.getMessage());
                }
            } finally {
                symbolService.saveEodPrediction(mapToPredictionEntity(jobRunDate, predictionVo));
            }
        }
    }

    private double avg(double a, double b) {
        return (a + b) / 2;
    }

    private SymbolEodPrediction mapToPredictionEntity(LocalDate rundate, PredictionVo predictionVo) {
        SymbolEodPrediction symbolEodPrediction = new SymbolEodPrediction();
        symbolEodPrediction.setSymbolId(predictionVo.getSymbol().getId());
        symbolEodPrediction.setPredictedDeltaVolume(predictionVo.getPDeltaVol());
        symbolEodPrediction.setPredictionDate(rundate);
        symbolEodPrediction.setPredictedPrice(predictionVo.getPDeltaPrice());
        symbolEodPrediction.setPredictedDeltaOI(predictionVo.getPDeltaOi());
        symbolEodPrediction.setSignal(predictionVo.getSignal().name());
        symbolEodPrediction.setViolationText(predictionVo.getViolationText());
        symbolEodPrediction.setViolationType(predictionVo.getViolationType().name());
        return symbolEodPrediction;
    }

    private IntradaySnapVo mapToIntradaySnap(SymbolIntraday1HSnap intraday1HSnap) {
        return getIntradaySnapVo(intraday1HSnap.getCollectionDate(), intraday1HSnap.getChgInOi(), intraday1HSnap.getPrevOi(), intraday1HSnap.getCollectionTime(), intraday1HSnap.getVolume(), intraday1HSnap.getOi(), intraday1HSnap.getUnderlyingValue());
    }

    private IntradaySnapVo mapToIntradaySnap(SymbolIntraday15mSnap intraday15mSnap) {
        return getIntradaySnapVo(intraday15mSnap.getCollectionDate(), intraday15mSnap.getChgInOi(), intraday15mSnap.getPrevOi(), intraday15mSnap.getCollectionTime(), intraday15mSnap.getVolume(), intraday15mSnap.getOi(), intraday15mSnap.getUnderlyingValue());
    }

    private IntradaySnapVo getIntradaySnapVo(LocalDate collectionDate, double chgInOi, double prevOi, LocalTime collectionTime, double volume, double oi, double underlyingValue) {
        IntradaySnapVo intradaySnapVo = new IntradaySnapVo();
        intradaySnapVo.setCollectionDate(collectionDate);
        intradaySnapVo.setChgInOi(chgInOi);
        intradaySnapVo.setPrevOi(prevOi);
        intradaySnapVo.setCollectionTime(collectionTime);
        intradaySnapVo.setVolume(volume);
        intradaySnapVo.setOi(oi);
        intradaySnapVo.setUnderlyingValue(underlyingValue);
        return intradaySnapVo;
    }

    private PredictionInput mapToPredictionInput(IntradaySnapVo intradaySnapVo) {
        PredictionInput predictionInput = new PredictionInput();
        predictionInput.setUnderlyingValue(intradaySnapVo.getUnderlyingValue());
        predictionInput.setVolume(intradaySnapVo.getVolume());
        predictionInput.setOi(intradaySnapVo.getChgInOi());
        return predictionInput;
    }

    private List<Double> getXvals(int xmax) {
        List<Double> xVals = new LinkedList<>();
        for (int i = 0; i < xmax; i++) {
            xVals.add(Double.valueOf(i));
        }
        return xVals;
    }

}
