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

import io.github.nearchos.water.data.DayStatistics;

import java.io.IOException;

public class LocalTest {

    public static final String TEST_URL_2018_08_16 = "http://www.cyprus.gov.cy/moa/wdd/WDD.nsf/all/BE34CE00F9323F0EC2257DD3003B2AEF/$file/16-AUG-2018-UK.xlsx?openelement";
    public static final String TEST_URL_2018_08_31 = "http://www.cyprus.gov.cy/moa/wdd/WDD.nsf/all/BE34CE00F9323F0EC2257DD3003B2AEF/$file/31-8-2018UK.xls?openelement";
    public static final String TEST_URL_2018_10_04 = "http://www.moa.gov.cy/moa/wdd/wdd.nsf/all/393B6248A8F87D91C22581CD002D5DAB/$file/2-10-2018GR.xls?openelement";
    public static final String TEST_URL_2018_12_20 = "http://www.moa.gov.cy/moa/wdd/wdd.nsf/393B6248A8F87D91C22581CD002D5DAB/$file/20-12-2018GR.xls";
    public static final String TEST_URL_2019_01_21 = "http://www.moa.gov.cy/moa/wdd/wdd.nsf/393B6248A8F87D91C22581CD002D5DAB/$file/21-1-2019GR.xls";

    public static void main(String[] args) {
//        testPOI_XLSX(TEST_URL_2018_08_16);
//        testPOI_XLSX(TEST_URL_2018_08_31);
//        testPOI_XLSX(TEST_URL_2018_10_04);
//        testPOI_XLSX(TEST_URL_2018_12_20);
        testPOI_XLSX(TEST_URL_2019_01_21);
    }

    private static void testPOI_XLSX(final String url) {
        try {
            final org.apache.poi.ss.usermodel.Workbook workbook = Util.doRequestXls(url);
            System.out.println("workbook: " + workbook);
            final DayStatistics dayStatistics = Util.getDayStatistics(workbook);
            System.out.println("dayStatistics:" + dayStatistics);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}