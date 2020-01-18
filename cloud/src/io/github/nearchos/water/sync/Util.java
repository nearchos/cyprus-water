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

import io.github.nearchos.water.data.Data;
import io.github.nearchos.water.data.DayStatistics;
import io.github.nearchos.water.data.MonthlyInflow;
import io.github.nearchos.water.data.Period;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Logger;

public class Util {

    private static final Logger log = Logger.getLogger("cyprus-water");

    private enum FileFormat { HSSF, XSSF }

    static Workbook doRequestXls(final String urlString) throws IOException {
        final URL url = new URL(urlString);
        String encodedUrl;
        try {
            final URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            encodedUrl = uri.toASCIIString();
        } catch (URISyntaxException urise) {
            throw new IOException(urise);
        }

        try {
            return doRequestXls(encodedUrl, FileFormat.XSSF);
        } catch (OLE2NotOfficeXmlFileException e) {
            return doRequestXls(encodedUrl, FileFormat.HSSF);
        }
    }

    private static Workbook doRequestXls(final String url, final FileFormat fileFormat) throws IOException {
        final URL requestUrl = new URL(url);
        final HttpURLConnection httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Content-Type", "application/vnd.ms-excel");

        int responseCode = httpURLConnection.getResponseCode();
        if(responseCode != 200) {
            log.severe("RequestServlet @ '" + url + "' produced response code: " + responseCode);
            throw new IOException("HTTP (XML) response code: " + responseCode);
        }

        return getWorkbook(httpURLConnection.getInputStream(), fileFormat);
    }

//    private static DayStatistics getDayStatistics(final InputStream inputStream, final FileFormat fileFormat) throws IOException {
//        final Workbook workbook = getWorkbook(inputStream, fileFormat);
//        return getDayStatistics(workbook);
//    }

    static Workbook getWorkbook(final InputStream inputStream, final FileFormat fileFormat) throws IOException {
        switch (fileFormat) {
            case HSSF:
                return new HSSFWorkbook(inputStream);
            case XSSF:
                return new XSSFWorkbook(inputStream);
            default:
                throw new RuntimeException("Unknown file format: " + fileFormat);
        }
    }

    private static final Vector<String> damNamesEn = new Vector<>(Arrays.asList(Data.DAM_NAMES_EN));
    private static final Vector<String> damNamesEl = new Vector<>(Arrays.asList(Data.DAM_NAMES_EL));

    static DayStatistics getDayStatistics(final Workbook workbook) throws IOException {
        Date date;
        Map<String,Double> mapStorage = new HashMap<>();
        Map<String,Double> mapInflow = new HashMap<>();

        try {
            final Sheet sheet = workbook.getSheetAt(0);
            try {
                date = sheet.getRow(9).getCell(11).getDateCellValue();
            } catch (IllegalStateException ise) {
                date = new Date();
            }
            for(int j = 16; j < 41; j++) {
                final String damName = getDamNameFrmEnglishOrGreek(sheet.getRow(j).getCell(1).getStringCellValue().trim());
                {
                    double damStorage = sheet.getRow(j).getCell(7).getNumericCellValue();
                    if(!damName.isEmpty() && damNamesEn.contains(damName)) {
                        mapStorage.put(damName, damStorage);
                    }
                }
                {
                    double damInflow = sheet.getRow(j).getCell(5).getNumericCellValue();
                    if(!damName.isEmpty() && damNamesEn.contains(damName)) {
                        mapInflow.put(damName, damInflow);
                    }
                }
            }
        } catch (RuntimeException re) {
            throw new IOException(re);
        }

        return new DayStatistics(date, mapStorage, mapInflow);
    }

    static Vector<MonthlyInflow> getMonthlyInflows(final Workbook workbook) throws IOException {

        final Vector<MonthlyInflow> monthlyInflows = new Vector<>();

        final Period [] allPeriods = Period.values();

        // current year
        final LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        final int yearNow = localDate.getYear();
        final int monthValueNow = localDate.getMonthValue(); // 1..12

        try {
            final Sheet sheet = workbook.getSheetAt(0);

            // identify year
            final Cell headerCell = sheet.getRow(48).getCell(2);
            final String years = headerCell.toString();
            int currentYear = 2000 + Integer.parseInt(years.substring(0, 2));

            for(int c = 2; c <= 12; c++) {
                for(int r = 49; r <= 59; r++) {
                    final Period period = allPeriods[r-49];
                    final int periodOrdinal = period.ordinal();
                    final int correspondingMonthValue = periodOrdinal < 3 ? periodOrdinal + 11 : periodOrdinal - 2; // 1..12
                    if(period == Period.JANUARY) currentYear++;
                    final Cell cell = sheet.getRow(r).getCell(c);
                    final double inflowInMCM = cell.getNumericCellValue();
                    final MonthlyInflow monthlyInflow = new MonthlyInflow(currentYear, period, inflowInMCM);
                    final boolean examinedPeriodIsInThePresentOrPast = currentYear < yearNow || (currentYear == yearNow && correspondingMonthValue <= monthValueNow);
                    if(examinedPeriodIsInThePresentOrPast) {
                        monthlyInflows.add(monthlyInflow);
                    }
                }
            }

        } catch (RuntimeException re) {
            throw new IOException(re);
        }

        return monthlyInflows;
    }

    private static String getDamNameFrmEnglishOrGreek(final String damNameInEnOrEl) {
        if(damNamesEn.contains(damNameInEnOrEl)) return damNameInEnOrEl;
        else { // look it up if it's in Greek
            final int index = damNamesEl.indexOf(damNameInEnOrEl);
            if(index >= 0) return damNamesEn.get(index);
            else return damNameInEnOrEl;
        }
    }
}