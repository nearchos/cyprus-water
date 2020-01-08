<%--
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
  Time: 6:18 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cyprus Water - Admin</title>
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
%>
    <a href="/admin/events">Events</a>
<%
    }
%>

</body>
</html>
