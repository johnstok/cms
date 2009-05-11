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

import static ccc.commons.Strings.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ccc.content.exceptions.AuthenticationRequiredException;
import ccc.content.exceptions.NotFoundException;
import ccc.content.exceptions.RedirectRequiredException;
import ccc.content.response.DefaultRenderer;
import ccc.content.response.Renderer;
import ccc.content.response.Response;
import ccc.domain.CCCException;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.User;
import ccc.services.DataManager;
import ccc.services.SearchEngine;
import ccc.services.StatefulReader;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class RenderResourceAction
    extends
        AbstractServletAction
    implements
        ServletAction {
    private static final Logger LOG =
        Logger.getLogger(RenderResourceAction.class);

    private final boolean _respectVisiblity;
    private final String _rootName;
    private final String _loginUri;
    private final SearchEngine _search;

    /**
     * Constructor.
     *
     * @param user
     * @param respectVisiblity
     * @param rootName
     * @param _data
     * @param _search
     */
    public RenderResourceAction(final boolean respectVisiblity,
                                final String rootName,
                                final String loginUri,
                                final SearchEngine search) {
        _respectVisiblity = respectVisiblity;
        _rootName = rootName;
        _loginUri = loginUri;
        _search = search;
    }


    /** {@inheritDoc} */
    @Override
    public void execute(final HttpServletRequest request,
                        final HttpServletResponse response)
                                          throws ServletException, IOException {
        try {
            final DataManager _data =
                (DataManager) request.getAttribute(SessionKeys.DATA_KEY);

            final ResourcePath contentPath = determineResourcePath(request);

            final StatefulReader reader =
                (StatefulReader) request.getAttribute(RenderingKeys.READER_KEY);

            final Resource rs = lookupResource(contentPath, reader);
            checkSecurity(
                rs, (User) request.getAttribute(SessionKeys.CURRENT_USER));
            final Response r =
                prepareResponse(request, reader, _data, _search, rs);

            if (rs.roles().size()>0) {
                r.setExpiry(null); // Dont'cache secure pages.
            }

            r.write(response);

        } catch (final NotFoundException e) {
            dispatchNotFound(request, response);
        } catch (final RedirectRequiredException e) {
            final String relUri = e.getResource().absolutePath().toString();
            dispatchRedirect(request, response, relUri);
        } catch (final AuthenticationRequiredException e) {
            final String relUri =
                _loginUri+e.getResource().absolutePath().toString();
            dispatchRedirect(request, response, relUri);
        }
    }


    private void checkSecurity(final Resource r, final User user) {
        final User u = (null==user) ? new User("anonymous") : user;
        if (!r.isAccessibleTo(u)) {
            throw new AuthenticationRequiredException(r);
        }
    }


    public Resource lookupResource(final ResourcePath contentPath,
                                    final StatefulReader reader) {
        final Resource rs = reader.lookup(_rootName, contentPath);
        if (null==rs) {
            throw new NotFoundException();
        }
        return rs;
    }


    /**
     * Determine the ResourcePath from a request's pathInfo.
     * TODO: Change param to type String.
     *
     * @param request The HTTP request.
     * @return The corresponding resource path.
     */
    public ResourcePath determineResourcePath(
                                             final HttpServletRequest request) {
        String pathString = request.getPathInfo();
        LOG.info("Request for /"+_rootName+pathString);
        pathString = nvl(pathString, "/");
        pathString = removeTrailing('/', pathString);

        try {
            final ResourcePath contentPath = new ResourcePath(pathString);
            return contentPath;
        } catch (final CCCException e) {
            throw new NotFoundException();
        }
    }


    @SuppressWarnings("unchecked")
    private Response prepareResponse(final HttpServletRequest request,
                                     final StatefulReader reader,
                                     final DataManager dataMgr,
                                     final SearchEngine searchEngine,
                                     final Resource rs) {

        final Map<String, String[]> parameters = request.getParameterMap();
        final Renderer renderer =
            new DefaultRenderer(
                dataMgr,
                searchEngine,
                reader,
                _respectVisiblity);

        final Response r;
        if (parameters.keySet().contains("wc")) {
            r = renderer.renderWorkingCopy(rs, parameters);
        } else if (parameters.keySet().contains("v")) {
            r = renderer.renderHistoricalVersion(rs, parameters);
        } else {
            r = renderer.render(rs, parameters);
        }
        return r;
    }
}
