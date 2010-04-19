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
package ccc.web.filters;

import java.io.IOException;
import java.util.Map;
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

import ccc.api.Resources;
import ccc.api.dto.ResourceSummary;
import ccc.api.exceptions.CCException;
import ccc.api.types.ResourcePath;
import ccc.web.rendering.NotFoundException;
import ccc.web.rendering.RedirectRequiredException;



/**
 * A servlet to redirect old CCC6 URLs to the corresponding CCC7 url.
 * <br>Supports links in the following forms:
 * <br>- /2334.html
 * <br>- /233.4.63.html
 * <br>- /files/my%20file.txt
 * <br>- /images/my%20image.jpg
 * <br>- /foo?p_service=Content.show&p_applic=CCC&pContentID=557
 *
 * @author Civic Computing Ltd.
 */
public final class LegacyLinkFilter
    implements
        Filter {
    private static final Logger LOG = Logger.getLogger(LegacyLinkFilter.class);

    @EJB(name = Resources.NAME) private transient Resources _resources;


    /**
     * Constructor.
     */
    public LegacyLinkFilter() { super(); }


    /**
     * Constructor.
     *
     * @param resources The resource API for this filter.
     */
    public LegacyLinkFilter(final Resources resources) {
        _resources = resources;
    }


    /** {@inheritDoc} */
    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
                                        throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse resp = (HttpServletResponse) response;

        final String path = req.getPathInfo();
        LOG.debug("Checking path: "+path);

        final Matcher pageMatcher = PAGE_PATTERN.matcher(path);
        final Matcher fileMatcher = FILE_PATTERN.matcher(path);
        final Matcher imageMatcher = IMAGE_PATTERN.matcher(path);

        if (pageMatcher.matches()) {
            redirectToPage(req, pageMatcher.group(1));

        } else if (fileMatcher.matches()
                   && !ResourcePath.isValid("/"+fileMatcher.group(1))) {
            redirectToFile(req, fileMatcher.group(1));

        } else if (imageMatcher.matches()
                   && !ResourcePath.isValid("/"+imageMatcher.group(1))) {
            redirectToImage(req, imageMatcher.group(1));

        } else if (isController(req.getParameterMap())) {
            redirectToPage(req, req.getParameter("pContentID"));

        } else {
            chain.doFilter(req, resp);
        }
    }


    /**
     * Determine whether the request is for a CCC6 controller.
     *
     * @param queryParams The requeste's query parameters.
     *
     * @return True if the request is to a CCC6 controller, false otherwise.
     */
    protected boolean isController(final Map<String, String> queryParams) {
        return
            queryParams.containsKey("p_applic")
            &&  queryParams.containsKey("p_service")
            &&  queryParams.containsKey("pContentID");
    }


    private void redirectToFile(final HttpServletRequest req,
                                final String badPath) {

        LOG.info(
            "Handling broken link: "
            + req.getContextPath()
            + req.getServletPath()
            + req.getPathInfo());


        final String fixedUrl = "/files/" + escape(badPath);
        LOG.debug("Fixed to path: "+fixedUrl);

        throw new RedirectRequiredException(fixedUrl, true);
    }


    private void redirectToImage(final HttpServletRequest req,
                                 final String badPath) {

        LOG.info(
            "Handling broken link: "
            + req.getContextPath()
            + req.getServletPath()
            + req.getPathInfo());


        final String fixedUrl = "/images/" + escape(badPath);
        LOG.debug("Fixed to path: "+fixedUrl);

        throw new RedirectRequiredException(fixedUrl, true);
    }


    private String escape(final String badPath) {
        return badPath.replaceAll("[^\\.\\-\\w/]", "_");
    }


    private void redirectToPage(final HttpServletRequest req,
                                final String legacyId) {
        LOG.info(
            "Handling broken link: "
            + req.getContextPath()
            + req.getServletPath()
            + req.getPathInfo());

        try {
            final ResourceSummary r = _resources.resourceForLegacyId(legacyId);
            final String resourcePath = r.getAbsolutePath();
            LOG.debug("Fixed to path: "+resourcePath);

            throw new RedirectRequiredException(resourcePath, true);

        } catch (final CCException e) {
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
        Pattern.compile("/(\\d++)[\\.\\d]*\\.htm[l]?");

    /** FILE_PATTERN : Pattern. */
    public static final Pattern FILE_PATTERN =
        Pattern.compile("/files/(.+)");

    /** IMAGE_PATTERN : Pattern. */
    public static final Pattern IMAGE_PATTERN =
        Pattern.compile("/images/(.+)");
}
