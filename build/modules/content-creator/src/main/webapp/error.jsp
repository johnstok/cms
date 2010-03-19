<%@ page import="org.apache.log4j.Logger" %><%@ page isErrorPage="true" %><%

    Logger log = Logger.getLogger("error_jsp");
    String errorId =
        (String) request.getAttribute(
            ccc.remoting.actions.SessionKeys.EXCEPTION_CODE);

    log.warn(
            "Error executing servlet request."
            + "\n\t"+ccc.remoting.actions.SessionKeys.EXCEPTION_CODE + " = "
                + errorId
            + "\n\tjavax.servlet.error.status_code = "
                + request.getAttribute("javax.servlet.error.status_code")
            + "\n\tjavax.servlet.error.request_uri = "
                + request.getAttribute("javax.servlet.error.request_uri")
            + "\n\tjavax.servlet.error.servlet_name = "
                + request.getAttribute("javax.servlet.error.servlet_name")
            + "\n\tjavax.servlet.error.message = "
                + request.getAttribute("javax.servlet.error.message")
            + "\n\tjavax.servlet.error.exception_type = "
                + request.getAttribute("javax.servlet.error.exception_type"));
            
%><!-- An error occurred: <%= errorId %> -->
<html>

    <head>
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
