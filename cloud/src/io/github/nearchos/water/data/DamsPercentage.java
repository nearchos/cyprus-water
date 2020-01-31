/*
 * Copyright (c) 2018.
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static io.github.nearchos.water.data.Dam.ONE_MILLION;
import static io.github.nearchos.water.data.DayStatistics.SIMPLE_DATE_FORMAT;

public class DamsPercentage implements Serializable {

    public static final Logger log = Logger.getLogger("data");

    private transient static final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    private final Map<String, Double> damNamesToPercentage = new HashMap<>();
    private Date date;
    private final double totalPercentage;
    private final double totalCapacityInMCM;

    public DamsPercentage(final DayStatistics dayStatistics) {

        assert dayStatistics != null;
        this.date = dayStatistics.getDate();
        final Collection<String> allDamNames = dayStatistics.getDamNames();
        double currentStorage = 0d;
        for(final String damName : allDamNames) {
            currentStorage += dayStatistics.getStorageInMCM(damName);
        }

        long tempTotalCapacityInCM = 0L;
        final Dam [] allDams = gson.fromJson(Data.DAMS_JSON, Dam [].class);
        for(final Dam dam : allDams) {
            tempTotalCapacityInCM += dam.getCapacity();

            final String damNameEn = dam.getNameEn();
            try {
                damNamesToPercentage.put(damNameEn, dayStatistics.getStorageInMCM(damNameEn) / dam.getCapacityInMCM());
            } catch (Exception e) {
                log.warning("DayStatistics do not contain info for dam '" + damNameEn + "', dayStatistics: " + dayStatistics);
            }
        }

        this.totalCapacityInMCM = 1D * tempTotalCapacityInCM / ONE_MILLION;
        totalPercentage = currentStorage / totalCapacityInMCM;
    }

    public Date getDate() {
        return date;
    }

    public String getDateAsString() {
        return SIMPLE_DATE_FORMAT.format(date);
    }

    public double getPercentage(final String damName) {
        return damNamesToPercentage.get(damName);
    }

    public double getTotalCapacityInMCM() {
        return totalCapacityInMCM;
    }

    public double getTotalPercentage() {
        return totalPercentage;
    }
}