<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    response.reset();
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
%>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Not found</title>
        <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/advanced.css" type="text/css" media="screen"  />
    </head>

    <body id="notFoundPage">
        <div id="notFoundContainer">
            <div id="notFoundHeader">&nbsp;</div>
            <h1>The page you requested does not exist.</h1>
            <div id="notFoundFooter">&nbsp;</div>
        </div>
    </body>
    
</html>