<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    response.reset();
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
%>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Error</title>
        <link rel="stylesheet" href="assets/css/advanced.css" type="text/css" media="screen" />
    </head>

    <body id="errorPage">
        <div id="errorContainer">
            <div id="errorHeader">&nbsp;</div>
            <h1>An internal error occured.</h1>
            <div id="errorFooter">&nbsp;</div>
        </div>
    </body>
    
</html>
