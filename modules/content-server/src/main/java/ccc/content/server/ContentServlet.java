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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.commons.Registry;
import ccc.domain.ResourcePath;


/**
 * The ContentServlet class serves CCC content. Typically the servlet will be
 * mounted at the servlet path '/*' in the web.xml config file. Only the HTTP
 * GET method is currently supported.
 *
 * TODO: Mime type setting
 * TODO: Locale setting:
 *  log [getLocale;
 *       Accept-Language
 *       session locale setting]
 * TODO: Character encoding:
 *  log: [request content-type;
 *        response content-type;    // Done
 *        Accept-Charset header;
 *        Accept-encoding header]
 *
 * TODO: Handle bad resource paths - return 404?
 * TODO: Handle good path, no resource - return 404?
 * TODO: Markup escaping?
 * TODO: Handle standard errors -> converting to HTML.
 * TODO: Marshal CCCException to HTML.
 * TODO: How do we handle '/'? - return content root with folder template
 * TODO: Should be final but need to wait for resource injection...
 *
 * @author Civic Computing Ltd
 */
public class ContentServlet extends CCCServlet {

    /**
     * Constructor.
     *
     * @param registry The registry for this servlet.
     */
    public ContentServlet(final Registry registry) {
        super(registry);
    }

    /**
     * Constructor.
     */
    public ContentServlet() { super(); }

    /**
     * Get the content for the specified relative URI. This method reads the
     * value from {@link HttpServletRequest#getPathInfo()} and maps that to a
     * corresponding resource in CCC.
     *
     * {@inheritDoc}
     */
    @Override
    protected void doSafeGet(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final ResourceRenderer renderer)
        throws IOException, ServletException {
        try {
            final ResourcePath contentPath = determineResourcePath(request);
            final Response r = renderer.render(contentPath);
            handle(response, request, r);
        } catch (final NotFoundException e) {
            dispatchNotFound(request, response);
        } catch (final RedirectRequiredException e) {
            final String context = request.getContextPath();
            final String relUri = e.getResource().absolutePath().toString();
            response.sendRedirect(context+relUri);
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param request
     * @return
     */
    protected ResourcePath determineResourcePath(final HttpServletRequest request) {
        String pathString = request.getPathInfo();
        pathString = nvl(pathString, "/");
        pathString = removeTrailing('/', pathString);

        final ResourcePath contentPath = new ResourcePath(pathString);
        return contentPath;
    }

    /**
     * Translates a domain response into HTTP response for the servlet API.
     *
     * @param httpResponse
     * @param httpRequest
     * @param response
     * @throws IOException
     */
    public void handle(final HttpServletResponse httpResponse,
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
                disableCachingFor(httpResponse);
            } else {
                throw new RuntimeException();
            }
        }

        if (null!=response.getBody()) {
            response.getBody().write(httpResponse.getOutputStream());
        }
    }
}
