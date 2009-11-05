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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ccc.domain.EntityNotFoundException;
import ccc.domain.Resource;
import ccc.persistence.ResourceRepository;
import ccc.rendering.NotFoundException;
import ccc.types.ResourcePath;


/**
 * A servlet action that looks up the CCC resource for a URL.
 *
 * @author Civic Computing Ltd.
 */
public class LookupResourceAction
    extends
        AbstractServletAction {

    private static final Logger LOG =
        Logger.getLogger(LookupResourceAction.class);

    private final String        _rootName;


    /**
     * Constructor.
     *
     * @param rootName The name of content root to serve from.
     */
    public LookupResourceAction(final String rootName) {
        _rootName = rootName;
    }


    /** {@inheritDoc} */
    @Override
    public void execute(final HttpServletRequest request,
                        final HttpServletResponse response) {
            final ResourceRepository rdao = getResourceDao(request);

            final ResourcePath contentPath = determineResourcePath(request);
            final Resource rs = lookupResource(contentPath, rdao);
            request.setAttribute(SessionKeys.RESOURCE_KEY, rs);
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
            "Resource path is /"+_rootName+pathString);

        if (ResourcePath.isValid(pathString)) {
            return new ResourcePath(pathString);
        }

        throw new NotFoundException();
    }
}
