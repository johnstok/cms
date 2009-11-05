<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.UUID" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@page isErrorPage="true" %>
<%
    if(!response.isCommitted()) {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    UUID errorId = UUID.randomUUID() ;
    Logger log = Logger.getLogger("error_jsp");
    log.warn(
            "Error executing servlet request."
            + "\n\t"+ccc.remoting.actions.SessionKeys.EXCEPTION_CODE + " = " + errorId
            + "\n\tresponse.committed = " + response.isCommitted()
            + "\n\tjavax.servlet.error.status_code = "
                + request.getAttribute("javax.servlet.error.status_code")
            + "\n\tjavax.servlet.error.request_uri = "
                + request.getAttribute("javax.servlet.error.request_uri")
            + "\n\tjavax.servlet.error.servlet_name = "
                + request.getAttribute("javax.servlet.error.servlet_name")
            + "\n\tjavax.servlet.error.message = "
                + request.getAttribute("javax.servlet.error.message")
            + "\n\tjavax.servlet.error.exception_type = "
                + request.getAttribute("javax.servlet.error.exception_type"),
            exception);
%>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Error</title>
    </head>

    <body id="errorPage">
        <div id="errorContainer">
            <div id="errorHeader">&nbsp;</div>
            <h1>An internal error occurred.</h1>
            <p>Error code: <%= errorId %></p>
            <div id="errorFooter">&nbsp;</div>
        </div>
    </body>
    
</html>
