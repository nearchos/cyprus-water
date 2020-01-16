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
import com.google.apphosting.api.DatastorePb;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.nearchos.water.api.GetDayStatisticsServlet;

import java.time.*;
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
    public static final String KIND_EVENT = "event";
    public static final String PROPERTY_EVENT_NAME_EN = "event-name-en";
    public static final String PROPERTY_EVENT_NAME_EL = "event-name-el";
    public static final String PROPERTY_EVENT_TYPE = "event-type";
    public static final String PROPERTY_EVENT_DESCRIPTION = "event-description";
    public static final String PROPERTY_EVENT_FROM = "event-from";
    public static final String PROPERTY_EVENT_UNTIL = "event-until";
    public static final String KIND_MONTHLY_INFLOW = "monthly-inflow";
    public static final String PROPERTY_MONTHLY_INFLOW_TIMESTAMP = "monthly-inflow-timestamp";
    public static final String PROPERTY_MONTHLY_INFLOW_YEAR = "monthly-inflow-year";
    public static final String PROPERTY_MONTHLY_INFLOW_PERIOD = "monthly-inflow-period";
    public static final String PROPERTY_MONTHLY_INFLOW_VALUE = "monthly-inflow-value";

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

    public static Vector<DayStatistics> getSignificantDayStatistics() {

        final Vector<DayStatistics> significantDayStatistics = new Vector<>();

        final Query query = new Query(KIND_DAY_STATISTICS)
                .addSort(PROPERTY_DAY_STATISTICS_DAY, Query.SortDirection.ASCENDING);

        final PreparedQuery preparedQuery = datastoreService.prepare(query);

        log.info("looking up significant dayStatistics");

        final Vector<Event> events = DatastoreHelper.getAllEvents();

        Date lastDate = null;

        // assert exactly one (or none) is found
        for (final Entity dayStatisticsEntity : preparedQuery.asIterable()) {
            final String dayStatisticsJson = (String) dayStatisticsEntity.getProperty(PROPERTY_DAY_STATISTICS_JSON);
            final DayStatistics dayStatistics = gson.fromJson(dayStatisticsJson, DayStatistics.class);
            final Date date = dayStatistics.getDate();
            // get dates which are the first entries of a week or dates which  are parts of 'events'
            if(lastDate == null || isFirstInWeek(lastDate, date) || isContained(events, date) || isInLastDays(7, date)) {
                significantDayStatistics.add(dayStatistics);
                lastDate = date;
            }
        }
        return significantDayStatistics;
    }

    private static boolean isContained(final Vector<Event> events, final Date date) {
        final long timestamp = date.getTime();
        for(final Event event : events) {
            if(event.getFrom() <= timestamp && timestamp <= event.getUntil()) {
                log.info("Found date: " + date + " is in event: " + event.getNameEn());
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the specified date is a Monday, or >= 7 days since the last reported date
     * @param lastDate the last reported date
     * @param date the specified date to be checked
     * @return true if the specified date is a Monday, or >= 7 days since the last reported date
     */
    private static boolean isFirstInWeek(final Date lastDate, final Date date) {
        final LocalDate lastLocalDate = lastDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        final LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getDayOfWeek() == DayOfWeek.MONDAY // date is a Monday
                || Period.between(lastLocalDate, localDate).getDays() >= 7; // or duration between the two is > 7 days
    }

    /**
     * Returns true if the specified date is within numOfDays of today, e.g. in the last week for numOfDays=7.
     * @param numOfDays how many days back to check
     * @param date the specified date to be checked
     * @return true if the specified date is within numOfDays of today
     */
    private static boolean isInLastDays(final int numOfDays, final Date date) {
        final LocalDate today = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        final LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(localDate, today).getDays() <= numOfDays;
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

    public static void addEvent(final Event event) {

        final Entity eventEntity = new Entity(KIND_EVENT);
        eventEntity.setProperty(PROPERTY_EVENT_NAME_EN, event.getNameEn());
        eventEntity.setProperty(PROPERTY_EVENT_NAME_EL, event.getNameEl());
        eventEntity.setProperty(PROPERTY_EVENT_TYPE, event.getType().name());
        eventEntity.setProperty(PROPERTY_EVENT_DESCRIPTION, event.getDescription());
        eventEntity.setProperty(PROPERTY_EVENT_FROM, event.getFrom());
        eventEntity.setProperty(PROPERTY_EVENT_UNTIL, event.getUntil());
        datastoreService.put(eventEntity);
    }

    public static Vector<Event> getAllEvents() {

        final Vector<Event> allEvents = new Vector<>();

        final Query query = new Query(KIND_EVENT)
                .addSort(PROPERTY_EVENT_FROM, Query.SortDirection.ASCENDING);

        final PreparedQuery preparedQuery = datastoreService.prepare(query);

        log.info("looking up all events");

        for (final Entity eventEntity : preparedQuery.asIterable()) {
            final String nameEn = (String) eventEntity.getProperty(PROPERTY_EVENT_NAME_EN);
            final String nameEl = (String) eventEntity.getProperty(PROPERTY_EVENT_NAME_EL);
            final Event.Type type = Event.Type.valueOf(eventEntity.getProperty(PROPERTY_EVENT_TYPE).toString());
            final String description  = (String) eventEntity.getProperty(PROPERTY_EVENT_DESCRIPTION);
            final long from = (Long) eventEntity.getProperty(PROPERTY_EVENT_FROM);
            final long until = (Long) eventEntity.getProperty(PROPERTY_EVENT_UNTIL);
            allEvents.add(new Event(nameEn, nameEl, type, description, from, until));
        }

        return allEvents;
    }

    public static void addMonthlyInflow(final MonthlyInflow monthlyInflow) {

        final Entity monthlyInflowEntity = new Entity(KIND_MONTHLY_INFLOW);
        monthlyInflowEntity.setProperty(PROPERTY_MONTHLY_INFLOW_TIMESTAMP, monthlyInflow.getTimestamp());
        monthlyInflowEntity.setProperty(PROPERTY_MONTHLY_INFLOW_YEAR, monthlyInflow.getYear());
        monthlyInflowEntity.setProperty(PROPERTY_MONTHLY_INFLOW_PERIOD, monthlyInflow.getPeriod().name());
        monthlyInflowEntity.setProperty(PROPERTY_MONTHLY_INFLOW_VALUE, monthlyInflow.getInflowInMCM());
        datastoreService.put(monthlyInflowEntity);
    }

    public static Vector<MonthlyInflow> getAllMonthlyInflows() {

        final Vector<MonthlyInflow> allMonthlyInflows = new Vector<>();

        final Query query = new Query(KIND_MONTHLY_INFLOW);

        final PreparedQuery preparedQuery = datastoreService.prepare(query);

        log.info("looking up all monthly inflows");

        for (final Entity monthlyInflowEntity : preparedQuery.asIterable()) {
            final long timestamp = (Long) monthlyInflowEntity.getProperty(PROPERTY_MONTHLY_INFLOW_TIMESTAMP);
            final long year = (Long) monthlyInflowEntity.getProperty(PROPERTY_MONTHLY_INFLOW_YEAR);
            final MonthlyInflow.Period period = MonthlyInflow.Period.valueOf(monthlyInflowEntity.getProperty(PROPERTY_MONTHLY_INFLOW_PERIOD).toString());
            final Double inflowInMCM  = (Double) monthlyInflowEntity.getProperty(PROPERTY_MONTHLY_INFLOW_VALUE);
            allMonthlyInflows.add(new MonthlyInflow(timestamp, (int) year, period, inflowInMCM));
        }

        return allMonthlyInflows;
    }

    public static void updateMonthlyInflow(final MonthlyInflow monthlyInflow) {
        final Query query = new Query(KIND_MONTHLY_INFLOW);
        query.setFilter(new Query.CompositeFilter(Query.CompositeFilterOperator.AND, Arrays.asList(
            new Query.FilterPredicate(PROPERTY_MONTHLY_INFLOW_YEAR, Query.FilterOperator.EQUAL, monthlyInflow.getYear()),
            new Query.FilterPredicate(PROPERTY_MONTHLY_INFLOW_PERIOD, Query.FilterOperator.EQUAL, monthlyInflow.getPeriod().name())
        )));

        final Entity monthlyInflowEntity = datastoreService.prepare(query).asSingleEntity();
        assert monthlyInflowEntity != null;
        monthlyInflowEntity.setProperty(PROPERTY_MONTHLY_INFLOW_TIMESTAMP, System.currentTimeMillis());
        monthlyInflowEntity.setProperty(PROPERTY_MONTHLY_INFLOW_VALUE, monthlyInflow.getInflowInMCM());

        datastoreService.put(monthlyInflowEntity);
    }
}