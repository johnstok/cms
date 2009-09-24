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
package ccc.remoting.actions;

import static ccc.commons.Strings.*;

import java.io.IOException;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ccc.domain.CCCException;
import ccc.domain.EntityNotFoundException;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.FileRepository;
import ccc.persistence.ResourceRepository;
import ccc.persistence.ResourceRepositoryImpl;
import ccc.rendering.AuthenticationRequiredException;
import ccc.rendering.DefaultRenderer;
import ccc.rendering.NotFoundException;
import ccc.rendering.RedirectRequiredException;
import ccc.rendering.Renderer;
import ccc.rendering.Response;
import ccc.rendering.StatefulReader;
import ccc.rendering.velocity.VelocityProcessor;
import ccc.search.SearchEngine;
import ccc.types.ResourcePath;
import ccc.types.Username;


/**
 * A servlet action that renders a resource to servlet response.
 *
 * @author Civic Computing Ltd.
 */
public class RenderResourceAction
    extends
        AbstractServletAction {
    private static final Logger LOG =
        Logger.getLogger(RenderResourceAction.class);

    private final boolean _respectVisiblity;
    private final String _rootName;
    private final String _loginUri;
    private final SearchEngine _search;

    /**
     * Constructor.
     *
     * @param respectVisiblity Should we respect the visibility of resources,
     *  as specified by their published status.
     * @param rootName The name of content root to serve from.
     * @param loginUri The url of the login page for secure resources.
     * @param search The search engine to use.
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
            final FileRepository data = getDataManager(request);
            final StatefulReader reader = getStatefulReader(request);
            final ResourceRepository rdao = getResourceDao(request);
            final User currentUser = getCurrentUser(request);

            final ResourcePath contentPath = determineResourcePath(request);


            final Resource rs = lookupResource(contentPath, rdao);

            checkSecurity(rs, currentUser);

            final Response r =
                prepareResponse(request, reader, data, _search, rs);

            if (rs.roles().size()>0) { // Dont'cache secure pages.
                r.setExpiry(null);
            }

            r.write(response, currentUser, new VelocityProcessor());

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
        final User u = (null==user)
            ? new User(new Username("anonymous"), "password")
            : user;
        if (!r.isAccessibleTo(u)) {
            throw new AuthenticationRequiredException(r);
        }
    }


    /**
     * Look up a resource given its path.
     *
     * @param contentPath The resource path.
     * @param rdao The resource DAO.
     *
     * @return The corresponding resource.
     */
    public Resource lookupResource(final ResourcePath contentPath,
                                   final ResourceRepository rdao) {
        try {
            return rdao.lookup(_rootName, contentPath);
        } catch (final EntityNotFoundException e) {
            throw new NotFoundException();
        }
    }


    /**
     * Determine the ResourcePath from a request's pathInfo.
     *
     * @param request The HTTP request.
     * @return The corresponding resource path.
     */
    public ResourcePath determineResourcePath(
                                             final HttpServletRequest request) {
        String pathString = request.getPathInfo();
        pathString = nvl(pathString, "/");
        pathString = removeTrailing('/', pathString);
        LOG.info(
            "Request for "+request.getContextPath()+"/"+_rootName+pathString);

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
                                     final FileRepository dataMgr,
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


    private ResourceRepository getResourceDao(final HttpServletRequest req) {
        return new ResourceRepositoryImpl(
            (EntityManager) req.getAttribute(SessionKeys.EM_KEY));
    }


    private User getCurrentUser(final HttpServletRequest request) {
        return (User) request.getAttribute(SessionKeys.CURRENT_USER);
    }


    private FileRepository getDataManager(final HttpServletRequest request) {
        return (FileRepository) request.getAttribute(SessionKeys.DATA_KEY);
    }


    private StatefulReader getStatefulReader(final HttpServletRequest request) {
        return (StatefulReader) request.getAttribute(RenderingKeys.READER_KEY);
    }
}
