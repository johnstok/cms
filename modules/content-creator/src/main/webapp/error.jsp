<%@ page import="org.apache.log4j.Logger" %><%@ page isErrorPage="true" %><%

    Logger log = Logger.getLogger("error_jsp");
    String errorId =
        (String) request.getAttribute(
            ccc.web.SessionKeys.EXCEPTION_CODE);
    
    String WARNING =
        "Error executing servlet request."
        + "\n\t"+ccc.web.SessionKeys.EXCEPTION_CODE + " = "
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
            + request.getAttribute("javax.servlet.error.exception_type");

    log.warn(WARNING);
            
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
            <hr><%
            
            try {
            
                String debuggingParam = 
                    session.getServletContext()
                           .getInitParameter("ccc.web.debug");
                if(Boolean.valueOf(debuggingParam)) { %>
            <pre><% out.print(WARNING); %></pre>
            <hr>
		    <pre><% exception.printStackTrace(new java.io.PrintWriter(out)); %></pre><%
		        }
            } catch (Exception e) {
                log.warn("Failed to detect debugging environment",e);
            } %>
            <div id="errorFooter">&nbsp;</div>
        </div>
    </body>
    
</html>
