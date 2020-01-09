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

import java.util.SortedMap;
import java.util.Vector;

public class Timeseries {

    private final Vector<Dam> dams;
    private final SortedMap<String,DamsPercentage> percentages;
    private final int numOfDams;
    private final int numOfPercentageEntries;

    public Timeseries(final Vector<Dam> dams, final SortedMap<String, DamsPercentage> percentages) {
        this.dams = dams;
        this.percentages = percentages;
        this.numOfDams = dams.size();
        this.numOfPercentageEntries = percentages.size();
    }

    public Vector<Dam> getDams() {
        return dams;
    }

    public SortedMap<String, DamsPercentage> getPercentages() {
        return percentages;
    }

    public int getNumOfDams() {
        return numOfDams;
    }

    public int getNumOfPercentageEntries() {
        return numOfPercentageEntries;
    }
}