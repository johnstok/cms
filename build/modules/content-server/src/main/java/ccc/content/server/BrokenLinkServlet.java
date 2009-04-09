/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.content.server;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.domain.ResourceName;
import ccc.services.StatefulReader;



/**
 * A servlet to redirect old CCC6 URLs to the corresponding CCC7 url.
 * <br>Supports links in the following forms:
 * <br>- /2334.html
 * <br>- /files/my%20file.txt
 *
 * @author Civic Computing Ltd.
 */
public class BrokenLinkServlet
    extends
        CCCServlet {

    private final Registry _registry = new JNDI();

    /** PAGE_PATTERN : Pattern. */
    public static final Pattern PAGE_PATTERN =
        Pattern.compile("/(\\d++)\\.html");

    /** FILE_PATTERN : Pattern. */
    public static final Pattern FILE_PATTERN =
        Pattern.compile("/files/(.+)");

    /** {@inheritDoc} */
    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
                                          throws ServletException, IOException {

        final String path = req.getPathInfo();

        final Matcher pageMatcher = PAGE_PATTERN.matcher(path);
        final Matcher fileMatcher = FILE_PATTERN.matcher(path);

        if (pageMatcher.matches()) {
            final StatefulReader r = _services.statefulReader();
            final String resourcePath = r.absolutePath(pageMatcher.group(1));
            if (null==resourcePath) {
                dispatchNotFound(req, resp);
            } else {
                dispatchRedirect(req, resp, resourcePath);
            }

        } else if (fileMatcher.matches()) {
            final String fixedUrl =
                "/content/files/"+ResourceName.escape(fileMatcher.group(1));
            dispatchRedirect(req, resp, fixedUrl);

        } else {
            dispatchNotFound(req, resp);
        }
    }
}
