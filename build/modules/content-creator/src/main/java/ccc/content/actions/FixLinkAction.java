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

import ccc.domain.Resource;
import ccc.persistence.Repository;
import ccc.persistence.ResourceDao;
import ccc.services.impl.ResourceDaoImpl;
import ccc.types.ResourceName;


/**
 * An action that redirects CCC6 style URLs to the corresponding CCC7 URL.
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
        final ResourceDao rdao = getResourceDao(req);

        final Matcher pageMatcher = PAGE_PATTERN.matcher(path);
        final Matcher fileMatcher = FILE_PATTERN.matcher(path);
        final Matcher imageMatcher = IMAGE_PATTERN.matcher(path);

        if (pageMatcher.matches()) {
            redirectToPage(req, resp, rdao, pageMatcher);
        } else if (fileMatcher.matches()) {
            redirectToFile(req, resp, fileMatcher);
        } else if (imageMatcher.matches()) {
            redirectToImage(req, resp, imageMatcher);
        } else {
            dispatchNotFound(req, resp);
        }
    }


    private void redirectToFile(final HttpServletRequest req,
                                final HttpServletResponse resp,
                                final Matcher fileMatcher) throws IOException {
        final String fixedUrl =
            "/content/files/"
            + ResourceName.escape(fileMatcher.group(1));
        dispatchRedirect(req, resp, fixedUrl);
    }


    private void redirectToImage(final HttpServletRequest req,
                                 final HttpServletResponse resp,
                                 final Matcher fileMatcher) throws IOException {
        final String fixedUrl =
            "/content/images/"
            + ResourceName.escape(fileMatcher.group(1));
        dispatchRedirect(req, resp, fixedUrl);
    }


    private void redirectToPage(final HttpServletRequest req,
                                final HttpServletResponse resp,
                                final ResourceDao rdao,
                                final Matcher pageMatcher)
                                          throws ServletException, IOException {
        final String legacyId = pageMatcher.group(1);
        final Resource r = rdao.lookupWithLegacyId(legacyId);
        final String resourcePath =
            (null==r) ? null : r.absolutePath().toString();

        if (null==resourcePath) {
            dispatchNotFound(req, resp);
        } else {
            // TODO: Should be sending a permanent redirect here.
            dispatchRedirect(req, resp, resourcePath);
        }
    }


    private ResourceDao getResourceDao(final HttpServletRequest req) {
        return new ResourceDaoImpl((Repository) req.getAttribute(SessionKeys.DAO_KEY));
    }


    /** PAGE_PATTERN : Pattern. */
    public static final Pattern PAGE_PATTERN =
        Pattern.compile("/(\\d++)\\.html");

    /** FILE_PATTERN : Pattern. */
    public static final Pattern FILE_PATTERN =
        Pattern.compile("/files/(.+)");

    /** IMAGE_PATTERN : Pattern. */
    public static final Pattern IMAGE_PATTERN =
        Pattern.compile("/images/(.+)");
}
