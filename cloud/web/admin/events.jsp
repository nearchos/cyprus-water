<%@ page import="io.github.nearchos.water.data.DatastoreHelper" %>
<%@ page import="io.github.nearchos.water.data.Event" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="io.github.nearchos.water.admin.AddEventServlet" %><%--
  ~ Copyright (c) 2020. 
  ~
  ~ THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR
  ~  "LICENSE"). THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER
  ~   THAN AS AUTHORIZED UNDER THIS LICENSE OR COPYRIGHT LAW IS PROHIBITED.
  ~
  ~ BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO BE BOUND BY THE TERMS OF THIS
  ~  LICENSE. TO THE EXTENT THIS LICENSE MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS
  ~   CONTAINED HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
  ~
  ~ The complete license is available at https://creativecommons.org/licenses/by/3.0/legalcode
  --%>

<%--
  User: Nearchos
  Date: 07-Jan-20
  Time: 6:21 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cyprus Water - Admin Events</title>
</head>
<body>

<%@ include file="authenticate.jsp" %>

<%
    if(email == null) {
%>
You are not logged in!
<%
    } else if(!isAdmin) {
%>
You are not admin!
<%
    } else {

        final Date today = new Date();
%>

<h1>Events</h1>
<table style="border: 1px solid blue;">
    <tr>
        <th>From</th>
        <th>Until</th>
        <th>Name (EN)</th>
        <th>Name (EL)</th>
        <th>Type</th>
        <th>Description</th>
        <th></th>
    </tr>

<%
        final Vector<Event> allEvents = DatastoreHelper.getAllEvents();
        for(final Event event : allEvents) {

%>
    <tr>
        <td><%=AddEventServlet.SIMPLE_DATE_FORMAT.format(new Date(event.getFrom()))%></td>
        <td><%=AddEventServlet.SIMPLE_DATE_FORMAT.format(new Date(event.getUntil()))%></td>
        <td><%=event.getNameEn()%></td>
        <td><%=event.getNameEl()%></td>
        <td><%=event.getType()%></td>
        <td><%=event.getDescription()%></td>
        <td><u>Delete</u></td>
    </tr>
<%
        }
%>
</table>

<h2>Add new Event</h2>

<form action="/admin/add-event" method="post">
    <label>From: <input type="date" name="from" value="<%=AddEventServlet.SIMPLE_DATE_FORMAT.format(today)%>"></label>
    <br/>
    <label>Until: <input type="date" name="until" value="<%=AddEventServlet.SIMPLE_DATE_FORMAT.format(today)%>"></label>
    <br/>
    <label>Name (en): <input type="text" name="nameEn"></label>
    <br/>
    <label>Name (el): <input type="text" name="nameEl"></label>
    <br/>
    <label>Type: <input list="event-types" name="type"></label>
    <datalist id="event-types">
        <option>UNSPECIFIED</option>
        <option>WEATHER</option>
        <option>DAM_OVERFLOW</option>
    </datalist>
    <br/>
    <label>Description: <input type="text" name="description"></label>
    <br/>
    <input type="submit" name="Add">
</form>
<%
    }
%>

</body>
</html>
