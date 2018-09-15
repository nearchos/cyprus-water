package io.github.nearchos.water.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetHistoricInflowServlet extends HttpServlet {

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        response.getWriter().println(DEFAULT_HISTORIC_INFLOW);
    }

    public static final String DEFAULT_HISTORIC_INFLOW =
            "{\n" +
                    "  \"2008\": [2.634, 5.179, 2.848, 0.926, 0.133, 0.002, 0, 0.084, 0.227, 0.635, 3.151],\n" +
                    "  \"2009\": [13.248, 28.622, 27.17, 14.547, 6.889, 1.627, 0.096, 1.199, 1.16, 2.523, 23.111],\n" +
                    "  \"2010\": [42.973, 37.708, 21.849, 6.546, 2.914, 0.921, 0.482, 0, 0.065, 0.128, 5.09 ],\n" +
                    "  \"2011\": [7.627, 12.834, 21.389, 10.193, 4.927, 0.958, 0.03, 0.332, 0.308, 1.482, 5.769 ],\n" +
                    "  \"2012\": [92.634, 41.536, 29.378, 11.391, 6.996, 1.513, 0.432, 0.315, 0.748, 3.182, 50.878 ],\n" +
                    "  \"2013\": [13.246, 9.267, 6.497, 6.077, 2.876, 0.462, 0.101, 0.545, 0.384, 0.672, 1.669 ],\n" +
                    "  \"2014\": [1.963, 2.251, 1.964, 0.712, 1.853, 0.741, 0, 0.094, 0.315, 0.915, 2.14 ],\n" +
                    "  \"2015\": [38.354, 44.515, 17.669, 8.233, 3.137, 0.976, 0.091, 0.007, 1.024, 0.608, 1.248 ],\n" +
                    "  \"2016\": [3.685, 2.824, 6.132, 1.314, 0.961, 0.105, 0, 0.006, 0.247, 0.657, 7.424 ],\n" +
                    "  \"2017\": [21.083, 4.181, 8.891, 4.398, 1.78, 0.228, 0, 0, 0.142, 0.614, 0.881 ],\n" +
                    "  \"2018\": [20.661, 9.528, 5.944, 2.176, 2.802, 2.022, 0.05, 0.037 ]\n" +
                    "}";
}
