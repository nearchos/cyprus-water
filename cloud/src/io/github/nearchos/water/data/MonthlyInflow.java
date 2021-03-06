/*
 * Copyright (c) 2020.
 *
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR
 *  "LICENSE"). THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER
 *   THAN AS AUTHORIZED UNDER THIS LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 *
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO BE BOUND BY THE TERMS OF THIS
 *  LICENSE. TO THE EXTENT THIS LICENSE MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS
 *   CONTAINED HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 *
 * The complete license is available at https://creativecommons.org/licenses/by/3.0/legalcode
 */

package io.github.nearchos.water.data;

import java.io.Serializable;

public class MonthlyInflow implements Serializable {

    private long timestamp; // creation timestamp
    private int year; // e.g. 2019
    private Period period; // e.g. AUGUST_AND_SEPTEMBER
    private int periodOrder; // e.g. 8
    private double inflowInMCM; // e.g. 30.495 for December 2019

    public MonthlyInflow(long timestamp, int year, Period period, double inflowInMCM) {
        this.timestamp = timestamp;
        this.year = year;
        this.period = period;
        this.periodOrder = period.getOrder();
        this.inflowInMCM = inflowInMCM;
    }

    public MonthlyInflow(int year, Period period, double inflowInMCM) {
        this(System.currentTimeMillis(), year, period, inflowInMCM);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getYear() {
        return year;
    }

    public Period getPeriod() {
        return period;
    }

    public double getInflowInMCM() {
        return inflowInMCM;
    }

    @Override
    public String toString() {
        return "MonthlyInflow{" +
                "timestamp=" + timestamp +
                ", year=" + year +
                ", period=" + period +
                ", periodOrder=" + periodOrder +
                ", inflowInMCM=" + inflowInMCM +
                '}';
    }
}