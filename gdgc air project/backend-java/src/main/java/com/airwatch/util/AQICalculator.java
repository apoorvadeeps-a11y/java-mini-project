package com.airwatch.util;

import com.airwatch.model.AirQualityData;
import lombok.extern.slf4j.Slf4j;

/**
 * AQI Calculator Utility Class
 * 
 * Topics Covered:
 * - Utility classes with static methods
 * - Mathematical calculations
 * - Comparable interface usage
 * - Control flow: if-else, loops
 */
@Slf4j
public class AQICalculator {

    /**
     * Calculate AQI sub-index for PM2.5
     * Topics: Arrays, loops, mathematical calculations
     */
    public static int calculatePM25SubIndex(double concentration) {
        double[][] breakpoints = {
            {0, 30, 0, 50},
            {30, 60, 51, 100},
            {60, 90, 101, 200},
            {90, 120, 201, 300},
            {120, 250, 301, 400},
            {250, 500, 401, 500}
        };
        
        return calculateSubIndex(concentration, breakpoints);
    }

    /**
     * Calculate AQI sub-index for PM10
     */
    public static int calculatePM10SubIndex(double concentration) {
        double[][] breakpoints = {
            {0, 50, 0, 50},
            {50, 100, 51, 100},
            {100, 250, 101, 200},
            {250, 350, 201, 300},
            {350, 430, 301, 400},
            {430, 600, 401, 500}
        };
        
        return calculateSubIndex(concentration, breakpoints);
    }

    /**
     * Generic sub-index calculator
     * Topics: Arrays, loops, mathematical formula
     */
    private static int calculateSubIndex(double concentration, double[][] breakpoints) {
        for (double[] bp : breakpoints) {
            double cLow = bp[0];
            double cHigh = bp[1];
            double iLow = bp[2];
            double iHigh = bp[3];
            
            if (concentration >= cLow && concentration <= cHigh) {
                double subIndex = ((iHigh - iLow) / (cHigh - cLow)) * (concentration - cLow) + iLow;
                return (int) Math.round(subIndex);
            }
        }
        
        return concentration > 0 ? 500 : 0;
    }

    /**
     * Calculate overall AQI (max of all sub-indices)
     * Topics: Variable arguments, Collections
     */
    public static int calculateOverallAQI(int... subIndices) {
        int maxAQI = 0;
        for (int subIndex : subIndices) {
            if (subIndex > maxAQI) {
                maxAQI = subIndex;
            }
        }
        return maxAQI;
    }

    /**
     * Get AQI category label
     * Topics: Switch statements, enums
     */
    public static String getAQILabel(int aqi) {
        if (aqi <= 50) return "Good";
        if (aqi <= 100) return "Moderate";
        if (aqi <= 150) return "Unhealthy for Sensitive Groups";
        if (aqi <= 200) return "Unhealthy";
        if (aqi <= 300) return "Very Unhealthy";
        return "Hazardous";
    }

    /**
     * Get health recommendation based on AQI
     * Topics: String manipulation, control flow
     */
    public static String getHealthRecommendation(int aqi) {
        StringBuilder recommendation = new StringBuilder();
        
        if (aqi <= 50) {
            recommendation.append("Air quality is good. Enjoy outdoor activities!");
        } else if (aqi <= 100) {
            recommendation.append("Air quality is acceptable. Sensitive individuals should limit prolonged outdoor exertion.");
        } else if (aqi <= 150) {
            recommendation.append("Members of sensitive groups may experience health effects. General public less likely to be affected.");
        } else if (aqi <= 200) {
            recommendation.append("Everyone may begin to experience health effects. Sensitive groups may experience more serious effects.");
        } else if (aqi <= 300) {
            recommendation.append("Health alert: everyone may experience more serious health effects.");
        } else {
            recommendation.append("Health warning: emergency conditions. Entire population is likely to be affected.");
        }
        
        return recommendation.toString();
    }
}
