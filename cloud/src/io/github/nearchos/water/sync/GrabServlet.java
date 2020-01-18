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

package io.github.nearchos.water.sync;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import io.github.nearchos.water.data.DatastoreHelper;
import io.github.nearchos.water.data.DayStatistics;
import io.github.nearchos.water.data.MonthlyInflow;
import io.github.nearchos.water.data.Period;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Vector;

public class GrabServlet extends HttpServlet {

//    private static final Logger log = Logger.getLogger("cyprus-water");

//    private static final Gson gson = new Gson();

    private final MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();

    //    private static final String WDD_ROOT = "http://www.cyprus.gov.cy/moa/wdd/WDD.nsf/reservoir_en/reservoir_en?OpenDocument";
    private static final String WDD_ROOT = "http://www.moa.gov.cy/moa/wdd/wdd.nsf/page18_gr/page18_gr?opendocument";

    //    private String DOCUMENT_URL_PREFIX = "http://www.cyprus.gov.cy/moa/wdd/WDD.nsf";
//    private static String DOCUMENT_URL_PREFIX = "http://www.moa.gov.cy/moa/wdd/wdd.nsf";

    private static String getAbsoluteLink() throws IOException {
        // fetching and parsing root web page
        Document doc = Jsoup.connect(WDD_ROOT).get();
        // log(doc.title());
        final Elements elements = doc.select("a[href]");
        for(final Element element : elements) {
            final String link = element.attr("abs:href");
            if(link.toLowerCase().contains(".xls") || link.toLowerCase().contains(".xlsx")) {
                System.out.println("link: " + link);
                final String absoluteLink = link;
                System.out.println("absoluteLink: " + absoluteLink);
                return absoluteLink;
            }
        }
        throw new RuntimeException("Could not find XLS or XLSX link!"); // todo send an email to notify me?
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        final String absoluteLink = getAbsoluteLink();
        log("fetching XLS from absoluteLink: "  + absoluteLink);

        // fetching and processing XLS
        final org.apache.poi.ss.usermodel.Workbook workbook = getWorkbook(absoluteLink);
        final DayStatistics dayStatistics = getDayStatistics(workbook);
        final String date = dayStatistics.getDateAsString();

        // check if given object is already in datastore - if non-null, then set date already exists in datastore
        final PrintWriter printWriter = response.getWriter();
        final DayStatistics latestDayStatistics = DatastoreHelper.getDayStatistics(date);
        if(latestDayStatistics == null || !latestDayStatistics.getDateAsString().equals(dayStatistics.getDateAsString())) {
            DatastoreHelper.addDayStatistics(dayStatistics);
            printWriter.println("Added dayStatistics for: " + date);
            log("Added dayStatistics for: " + date + " --> " + dayStatistics);
            // invalidate possible memcache entry for timeseries
            final String memcacheKey = "timeseries-" + DayStatistics.SIMPLE_DATE_FORMAT.format(new Date());
            memcacheService.delete(memcacheKey);
        } else {
            printWriter.println("Skipped as dayStatistics already in datastore for: " + date + "<br>");
            log("Skipped as dayStatistics already in datastore for: " + date);
        }

        final Vector<MonthlyInflow> monthlyInflows = getMonthlyInflows(workbook);

        // add all monthly inflows that are missing or that were updated
        final Vector<MonthlyInflow> existingMonthlyInflows = DatastoreHelper.getAllMonthlyInflows();

        boolean modified = false;
        for(final MonthlyInflow monthlyInflow : monthlyInflows) {
            final MonthlyInflow existingMonthlyInflow = getMonthlyInflow(monthlyInflow.getYear(), monthlyInflow.getPeriod(), existingMonthlyInflows);
            if(existingMonthlyInflow == null) {
                // add it!
                DatastoreHelper.addMonthlyInflow(monthlyInflow);
                modified = true;
            } else {
                // update a monthly inflow if modified
                if(!almostEqual(monthlyInflow.getInflowInMCM(), existingMonthlyInflow.getInflowInMCM())) {
                    DatastoreHelper.updateMonthlyInflow(monthlyInflow);
                    modified = true;
                }
            }
        }
        if(modified) {
            memcacheService.delete("monthly-inflows-" + DayStatistics.SIMPLE_DATE_FORMAT.format(new Date()));
        } else {
            printWriter.println("Skipped as monthlyInflows already in datastore for: " + date + "<br>");
            log("Skipped as dayStatistics already in datastore for: " + date);
        }
    }

    public static final double EPSILON = 0.000001; // 1 millionth

    private static boolean almostEqual(double a, double b){
        return Math.abs(a-b) < EPSILON;
    }

//    /**
//     * Check over year/period only (i.e. not value of inflowInMCM)
//     * @param monthlyInflow
//     * @param existingMonthlyInflows
//     * @return
//     */
//    private static boolean isContained(final MonthlyInflow monthlyInflow, final Vector<MonthlyInflow> existingMonthlyInflows) {
//        for(final MonthlyInflow existingMonthlyInflow : existingMonthlyInflows) {
//            if(monthlyInflow.getYear() == existingMonthlyInflow.getYear() && monthlyInflow.getPeriod() == existingMonthlyInflow.getPeriod()) return true;
//        }
//        return false;
//    }

    private static MonthlyInflow getMonthlyInflow(final int year, final Period period, final Vector<MonthlyInflow> existingMonthlyInflows) {
        for(final MonthlyInflow existingMonthlyInflow : existingMonthlyInflows) {
            if(year == existingMonthlyInflow.getYear() && period == existingMonthlyInflow.getPeriod()) return existingMonthlyInflow;
        }
        return null;
    }

    private static org.apache.poi.ss.usermodel.Workbook getWorkbook(final String absoluteLink) throws IOException {
        return Util.doRequestXls(absoluteLink);
    }

    private static DayStatistics getDayStatistics(final org.apache.poi.ss.usermodel.Workbook workbook) throws IOException {
        return Util.getDayStatistics(workbook);
    }

    private static Vector<MonthlyInflow> getMonthlyInflows(final org.apache.poi.ss.usermodel.Workbook workbook) throws IOException {
        return Util.getMonthlyInflows(workbook);
    }

    public static void main(String[] args) {
        try {
            final String absoluteLink = getAbsoluteLink();

            // fetching and processing XLS
            final org.apache.poi.ss.usermodel.Workbook workbook = getWorkbook(absoluteLink);
            final DayStatistics dayStatistics = getDayStatistics(workbook);
            final String date = dayStatistics.getDateAsString();
            System.out.println("completed for date -> " + date);

            final Vector<MonthlyInflow> monthlyInflows = getMonthlyInflows(workbook);
            System.out.println("monthlyInflows -> " + monthlyInflows);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}