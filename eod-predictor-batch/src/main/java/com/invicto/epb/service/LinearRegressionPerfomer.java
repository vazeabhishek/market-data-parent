package com.invicto.epb.service;

import com.invicto.epb.model.PredictionInput;
import org.apache.commons.math.stat.regression.SimpleRegression;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinearRegressionPerfomer {

    private final int OI_PREDICT = 1;
    private final int VOL_PREDICT = 2;
    private final int UNDERLYING_PREDICT = 3;

    public PredictionInput performLinearRegression(List<Double> xVals, List<PredictionInput> inputs, double oX) {
        if (xVals.size() != inputs.size())
            throw new RuntimeException("Could not perform Regression");
        else {
            PredictionInput output = new PredictionInput();
            output.setUnderlyingValue(predict(xVals, inputs, oX, UNDERLYING_PREDICT));
            output.setOi(predict(xVals, inputs, oX, OI_PREDICT));
            output.setVolume(predict(xVals, inputs, oX, VOL_PREDICT));
            return output;
        }
    }

    private double predict(List<Double> xVals, List<PredictionInput> inputs, double oX, int pType) {
        SimpleRegression simpleRegression = new SimpleRegression();
        for (int i = 0; i < xVals.size(); i++) {
            if (pType == UNDERLYING_PREDICT)
                simpleRegression.addData(xVals.get(i), inputs.get(i).getUnderlyingValue());
            if (pType == OI_PREDICT)
                simpleRegression.addData(xVals.get(i), inputs.get(i).getOi());
            if (pType == VOL_PREDICT)
                simpleRegression.addData(xVals.get(i), inputs.get(i).getVolume());
        }
        return simpleRegression.predict(oX);
    }
}
