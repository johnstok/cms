/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.remoting.actions;

import static ccc.commons.Strings.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ccc.domain.CCCException;
import ccc.domain.EntityNotFoundException;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.ResourceRepository;
import ccc.rendering.AuthenticationRequiredException;
import ccc.rendering.NotFoundException;
import ccc.types.ResourcePath;
import ccc.types.Username;


/**
 * A servlet action that looks up the CCC resource for a URL.
 * <p>Security privileges are checked before handing control to the delegate
 * action.
 *
 * @author Civic Computing Ltd.
 */
public class LookupResourceAction
    extends
        AbstractServletAction {

    private static final Logger LOG =
        Logger.getLogger(LookupResourceAction.class);

    private final ServletAction _delegate;
    private final String        _rootName;


    /**
     * Constructor.
     *
     * @param delegate The action to call next in the chain.
     * @param rootName The name of content root to serve from.
     */
    public LookupResourceAction(final ServletAction delegate,
                                final String rootName) {
        _delegate = delegate;
        _rootName = rootName;
    }


    /** {@inheritDoc} */
    @Override
    public void execute(final HttpServletRequest request,
                        final HttpServletResponse response)
    throws ServletException, IOException {
            final ResourceRepository rdao = getResourceDao(request);
            final User currentUser = getCurrentUser(request);

            final ResourcePath contentPath = determineResourcePath(request);
            final Resource rs = lookupResource(contentPath, rdao);
            checkSecurity(rs, currentUser);
            request.setAttribute(SessionKeys.RESOURCE_KEY, rs);

            _delegate.execute(request, response);
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
}
