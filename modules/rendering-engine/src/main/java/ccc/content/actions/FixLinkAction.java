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
package ccc.content.actions;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.domain.ResourceName;
import ccc.services.StatefulReader;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class FixLinkAction
    extends
        AbstractServletAction {


    /** {@inheritDoc} */
    @Override
    public void execute(final HttpServletRequest req,
                        final HttpServletResponse resp) throws ServletException,
                                                               IOException {

        final String path = req.getPathInfo();
        final StatefulReader r =
                (StatefulReader) req.getAttribute(RenderingKeys.READER_KEY);

        final Matcher pageMatcher = PAGE_PATTERN.matcher(path);
        final Matcher fileMatcher = FILE_PATTERN.matcher(path);

        if (pageMatcher.matches()) {
            final String resourcePath =
                r.absolutePath(pageMatcher.group(1));
            if (null==resourcePath) {
                dispatchNotFound(req, resp);
            } else {
                // TODO: Should be sending a permanent redirect here.
                dispatchRedirect(req, resp, resourcePath);
            }

        } else if (fileMatcher.matches()) {
            final String fixedUrl =
                "/content/files/"
                + ResourceName.escape(fileMatcher.group(1));
            dispatchRedirect(req, resp, fixedUrl);

        } else {
            dispatchNotFound(req, resp);
        }
    }


    /** PAGE_PATTERN : Pattern. */
    public static final Pattern PAGE_PATTERN =
        Pattern.compile("/(\\d++)\\.html");

    /** FILE_PATTERN : Pattern. */
    public static final Pattern FILE_PATTERN =
        Pattern.compile("/files/(.+)");
}
