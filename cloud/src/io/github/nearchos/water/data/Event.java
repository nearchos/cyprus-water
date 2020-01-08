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

package io.github.nearchos.water.data;

import java.io.Serializable;

public class Event implements Serializable {

    public enum Type { UNSPECIFIED, WEATHER, DAM_OVERFLOW }

    public static final String NO_DESCRIPTION = "No description";

    private String nameEn;
    private String nameEl;
    private Type type;
    private String description;
    private long from;
    private long until;

    public Event(String nameEn, String nameEl, long from, long until) {
        this(nameEn, nameEl, NO_DESCRIPTION, from, until);
    }

    public Event(String nameEn, String nameEl, String description, long from, long until) {
        this(nameEn, nameEl, Type.UNSPECIFIED, description, from, until);
    }

    public Event(String nameEn, String nameEl, Type type, String description, long from, long until) {
        this.nameEn = nameEn;
        this.nameEl = nameEl;
        this.type = type;
        this.description = description;
        this.from = from;
        this.until = until;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getNameEl() {
        return nameEl;
    }

    public Type getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public long getFrom() {
        return from;
    }

    public long getUntil() {
        return until;
    }
}