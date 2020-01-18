package io.github.nearchos.water.api;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.nearchos.water.data.DatastoreHelper;
import io.github.nearchos.water.data.DayStatistics;
import io.github.nearchos.water.data.MonthlyInflow;
import io.github.nearchos.water.data.Period;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.util.*;

public class GetHistoricInflowServlet extends HttpServlet {

    private static final Period [] orderedPeriods = {
            Period.JANUARY,
            Period.FEBRUARY,
            Period.MARCH,
            Period.APRIL,
            Period.MAY,
            Period.JUNE,
            Period.JULY,
            Period.AUGUST_AND_SEPTEMBER,
            Period.OCTOBER,
            Period.NOVEMBER,
            Period.DECEMBER
    };

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    private final MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        final String reply;
        final String memcacheKey = "historical-inflow-" + DayStatistics.SIMPLE_DATE_FORMAT.format(new Date());
        if(memcacheService.contains(memcacheKey)) {
            reply = (String) memcacheService.get(memcacheKey);
        } else {

            final Vector<MonthlyInflow> allMonthlyInflows = DatastoreHelper.getAllMonthlyInflows();

            final Map<String, List<Double>> historicInflow = getHistoricInflow(allMonthlyInflows);

            reply = gson.toJson(historicInflow);
            memcacheService.put(memcacheKey, reply);
        }

        response.getWriter().println(reply);
    }

    private Map<String, List<Double>> getHistoricInflow(final Vector<MonthlyInflow> allMonthlyInflows) {

        final Map<String, List<Double>> historicInflow = (Map<String, List<Double>>) gson.fromJson(DEFAULT_HISTORIC_INFLOW, Map.class);

        allMonthlyInflows.sort((monthlyInflowLeft, monthlyInflowRight) -> {
            final int monthIndexLeft = monthlyInflowLeft.getPeriod().ordinal() < 3 ? monthlyInflowLeft.getPeriod().ordinal() + 10 : monthlyInflowLeft.getPeriod().ordinal() - 3;
            final int monthIndexRight = monthlyInflowRight.getPeriod().ordinal() < 3 ? monthlyInflowRight.getPeriod().ordinal() + 10 : monthlyInflowRight.getPeriod().ordinal() - 3;
            return monthlyInflowLeft.getYear() == monthlyInflowRight.getYear() ?
                    monthIndexLeft - monthIndexRight :
                    monthlyInflowLeft.getYear() - monthlyInflowRight.getYear();
        });

        final int yearNow = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
        for(int year = 2019; year <= yearNow; year++) {
            final List<Double> values = new Vector<>();
            boolean found = false;
            for(final Period period : orderedPeriods) {
                // get selected monthly inflow
                for(final MonthlyInflow monthlyInflow : allMonthlyInflows) {
                    if(monthlyInflow.getYear() == year && monthlyInflow.getPeriod() == period) { // found
                        if(period == Period.AUGUST_AND_SEPTEMBER) {
                            values.add(monthlyInflow.getInflowInMCM() / 2d); // add as 2 months, half the inflow
                            values.add(monthlyInflow.getInflowInMCM() / 2d);
                        } else {
                            values.add(monthlyInflow.getInflowInMCM()); // else add normally
                        }
                        found = true; // either eay it was found
                    }
                }
                if(!found) {
                    // terminate loop
                    break;
                }
            }
            historicInflow.put(Integer.toString(year), values);
        }

        return historicInflow;
    }

    public static final String DEFAULT_HISTORIC_INFLOW =
            "{\n" +
            "  \"2008\": [2.634, 5.179, 2.848, 0.926, 0.133, 0.002, 0, 0.042, 0.042, 0.227, 0.635, 3.151],\n" +
            "  \"2009\": [13.248, 28.622, 27.17, 14.547, 6.889, 1.627, 0.096, 0.6, 0.599, 1.16, 2.523, 23.111],\n" +
            "  \"2010\": [42.973, 37.708, 21.849, 6.546, 2.914, 0.921, 0.482, 0, 0, 0.065, 0.128, 5.09 ],\n" +
            "  \"2011\": [7.627, 12.834, 21.389, 10.193, 4.927, 0.958, 0.03, 0.166, 0.166, 0.308, 1.482, 5.769 ],\n" +
            "  \"2012\": [92.634, 41.536, 29.378, 11.391, 6.996, 1.513, 0.432, 0.157, 0.158, 0.748, 3.182, 50.878 ],\n" +
            "  \"2013\": [13.246, 9.267, 6.497, 6.077, 2.876, 0.462, 0.101, 0.272, 0.273, 0.384, 0.672, 1.669 ],\n" +
            "  \"2014\": [1.963, 2.251, 1.964, 0.712, 1.853, 0.741, 0, 0.047, 0.047, 0.315, 0.915, 2.14 ],\n" +
            "  \"2015\": [38.354, 44.515, 17.669, 8.233, 3.137, 0.976, 0.091, 0.003, 0.004, 1.024, 0.608, 1.248 ],\n" +
            "  \"2016\": [3.685, 2.824, 6.132, 1.314, 0.961, 0.105, 0, 0.003, 0.003, 0.247, 0.657, 7.424 ],\n" +
            "  \"2017\": [21.083, 4.181, 8.891, 4.398, 1.78, 0.228, 0, 0, 0, 0.142, 0.614, 0.881 ],\n" +
            "  \"2018\": [20.661, 9.528, 5.944, 2.176, 2.802, 2.022, 0.05, 0.038, 0.039, 0.858, 0.757, 16.665 ]\n" +
            "}";
}