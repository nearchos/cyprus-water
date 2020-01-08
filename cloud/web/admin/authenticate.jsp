<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.users.UserService" %>

    <h1> Code Cyprus - Treasure Hunt </h1>
<%
    final UserService userService = UserServiceFactory.getUserService();
    final String email = userService.getCurrentUser() == null ? "Unknown" : userService.getCurrentUser().getEmail();
    final String nickname = userService.getCurrentUser() == null ? "Nickname" : userService.getCurrentUser().getNickname();
    boolean isAdmin = false;
    if (userService.getCurrentUser() == null) {
%>
    <p>You need to <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">sign in</a> to use this service.</p>
<%
    } else {
        isAdmin = "nearchos@gmail.com".equals(email);
%>
    <p> Signed in as: <%= nickname %> <b> <%= isAdmin ? "(admin)" : "" %> </b> [<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>]</p>
<%
    }
%>