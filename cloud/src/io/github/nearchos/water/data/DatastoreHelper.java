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

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.nearchos.water.api.GetDayStatisticsServlet;

import java.util.*;
import java.util.logging.Logger;

public class DatastoreHelper {

    public static final Logger log = Logger.getLogger("datastore");

    public static final String KIND_DAY_STATISTICS = "day-statistics";
    public static final String PROPERTY_DAY_STATISTICS_DAY = "day-statistics-day";
    public static final String PROPERTY_DAY_STATISTICS_JSON = "day-statistics-json";
    public static final String KIND_SCORE = "score";
    public static final String PROPERTY_SCORE_NICKNAME = "score-nickname";
    public static final String PROPERTY_SCORE_SCORE = "score-score";
    public static final String PROPERTY_SCORE_DATE = "score-date";

    private static final DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    public static DayStatistics getDayStatistics(final String date) {

        final Query.Filter filter = new Query.FilterPredicate(PROPERTY_DAY_STATISTICS_DAY, Query.FilterOperator.LESS_THAN_OR_EQUAL, date);
        final Query query = new Query(KIND_DAY_STATISTICS)
                .setFilter(filter)
                .addSort(PROPERTY_DAY_STATISTICS_DAY, Query.SortDirection.DESCENDING);

        final PreparedQuery preparedQuery = datastoreService.prepare(query);

        log.info("looking up dayStatistics with date: " + date);

        // assert exactly one (or none) is found
        final Iterator<Entity> iterator = preparedQuery.asIterable().iterator();
        if(iterator.hasNext()) {
            final Entity dayStatisticsEntity = iterator.next();
            final String dayStatisticsJson = (String) dayStatisticsEntity.getProperty(PROPERTY_DAY_STATISTICS_JSON);
            return gson.fromJson(dayStatisticsJson, DayStatistics.class);
        } else {
            log.severe("Could not find " + KIND_DAY_STATISTICS + " with date: " + date);
            return null;
        }
    }

    public static Vector<DayStatistics> getAllDayStatistics() {

        final Vector<DayStatistics> allDayStatistics = new Vector<>();

        final Query query = new Query(KIND_DAY_STATISTICS)
                .addSort(PROPERTY_DAY_STATISTICS_DAY, Query.SortDirection.ASCENDING);

        final PreparedQuery preparedQuery = datastoreService.prepare(query);

        log.info("looking up all dayStatistics");

        // assert exactly one (or none) is found
        for (final Entity dayStatisticsEntity : preparedQuery.asIterable()) {
            final String dayStatisticsJson = (String) dayStatisticsEntity.getProperty(PROPERTY_DAY_STATISTICS_JSON);
            allDayStatistics.add(gson.fromJson(dayStatisticsJson, DayStatistics.class));
        }

        return allDayStatistics;
    }

    public static void addDayStatistics(final DayStatistics dayStatistics) {

        final String date = dayStatistics.getDateAsString();
        final String dayStatisticsJson = gson.toJson(dayStatistics);

        final Entity dayStatisticsEntity = new Entity(KIND_DAY_STATISTICS);
        dayStatisticsEntity.setProperty(PROPERTY_DAY_STATISTICS_DAY, date);
        dayStatisticsEntity.setProperty(PROPERTY_DAY_STATISTICS_JSON, dayStatisticsJson);
        datastoreService.put(dayStatisticsEntity);

        // update memcache
        MemcacheServiceFactory.getMemcacheService().put(GetDayStatisticsServlet.getMemcacheKey(dayStatistics.getDate()), gson.toJson(dayStatistics));
    }

    public static Vector<Dam> getDams() {
        // for now just return the fixed set, i.e. skip the datastore
        return Data.getDams();
    }

    public static void addScore(final Score score) {
        final Entity scoreEntity = new Entity(KIND_SCORE);
        scoreEntity.setProperty(PROPERTY_SCORE_NICKNAME, score.getNickname());
        scoreEntity.setProperty(PROPERTY_SCORE_SCORE, score.getScore());
        scoreEntity.setProperty(PROPERTY_SCORE_DATE, score.getDate());
        datastoreService.put(scoreEntity);
    }

    public static Vector<Score> getAllScores(final boolean sorted) {
        final Vector<Score> allScores = new Vector<>();

        final Query query = new Query(KIND_SCORE);

        final PreparedQuery preparedQuery = datastoreService.prepare(query);

        log.info("looking up all scores");

        for (final Entity scoreEntity : preparedQuery.asIterable()) {
            final String nickname = (String) scoreEntity.getProperty(PROPERTY_SCORE_NICKNAME);
            final long score = (Long) scoreEntity.getProperty(PROPERTY_SCORE_SCORE);
            final Date date = (Date) scoreEntity.getProperty(PROPERTY_SCORE_DATE);
            allScores.add(new Score(nickname, score, date));
        }

        if(sorted) allScores.sort(Score::compareTo);

        return allScores;
    }
}