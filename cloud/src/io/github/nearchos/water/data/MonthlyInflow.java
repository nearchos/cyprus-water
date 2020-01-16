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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class MonthlyInflow implements Serializable {

    public enum Period {OCTOBER, NOVEMBER, DECEMBER, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST_AND_SEPTEMBER}

    private long timestamp; // creation timestamp
    private int year; // e.g. 2019
    private Period period; // e.g. AUGUST_AND_SEPTEMBER
    private double inflowInMCM; // e.g. 30.495 for December 2019

    public MonthlyInflow(long timestamp, int year, Period period, double inflowInMCM) {
        this.timestamp = timestamp;
        this.year = year;
        this.period = period;
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
                ", inflowInMCM=" + inflowInMCM +
                '}';
    }

    public static boolean inThePresent(final MonthlyInflow monthlyInflow) {
        final LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        final int yearNow = localDate.getYear();
        final int monthValueNow = localDate.getMonthValue(); // 1..12
        final int periodOrdinal = monthlyInflow.getPeriod().ordinal();
        final int correspondingMonthValue = periodOrdinal < 3 ? periodOrdinal + 11 : periodOrdinal - 2; // 1..12
        return monthlyInflow.year == yearNow && correspondingMonthValue == monthValueNow;
    }

    public static boolean inThePastOrPresent(final MonthlyInflow monthlyInflow) {
        final LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        final int yearNow = localDate.getYear();
        final int monthValueNow = localDate.getMonthValue(); // 1..12
        final int periodOrdinal = monthlyInflow.getPeriod().ordinal();
        final int correspondingMonthValue = periodOrdinal < 3 ? periodOrdinal + 11 : periodOrdinal - 2; // 1..12
        return monthlyInflow.year < yearNow || (monthlyInflow.year == yearNow && correspondingMonthValue <= monthValueNow);
    }

    public static boolean inTheFuture(final MonthlyInflow monthlyInflow) {
        return !inThePastOrPresent(monthlyInflow);
    }
}