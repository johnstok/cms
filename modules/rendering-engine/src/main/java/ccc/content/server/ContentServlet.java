/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */

package ccc.content.server;

import static ccc.commons.Strings.*;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.commons.DBC;
import ccc.commons.JNDI;
import ccc.domain.CCCException;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;


/**
 * The ContentServlet class serves CCC content. Typically the servlet will be
 * mounted at the servlet path '/*' in the web.xml config file. Only the HTTP
 * GET method is currently supported.
 *
 * TODO: Locale setting:
 *  log [getLocale;
 *       Accept-Language
 *       session locale setting]
 * TODO: Character encoding:
 *  log: [request content-type;
 *        response content-type;    // Done
 *        Accept-Charset header;
 *        Accept-encoding header]
 * TODO: Markup escaping?
 *
 * @author Civic Computing Ltd
 */
public final class ContentServlet extends CCCServlet {

    private final ObjectFactory _factory;


    /**
     * Constructor.
     *
     * @param factory The renderer factory for this servlet.
     */
    public ContentServlet(final ObjectFactory factory) {
        DBC.require().notNull(factory);
        _factory = factory;
    }

    /**
     * Constructor.
     */
    public ContentServlet() {
        _factory = new DefaultObjectFactory(new JNDI());
    }

    /** {@inheritDoc} */
    @Override
    public void init() throws ServletException {
        final ServletConfig cf = getServletConfig();
        _factory.setRespectVisibility(
            cf.getInitParameter("respect_visibility"));
        _factory.setRootName(
            cf.getInitParameter("root_name"));
    }

    /**
     * Get the content for the specified relative URI. This method reads the
     * value from {@link HttpServletRequest#getPathInfo()} and maps that to a
     * corresponding resource in CCC.
     *
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response)
                                          throws IOException, ServletException {
        try {
            final ResourcePath contentPath = determineResourcePath(request);
            final Resource rs = _factory.createLocator().locate(contentPath);

            final Response r;
            if (request.getParameterMap().keySet().contains("wc")) {
                r = _factory.createRenderer().renderWorkingCopy(rs);
            } else {
                r = _factory.createRenderer().render(rs);
            }
            handle(response, request, r);

        } catch (final NotFoundException e) {
            dispatchNotFound(request, response);

        } catch (final RedirectRequiredException e) {
            final String relUri = e.getResource().absolutePath().toString();
            dispatchRedirect(request, response, relUri);
        }
    }

    /**
     * Determine the ResourcePath from a request's pathInfo.
     * TODO: Change param to type String.
     *
     * @param request The HTTP request.
     * @return The corresponding resource path.
     */
    protected ResourcePath determineResourcePath(
                                             final HttpServletRequest request) {
        String pathString = request.getPathInfo();
        pathString = nvl(pathString, "/");
        pathString = removeTrailing('/', pathString);

        try {
            final ResourcePath contentPath = new ResourcePath(pathString);
            return contentPath;
        } catch (final CCCException e) {
            throw new NotFoundException();
        }
    }

    /**
     * Translates a domain response into HTTP response for the servlet API.
     *
     * @param httpResponse The servlet response.
     * @param httpRequest The servlet request.
     * @param response The CCC response.
     * @throws IOException - if writing to the servlet response fails.
     */
    protected void handle(final HttpServletResponse httpResponse,
                          final HttpServletRequest httpRequest,
                          final Response response) throws IOException {

        if (null!=response.getDescription()) {
            httpResponse.setHeader(
                "Content-Description",
                response.getDescription());
        }

        if (null!=response.getDisposition()) {
            httpResponse.setHeader(
                "Content-Disposition",
                response.getDisposition());
        }

        if (null!=response.getLength()) {
            httpResponse.setHeader(
                "Content-Length",
                String.valueOf(response.getLength().longValue()));
        }

        if (null!=response.getMimeType()) {
            httpResponse.setContentType(response.getMimeType());
        }

        if (null!=response.getCharSet()) {
            httpResponse.setCharacterEncoding(response.getCharSet());
        }

        if (null!=response.getExpiry()) {
            if (response.getExpiry().longValue() < 1) {
                disableCaching(httpResponse);
            } else {
                throw new RuntimeException();
            }
        }

        if (null!=response.getBody()) {
            response.getBody()
                .write(httpResponse.getOutputStream(), _factory.getReader());
        }
    }
}
