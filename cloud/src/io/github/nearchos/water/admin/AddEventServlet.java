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

package io.github.nearchos.water.admin;

import io.github.nearchos.water.data.DatastoreHelper;
import io.github.nearchos.water.data.Event;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class AddEventServlet extends HttpServlet {

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    final static Logger log = Logger.getLogger("cyprus-water");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            final String nameEn = request.getParameter("nameEn");
            final String nameEl = request.getParameter("nameEl");
            final String typeS = request.getParameter("type");
            Event.Type type = Event.Type.UNSPECIFIED;
            try {
                type = Event.Type.valueOf(typeS);
            } catch (IllegalArgumentException iae) {
                // ignore
                log.severe(iae.getMessage());
            }
            final String description = request.getParameter("description");
            final String fromS = request.getParameter("from");
            final String untilS = request.getParameter("until");
            final Date from = SIMPLE_DATE_FORMAT.parse(fromS);
            final Date until = SIMPLE_DATE_FORMAT.parse(untilS);

            final Event event = new Event(nameEn, nameEl, type, description, from.getTime(), until.getTime());
            DatastoreHelper.addEvent(event);

            response.sendRedirect("/admin/events");
        } catch (ParseException pe) {
            throw new IOException(pe);
        }
    }
}
