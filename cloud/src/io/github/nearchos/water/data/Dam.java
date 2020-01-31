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

import java.io.Serializable;

public class Dam implements Serializable {

    public static final long ONE_MILLION = 1000000L;

    private String nameEn;
    private String nameEl;
    private int yearOfConstruction;
    private int height;
    private long capacity;
    private double lat;
    private double lng;
    private String riverNameEl;
    private String typeEl;
    private String imageUrl;
    private String wikipediaUrl;

    public Dam(String nameEn, String nameEl, int yearOfConstruction, int height, long capacity, double lat, double lng, String riverNameEl, String typeEl, String imageUrl, String wikipediaUrl) {
        this.nameEn = nameEn;
        this.nameEl = nameEl;
        this.yearOfConstruction = yearOfConstruction;
        this.height = height;
        this.capacity = capacity;
        this.lat = lat;
        this.lng = lng;
        this.riverNameEl = riverNameEl;
        this.typeEl = typeEl;
        this.imageUrl = imageUrl;
        this.wikipediaUrl = wikipediaUrl;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getNameEl() {
        return nameEl;
    }

    public int getYearOfConstruction() {
        return yearOfConstruction;
    }

    public int getHeight() {
        return height;
    }

    public long getCapacity() {
        return capacity;
    }

    public double getCapacityInMCM() {
        return 1D * capacity / ONE_MILLION;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getRiverNameEl() {
        return riverNameEl;
    }

    public String getTypeEl() {
        return typeEl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getWikipediaUrl() {
        return wikipediaUrl;
    }

    @Override
    public String toString() {
        return "Dam{" +
                "nameEn='" + nameEn + '\'' +
                ", nameEl='" + nameEl + '\'' +
                ", yearOfConstruction=" + yearOfConstruction +
                ", height=" + height +
                ", capacity=" + capacity +
                ", lat=" + lat +
                ", lng=" + lng +
                ", riverNameEl='" + riverNameEl + '\'' +
                ", typeEl='" + typeEl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", wikipediaUrl='" + wikipediaUrl + '\'' +
                '}';
    }
}