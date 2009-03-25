/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

import static ccc.commons.Strings.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.commons.DBC;
import ccc.commons.JNDI;
import ccc.domain.CCCException;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.User;


/**
 * The ContentServlet class serves CCC content.
 * Only the HTTP GET method is supported.
 *
 * @author Civic Computing Ltd.
 */
public final class ContentServlet extends CCCServlet {

    private final ObjectFactory _factory;
    private String _rootName;


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
        _rootName = cf.getInitParameter("root_name");
        _factory.setRespectVisibility(
            cf.getInitParameter("respect_visibility"));
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
            final Resource rs = lookupResource(contentPath);
            checkSecurity(rs);
            final Response r = prepareResponse(request, rs);
            handle(response, request, r);

        } catch (final NotFoundException e) {
            dispatchNotFound(request, response);
        } catch (final RedirectRequiredException e) {
            final String relUri = e.getResource().absolutePath().toString();
            dispatchRedirect(request, response, relUri);
        } catch (final AuthenticationRequiredException e) {
            final String relUri = "/content/login?tg="+e.getResource().absolutePath().toString();
            dispatchRedirect(request, response, relUri);
        }
    }


    private Response prepareResponse(final HttpServletRequest request,
                                     final Resource rs) {

        final Map<String, String[]> parameters = request.getParameterMap();
        final Response r;
        if (parameters.keySet().contains("wc")) {
            r = _factory
                    .createRenderer()
                    .renderWorkingCopy(rs, parameters);
        } else if (parameters.keySet().contains("v")) {
            r = _factory
                    .createRenderer()
                    .renderHistoricalVersion(rs, parameters);
        } else {
            r = _factory
                    .createRenderer()
                    .render(rs, parameters);
        }
        return r;
    }


    private Resource lookupResource(final ResourcePath contentPath) {
        final Resource rs =
            _factory.getReader().lookup(_rootName, contentPath);
        if (null==rs) {
            throw new NotFoundException();
        }
        return rs;
    }


    private void checkSecurity(final Resource r) {
        User u = _factory.currentUser();
        if (null==u) {
            u = new User("anonymous");
        }
        if (!r.isAccessibleTo(u)) {
            throw new AuthenticationRequiredException(r);
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
