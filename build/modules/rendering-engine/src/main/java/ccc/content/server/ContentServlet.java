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

import org.apache.log4j.Logger;

import ccc.commons.DBC;
import ccc.commons.JNDI;
import ccc.content.response.Response;
import ccc.domain.CCCException;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.User;
import ccc.services.StatefulReader;


/**
 * The ContentServlet class serves CCC content.
 * Only the HTTP GET method is supported.
 *
 * @author Civic Computing Ltd.
 */
public final class ContentServlet extends CCCServlet {
    private static final Logger LOG = Logger.getLogger(ContentServlet.class);

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
            final StatefulReader reader = _factory.getReader();
            try {
                final Resource rs = lookupResource(contentPath, reader);
                checkSecurity(rs);
                final Response r = prepareResponse(request, reader, rs);
                r.setExpiry(0);
//                if (rs.roles().size()>0) {
//                    r.setExpiry(0); // Dont'cache secure pages.
//                } else {
//                    r.setExpiry(3600); // Cache all other pages for 1hr.
//                }
                r.write(response);
            } finally {
                reader.close();
            }

        } catch (final NotFoundException e) {
            dispatchNotFound(request, response);
        } catch (final RedirectRequiredException e) {
            final String relUri = e.getResource().absolutePath().toString();
            dispatchRedirect(request, response, relUri);
        } catch (final AuthenticationRequiredException e) {
            final String relUri =
                "/content/login?tg="+e.getResource().absolutePath().toString();
            dispatchRedirect(request, response, relUri);
        }
    }


    @SuppressWarnings("unchecked")
    private Response prepareResponse(final HttpServletRequest request,
                                     final StatefulReader reader,
                                     final Resource rs) {

        final Map<String, String[]> parameters = request.getParameterMap();
        final Response r;
        if (parameters.keySet().contains("wc")) {
            r = _factory
                    .createRenderer(reader)
                    .renderWorkingCopy(rs, parameters);
        } else if (parameters.keySet().contains("v")) {
            r = _factory
                    .createRenderer(reader)
                    .renderHistoricalVersion(rs, parameters);
        } else {
            r = _factory
                    .createRenderer(reader)
                    .render(rs, parameters);
        }
        return r;
    }


    private Resource lookupResource(final ResourcePath contentPath,
                                    final StatefulReader reader) {
        final Resource rs = reader.lookup(_rootName, contentPath);
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
        LOG.info("Request for "+pathString);
        pathString = nvl(pathString, "/");
        pathString = removeTrailing('/', pathString);

        try {
            final ResourcePath contentPath = new ResourcePath(pathString);
            return contentPath;
        } catch (final CCCException e) {
            throw new NotFoundException();
        }
    }
}
