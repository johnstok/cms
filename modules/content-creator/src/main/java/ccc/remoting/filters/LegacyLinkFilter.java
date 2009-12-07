/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.remoting.filters;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ccc.rendering.NotFoundException;
import ccc.rendering.RedirectRequiredException;
import ccc.rest.Resources;
import ccc.rest.RestException;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.extensions.ResourcesExt;



/**
 * A servlet to redirect old CCC6 URLs to the corresponding CCC7 url.
 * <br>Supports links in the following forms:
 * <br>- /2334.html
 * <br>- /files/my%20file.txt
 *
 * @author Civic Computing Ltd.
 */
public final class LegacyLinkFilter
    implements
        Filter {
    private static final Logger LOG = Logger.getLogger(LegacyLinkFilter.class);

    @EJB(name = Resources.NAME) private transient ResourcesExt _resources;


    /** {@inheritDoc} */
    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
                                        throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse resp = (HttpServletResponse) response;

//        final ServletAction action =
//                    new SerialAction(
//                        new ReaderAction(),
//                        new FixLinkAction());


        final String path = req.getPathInfo();
        LOG.debug("Checking path: "+path);

        final Matcher pageMatcher = PAGE_PATTERN.matcher(path);
//        final Matcher fileMatcher = FILE_PATTERN.matcher(path);
//        final Matcher imageMatcher = IMAGE_PATTERN.matcher(path);

        if (pageMatcher.matches()) {
            redirectToPage(req, pageMatcher);
//        } else if (fileMatcher.matches()) {
//            redirectToFile(req, resp, fileMatcher);
//        } else if (imageMatcher.matches()) {
//            redirectToImage(req, resp, imageMatcher);
        } else {
            chain.doFilter(req, resp);
        }
    }


//    private void redirectToFile(final HttpServletRequest req,
//                                final HttpServletResponse resp,
//                                final Matcher fileMatcher)
//    throws IOException {
//        final String fixedUrl =
//            "/files/"
//            + ResourceName.escape(fileMatcher.group(1));
//        LOG.info("Fixed to path: "+fixedUrl);
//        dispatchRedirect(req, resp, fixedUrl);
//    }
//
//
//    private void redirectToImage(final HttpServletRequest req,
//                                 final HttpServletResponse resp,
//                                 final Matcher fileMatcher)
//    throws IOException {
//        final String fixedUrl =
//            "/images/"
//            + ResourceName.escape(fileMatcher.group(1));
//        LOG.info("Fixed to path: "+fixedUrl);
//        dispatchRedirect(req, resp, fixedUrl);
//    }


    private void redirectToPage(final HttpServletRequest req,
                                final Matcher pageMatcher) {
        LOG.info(
            "Handling broken link: "
            + req.getContextPath()
            + req.getServletPath()
            + req.getPathInfo());

        final String legacyId = pageMatcher.group(1);

        // FIXME: Broken.
        try {
            final ResourceSummary r = _resources.lookupWithLegacyId(legacyId);
            final String resourcePath = r.getAbsolutePath();
            LOG.info("Fixed to path: "+resourcePath);

            // TODO: Should be permanent redirect.
            throw new RedirectRequiredException(resourcePath);

        } catch (final RestException e) {
            throw new NotFoundException();
        }
    }



    /** {@inheritDoc} */
    @Override
    public void destroy() { /* NO OP */ }


    /** {@inheritDoc} */
    @Override
    public void init(final FilterConfig filterConfig) { /* NO OP */ }


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
