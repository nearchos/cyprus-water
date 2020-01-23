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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;

public class Data {

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    public static final String [] DAM_NAMES_EN = new String [] {
            "Achna",
            "Agia Marina",
            "Argaka",
            "Arminou",
            "Asprokremmos",
            "Dipotamos",
            "Evretou",
            "Germasoyeia",
            "Kalavasos",
            "Kalopanagiotis",
            "Kannaviou",
            "Kouris",
            "Lefkara",
            "Mavrokolympos",
            "Polemidia",
            "Pomos",
            "Vyzakia",
            "Xyliatos"
    };

    public static final String [] DAM_NAMES_EL = new String [] {
            "Αχνα",
            "Αγία Μαρίνα",
            "Αργάκα",
            "Αρμίνου",
            "Ασπρόκρεμμος",
            "Διπόταμος",
            "Ευρέτου",
            "Γερμασόγεια",
            "Καλαβασός",
            "Καλοπαναγιώτης",
            "Κανναβιού",
            "Κούρης",
            "Λεύκαρα",
            "Μαυροκόλυμπος",
            "Πολεμίδια",
            "Πωμός",
            "Βυζακιά",
            "Ξυλιάτος"
    };

    public static Vector<Dam> getDams() {
        // first parse the JSON to produce an array of dams
        final Dam [] allDamsArray = gson.fromJson(DAMS_JSON, Dam[].class);
        // then package the array in a vector and return it
        return new Vector<>(Arrays.asList(allDamsArray));
    }

    public static final String DAMS_JSON =
            "[\n" +
            "  {\n" +
            "    \"nameEn\": \"Kouris\",\n" +
            "    \"nameEl\": \"Κούρης\",\n" +
            "    \"yearOfConstruction\": 1988,\n" +
            "    \"height\": 110,\n" +
            "    \"capacity\": 115000000,\n" +
            "    \"lat\": 34.72779846191406,\n" +
            "    \"lng\": 32.91780090332031,\n" +
            "    \"riverNameEl\": \"Κούρης\",\n" +
            "    \"typeEl\": \"Xωμάτινο\",\n" +
            "    \"imageUrl\": \"http://www.cyprus.gov.cy/moa/wdd/wdd.nsf/All/6E89F8FECBC501E4C2256F100031C213/$file/99-Kouris_500.jpg\",\n" +
            "    \"wikipediaUrl\": \"https://en.wikipedia.org/wiki/Kouris_Dam\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"nameEn\": \"Asprokremmos\",\n" +
            "    \"nameEl\": \"Ασπρόκρεμμος\",\n" +
            "    \"yearOfConstruction\": 1982,\n" +
            "    \"height\": 53,\n" +
            "    \"capacity\": 52375000,\n" +
            "    \"lat\": 34.7256965637207,\n" +
            "    \"lng\": 32.555015563964844,\n" +
            "    \"riverNameEl\": \"Ξερός Ποταμός\",\n" +
            "    \"typeEl\": \"Xωμάτινο\",\n" +
            "    \"imageUrl\": \"http://www.cyprus.gov.cy/moa/wdd/wdd.nsf/All/6E89F8FECBC501E4C2256F100031C213/$file/81-Asprokremos_500.jpg\",\n" +
            "    \"wikipediaUrl\": \"https://en.wikipedia.org/wiki/Asprokremmos_Dam\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"nameEn\": \"Evretou\",\n" +
            "    \"nameEl\": \"Ευρέτου\",\n" +
            "    \"yearOfConstruction\": 1986,\n" +
            "    \"height\": 70,\n" +
            "    \"capacity\": 24000000,\n" +
            "    \"lat\": 34.975826263427734,\n" +
            "    \"lng\": 32.47270202636719,\n" +
            "    \"riverNameEl\": \"Σταυρός της Ψώκας\",\n" +
            "    \"typeEl\": \"Λιθόρριπτο\",\n" +
            "    \"imageUrl\": \"http://www.cyprus.gov.cy/moa/wdd/wdd.nsf/All/6E89F8FECBC501E4C2256F100031C213/$file/96-Evretou_500.jpg\",\n" +
            "    \"wikipediaUrl\": \"https://en.wikipedia.org/wiki/Evretou_Dam\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"nameEn\": \"Kannaviou\",\n" +
            "    \"nameEl\": \"Κανναβιού\",\n" +
            "    \"yearOfConstruction\": 2005,\n" +
            "    \"height\": 75,\n" +
            "    \"capacity\": 17168000,\n" +
            "    \"lat\": 34.92770004272461,\n" +
            "    \"lng\": 32.587799072265625,\n" +
            "    \"riverNameEl\": \"Έζουσα\",\n" +
            "    \"typeEl\": \"Xωμάτινο/Λιθόρριπτο\",\n" +
            "    \"imageUrl\": \"http://www.cyprus.gov.cy/moa/wdd/wdd.nsf/All/339F46EA7BDFB08FC22575370028F67C/$file/Pict17.jpg\",\n" +
            "    \"wikipediaUrl\": \"\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"nameEn\": \"Kalavasos\",\n" +
            "    \"nameEl\": \"Καλαβασός\",\n" +
            "    \"yearOfConstruction\": 1985,\n" +
            "    \"height\": 60,\n" +
            "    \"capacity\": 17100000,\n" +
            "    \"lat\": 34.803714752197266,\n" +
            "    \"lng\": 33.26237106323242,\n" +
            "    \"riverNameEl\": \"Βασιλικός\",\n" +
            "    \"typeEl\": \"Λιθόρριπτο\",\n" +
            "    \"imageUrl\": \"http://www.cyprus.gov.cy/moa/wdd/wdd.nsf/All/6E89F8FECBC501E4C2256F100031C213/$file/94-kalavasos_500.jpg\",\n" +
            "    \"wikipediaUrl\": \"\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"nameEn\": \"Dipotamos\",\n" +
            "    \"nameEl\": \"Διπόταμος\",\n" +
            "    \"yearOfConstruction\": 1985,\n" +
            "    \"height\": 60,\n" +
            "    \"capacity\": 15500000,\n" +
            "    \"lat\": 34.851619720458984,\n" +
            "    \"lng\": 33.359275817871094,\n" +
            "    \"riverNameEl\": \"Πεντάσχοινος\",\n" +
            "    \"typeEl\": \"Λιθόρριπτο\",\n" +
            "    \"imageUrl\": \"http://www.cyprus.gov.cy/moa/wdd/wdd.nsf/All/6E89F8FECBC501E4C2256F100031C213/$file/95-Dhypotamos_500.jpg\",\n" +
            "    \"wikipediaUrl\": \"\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"nameEn\": \"Lefkara\",\n" +
            "    \"nameEl\": \"Λεύκαρα\",\n" +
            "    \"yearOfConstruction\": 1973,\n" +
            "    \"height\": 71,\n" +
            "    \"capacity\": 13850000,\n" +
            "    \"lat\": 34.89440155029297,\n" +
            "    \"lng\": 33.29560089111328,\n" +
            "    \"riverNameEl\": \"Συργάτης (Πεντάσχοινος)\",\n" +
            "    \"typeEl\": \"Xωμάτινο/Λιθόρριπτο\",\n" +
            "    \"imageUrl\": \"http://www.cyprus.gov.cy/moa/wdd/wdd.nsf/All/FB2F2507BB13A833C2256F0900268224/$file/64-Lefkara_500.jpg\",\n" +
            "    \"wikipediaUrl\": \"\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"nameEn\": \"Germasoyeia\",\n" +
            "    \"nameEl\": \"Γερμασόγεια\",\n" +
            "    \"yearOfConstruction\": 1968,\n" +
            "    \"height\": 49,\n" +
            "    \"capacity\": 13500000,\n" +
            "    \"lat\": 34.743900299072266,\n" +
            "    \"lng\": 33.08430099487305,\n" +
            "    \"riverNameEl\": \"Γερμασόγεια\",\n" +
            "    \"typeEl\": \"Xωμάτινο\",\n" +
            "    \"imageUrl\": \"http://www.cyprus.gov.cy/moa/wdd/wdd.nsf/All/FB2F2507BB13A833C2256F0900268224/$file/55-Yermasoyia_500.jpg\",\n" +
            "    \"wikipediaUrl\": \"\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"nameEn\": \"Achna\",\n" +
            "    \"nameEl\": \"Άχνα\",\n" +
            "    \"yearOfConstruction\": 1987,\n" +
            "    \"height\": 16,\n" +
            "    \"capacity\": 6800000,\n" +
            "    \"lat\": 35.055320739746094,\n" +
            "    \"lng\": 33.814308166503906,\n" +
            "    \"riverNameEl\": \"Εξωποτάμιο φράγμα\",\n" +
            "    \"typeEl\": \"Xωμάτινο\",\n" +
            "    \"imageUrl\": \"http://www.cyprus.gov.cy/moa/wdd/wdd.nsf/All/6E89F8FECBC501E4C2256F100031C213/$file/97-Axna_500.jpg\",\n" +
            "    \"wikipediaUrl\": \"\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"nameEn\": \"Arminou\",\n" +
            "    \"nameEl\": \"Αρμίνου\",\n" +
            "    \"yearOfConstruction\": 1998,\n" +
            "    \"height\": 45,\n" +
            "    \"capacity\": 4300000,\n" +
            "    \"lat\": 34.87519836425781,\n" +
            "    \"lng\": 32.737098693847656,\n" +
            "    \"riverNameEl\": \"Διάριζος\",\n" +
            "    \"typeEl\": \"Xωμάτινο/Λιθόρριπτο\",\n" +
            "    \"imageUrl\": \"http://www.cyprus.gov.cy/moa/wdd/wdd.nsf/All/6E89F8FECBC501E4C2256F100031C213/$file/104-Arminou_500.jpg\",\n" +
            "    \"wikipediaUrl\": \"\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"nameEn\": \"Polemidia\",\n" +
            "    \"nameEl\": \"Πολεμίδια\",\n" +
            "    \"yearOfConstruction\": 1965,\n" +
            "    \"height\": 45,\n" +
            "    \"capacity\": 3400000,\n" +
            "    \"lat\": 34.71870040893555,\n" +
            "    \"lng\": 32.988800048828125,\n" +
            "    \"riverNameEl\": \"Γαρύλλης\",\n" +
            "    \"typeEl\": \"Xωμάτινο\",\n" +
            "    \"imageUrl\": \"http://www.cyprus.gov.cy/moa/wdd/wdd.nsf/All/CAEB057DDF020888C2256F090022C5D4/$file/41-Polemidhia_500.jpg\",\n" +
            "    \"wikipediaUrl\": \"\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"nameEn\": \"Mavrokolympos\",\n" +
            "    \"nameEl\": \"Μαυροκόλυμπος\",\n" +
            "    \"yearOfConstruction\": 1966,\n" +
            "    \"height\": 45,\n" +
            "    \"capacity\": 2180000,\n" +
            "    \"lat\": 34.85649871826172,\n" +
            "    \"lng\": 32.405799865722656,\n" +
            "    \"riverNameEl\": \"Μαυροκόλυμπος\",\n" +
            "    \"typeEl\": \"Xωμάτινο\",\n" +
            "    \"imageUrl\": \"http://www.cyprus.gov.cy/moa/wdd/wdd.nsf/All/CAEB057DDF020888C2256F090022C5D4/$file/44-Mavrokolympos_500.jpg\",\n" +
            "    \"wikipediaUrl\": \"\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"nameEn\": \"Vyzakia\",\n" +
            "    \"nameEl\": \"Βυζακιά\",\n" +
            "    \"yearOfConstruction\": 1994,\n" +
            "    \"height\": 37,\n" +
            "    \"capacity\": 1690000,\n" +
            "    \"lat\": 35.0616340637207,\n" +
            "    \"lng\": 33.02715301513672,\n" +
            "    \"riverNameEl\": \"Εξωποτάμιο φράγμα\",\n" +
            "    \"typeEl\": \"Xωμάτινο\",\n" +
            "    \"imageUrl\": \"http://www.cyprus.gov.cy/moa/wdd/wdd.nsf/All/6E89F8FECBC501E4C2256F100031C213/$file/100-Vizakia_500.jpg\",\n" +
            "    \"wikipediaUrl\": \"\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"nameEn\": \"Xyliatos\",\n" +
            "    \"nameEl\": \"Ξυλιάτος\",\n" +
            "    \"yearOfConstruction\": 1982,\n" +
            "    \"height\": 42,\n" +
            "    \"capacity\": 1430000,\n" +
            "    \"lat\": 35.008968353271484,\n" +
            "    \"lng\": 33.037330627441406,\n" +
            "    \"riverNameEl\": \"Λαγουδερά (Ελιά)\",\n" +
            "    \"typeEl\": \"Λιθόρριπτο\",\n" +
            "    \"imageUrl\": \"http://www.cyprus.gov.cy/moa/wdd/wdd.nsf/All/6E89F8FECBC501E4C2256F100031C213/$file/82-Xyliatos_500.jpg\",\n" +
            "    \"wikipediaUrl\": \"\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"nameEn\": \"Argaka\",\n" +
            "    \"nameEl\": \"Αργάκα\",\n" +
            "    \"yearOfConstruction\": 1964,\n" +
            "    \"height\": 41,\n" +
            "    \"capacity\": 990000,\n" +
            "    \"lat\": 35.048606872558594,\n" +
            "    \"lng\": 32.50206756591797,\n" +
            "    \"riverNameEl\": \"Μακούντα\",\n" +
            "    \"typeEl\": \"Λιθόρριπτο\",\n" +
            "    \"imageUrl\": \"http://www.cyprus.gov.cy/moa/wdd/wdd.nsf/All/CAEB057DDF020888C2256F090022C5D4/$file/30-Argaka_500.jpg\",\n" +
            "    \"wikipediaUrl\": \"\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"nameEn\": \"Pomos\",\n" +
            "    \"nameEl\": \"Πομός\",\n" +
            "    \"yearOfConstruction\": 1966,\n" +
            "    \"height\": 38,\n" +
            "    \"capacity\": 860000,\n" +
            "    \"lat\": 35.14495849609375,\n" +
            "    \"lng\": 32.57540512084961,\n" +
            "    \"riverNameEl\": \"Λειβάδι\",\n" +
            "    \"typeEl\": \"Λιθόρριπτο\",\n" +
            "    \"imageUrl\": \"http://www.cyprus.gov.cy/moa/wdd/wdd.nsf/All/CAEB057DDF020888C2256F090022C5D4/$file/45-Pomos_500.jpg\",\n" +
            "    \"wikipediaUrl\": \"\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"nameEn\": \"Kalopanagiotis\",\n" +
            "    \"nameEl\": \"Καλοπαναγιώτης\",\n" +
            "    \"yearOfConstruction\": 1966,\n" +
            "    \"height\": 40,\n" +
            "    \"capacity\": 363000,\n" +
            "    \"lat\": 35.006126403808594,\n" +
            "    \"lng\": 32.825435638427734,\n" +
            "    \"riverNameEl\": \"Σέτραχος (Μαραθάσας)\",\n" +
            "    \"typeEl\": \"Xωμάτινο\",\n" +
            "    \"imageUrl\": \"http://www.cyprus.gov.cy/moa/wdd/wdd.nsf/All/CAEB057DDF020888C2256F090022C5D4/$file/43-Kalopanayiotis_500.jpg\",\n" +
            "    \"wikipediaUrl\": \"\"\n" +
            "  }\n" +
            "]";

    public static void main(String[] args) {
        try {
            File file = new File("C:\\Users\\Nearchos\\Desktop\\Dams1.csv");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            boolean firstLine = true;
            final Vector<Dam> dams = new Vector<>();
            while ((line = bufferedReader.readLine()) != null) {
                if(firstLine) { // skip first line
                    firstLine = false;
                    continue;
                }
                // handle line
                StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                while(stringTokenizer.hasMoreTokens()) {
                    final String nameEl = stringTokenizer.nextToken().trim();
                    final String nameEn = stringTokenizer.nextToken().trim();
                    final String imageUrl = stringTokenizer.nextToken().trim();
                    final String wikipediaUrl = stringTokenizer.nextToken().trim();
                    final float lat = Float.parseFloat(stringTokenizer.nextToken());
                    final float lng = Float.parseFloat(stringTokenizer.nextToken());
                    final int yearBuilt = Integer.parseInt(stringTokenizer.nextToken());
                    final String riverNameEl = stringTokenizer.nextToken().trim();
                    final String typeEl = stringTokenizer.nextToken().trim();
                    final int heightInMeters = Integer.parseInt(stringTokenizer.nextToken());
                    final long volumeInCubicMeters = Long.parseLong(stringTokenizer.nextToken());
                    final Dam dam = new Dam(nameEn, nameEl, yearBuilt, heightInMeters, volumeInCubicMeters, lat, lng, riverNameEl, typeEl, imageUrl, wikipediaUrl);
                    dams.add(dam);
                    System.out.println("dam: " + dam);
                    System.out.println("json: " + gson.toJson(dam));
                }
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            fileReader.close();
            System.out.println("Contents of file:");
            System.out.println(stringBuilder.toString());

            System.out.println("dams...\n" + gson.toJson(dams));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
