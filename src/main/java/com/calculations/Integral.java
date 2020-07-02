package com.calculations;

import org.apache.log4j.Logger;

import java.util.List;

public class Integral {

    private final static Logger logger = Logger.getLogger(Integral.class);

    public static Float getPositiveIntegral(List<Integer> xValues, List<Float> yValues) {
//        int[] x = {1, 2, 3, 4, 5, 6, 7, 8};
//        float[] y = {1, 1, -2, -3, -4, -5, -6, -7};
        Integer x[] = new Integer[xValues.size()];
        xValues.toArray(x);
        Float y[] = new Float[yValues.size()];
        yValues.toArray(y);

        float sumPositive = 0;

        for (int i = 0; i < x.length - 1; i++) {
            if ( x[i + 1] >= 0 &
                    (Float) y[i + 1] >= (float) 0) {
                sumPositive += ((Float) y[i] + (Float) y[i + 1]) * 0.5 * ((Integer) x[i + 1] - (Integer) x[i]);
            }
        }
//        logger.debug(sumPositive);
        return sumPositive;

    }

    public static float getNegativeIntegral(List<Integer> xValues, List<Float> yValues) {
//        int[] x = {1, 2, 3, 4, 5, 6, 7, 8};
//        float[] y = {1, 1, -2, -3, -4, -5, -6, -7};

        Integer x[] = new Integer[xValues.size()];
        xValues.toArray(x);
        Float y[] = new Float[yValues.size()];
        yValues.toArray(y);

        float sumNegative = 0;

        for (int i = 0; i < x.length - 1; i++) {
            if (x[i] >= 0 & y[i] <= 0) {
                sumNegative += Math.abs(y[i] + y[i + 1]) * 0.5 * (x[i + 1] - x[i]);

            }
        }
//        logger.debug(sumNegative);
        return sumNegative;
    }

    public static float getCustomIntegral(List<Integer> xValues, List<Float> yValues, int beginIndex, int endIndex) {

        Integer x[] = new Integer[xValues.size()];
        xValues.toArray(x);
        Float y[] = new Float[yValues.size()];
        yValues.toArray(y);

        float sumCustom = 0;

        if((y[beginIndex] >= 0 && y[endIndex] >= 0) || (y[beginIndex] <= 0 && y[endIndex] <= 0)){
            if(beginIndex < endIndex) {
                for (int i = beginIndex; i < endIndex; i++) {
                        sumCustom += Math.abs(y[i] + y[i + 1]) * 0.5 * (x[i + 1] - x[i]);
                }
            } else {
                for (int i = endIndex; i < beginIndex; i++) {
                        sumCustom += Math.abs(y[i] + y[i + 1]) * 0.5 * (x[i + 1] - x[i]);
                }
            }
        } else {
            throw new RuntimeException("Dots not valid");
        }

//        logger.debug(sumCustom);
        return sumCustom;
    }
}
