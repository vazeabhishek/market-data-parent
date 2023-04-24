package com.invicto.fbl.helper;

public class MathsHelper {

    public double calculatePercentage(double delta, double against) {
        return 100 * delta / against;
    }

    public double calculateDelta(double latest, double prev){
        return latest - prev;
    }
}
