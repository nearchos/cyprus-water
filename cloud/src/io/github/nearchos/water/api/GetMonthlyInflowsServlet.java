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

package io.github.nearchos.water.api;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.nearchos.water.data.DatastoreHelper;
import io.github.nearchos.water.data.DayStatistics;
import io.github.nearchos.water.data.Event;
import io.github.nearchos.water.data.MonthlyInflow;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

public class GetMonthlyInflowsServlet extends HttpServlet {

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    private final MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String reply;

        final String memcacheKey = "monthly-inflows-" + DayStatistics.SIMPLE_DATE_FORMAT.format(new Date());
        if(memcacheService.contains(memcacheKey)) {
            reply = (String) memcacheService.get(memcacheKey);
        } else {
            final Vector<MonthlyInflow> allMonthlyInflows = DatastoreHelper.getAllMonthlyInflows();
            allMonthlyInflows.sort((monthlyInflowLeft, monthlyInflowRight) -> {
                final int monthIndexLeft = monthlyInflowLeft.getPeriod().ordinal() < 3 ? monthlyInflowLeft.getPeriod().ordinal() + 10 : monthlyInflowLeft.getPeriod().ordinal() - 3;
                final int monthIndexRight = monthlyInflowRight.getPeriod().ordinal() < 3 ? monthlyInflowRight.getPeriod().ordinal() + 10 : monthlyInflowRight.getPeriod().ordinal() - 3;
                return monthlyInflowLeft.getYear() == monthlyInflowRight.getYear() ?
                        monthIndexLeft - monthIndexRight :
                        monthlyInflowLeft.getYear() - monthlyInflowRight.getYear();
            });
            reply = gson.toJson(allMonthlyInflows);
            memcacheService.put(memcacheKey, reply);
        }

        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.getOutputStream().println(reply);
    }
}
