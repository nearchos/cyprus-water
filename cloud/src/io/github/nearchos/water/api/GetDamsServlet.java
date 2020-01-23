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

package io.github.nearchos.water.api;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.nearchos.water.data.Dam;
import io.github.nearchos.water.data.DatastoreHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Vector;

public class GetDamsServlet extends HttpServlet {

    public static final String MEM_CACHE_KEY_DAMS = "dams-data";

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    private final MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        if(memcacheService.contains(MEM_CACHE_KEY_DAMS)) {
            final String damsJson = (String) memcacheService.get(MEM_CACHE_KEY_DAMS);
            response.getWriter().println(damsJson); // write the response
        } else {
            final Vector<Dam> dams = DatastoreHelper.getDams();
            if (dams.isEmpty()) {
                final Message errorMessage = Message.getErrorMessage("Hmmm, missing dams data. Please contact the developer.");
                response.getWriter().println(gson.toJson(errorMessage));
            } else {
                final String damsJson = gson.toJson(dams.toArray());
                // store the result in memcache
                memcacheService.put(MEM_CACHE_KEY_DAMS, damsJson);
                // return the result as JSON
                response.getWriter().println(gson.toJson(damsJson));
            }
        }
    }

    public static void main(String[] args) {
        final Vector<Dam> dams = DatastoreHelper.getDams();
        System.out.println(dams);
        System.out.println(gson.toJson(dams));
    }
}