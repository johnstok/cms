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

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.domain.CCCException;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.DataRepository;
import ccc.persistence.ResourceRepository;
import ccc.persistence.ResourceRepositoryImpl;
import ccc.rendering.StatefulReader;



/**
 * Abstract servlet action providing helper methods.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractServletAction
    implements
        ServletAction {


    /**
     * Dispatch to the 'not found' URI.
     *
     * @param request The request.
     * @param response The response.
     * @throws ServletException From servlet API.
     * @throws IOException From servlet API.
     */
    protected void dispatchNotFound(final HttpServletRequest request,
                                    final HttpServletResponse response)
                                          throws ServletException, IOException {
        request.getRequestDispatcher("/notfound").forward(request, response);
    }


    /**
     * Dispatch to the error handler.
     *
     * @param request The request.
     * @param response The response.
     * @param e The exception we encountered
     * @throws ServletException From servlet API.
     * @throws IOException From servlet API.
     */
    protected void dispatchError(final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 final Exception e)
                                          throws ServletException, IOException {
        request.setAttribute(SessionKeys.EXCEPTION_KEY, e);
        request.getRequestDispatcher("/error").forward(request, response);
    }


    /**
     * Send a redirect to the client.
     *
     * @param request The incoming request.
     * @param response The outgoing response.
     * @param relUri The relative URI to redirect to.
     * @throws IOException Servlet API can throw an {@link IOException}.
     */
    protected void dispatchRedirect(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final String relUri) throws IOException {
        final String context = request.getContextPath();
        response.sendRedirect(context+relUri);
    }


    /**
     * Retrieves the exception that this servlet should report.
     * Guarantees to return an exception and never NULL.
     *
     * @param request The request that the exception will be retrieved from.
     * @return The exception that should be reported.
     */
    protected Exception getException(final HttpServletRequest request) {
        final Object o = request.getAttribute(SessionKeys.EXCEPTION_KEY);
        if (null==o) {
            return new CCCException(
                "No exception was found at the expected location: "
                +SessionKeys.EXCEPTION_KEY);
        } else if (!(o instanceof Exception)) {
            return new CCCException(
                "Object at location: "
                +SessionKeys.EXCEPTION_KEY
                +" was not an exception.");
        } else {
            return Exception.class.cast(o);
        }
    }



    protected final ResourceRepository getResourceDao(final HttpServletRequest req) {
        return new ResourceRepositoryImpl(
            (EntityManager) req.getAttribute(SessionKeys.EM_KEY));
    }


    protected final User getCurrentUser(final HttpServletRequest request) {
        return (User) request.getAttribute(SessionKeys.CURRENT_USER);
    }


    protected final DataRepository getDataManager(final HttpServletRequest request) {
        return (DataRepository) request.getAttribute(SessionKeys.DATA_KEY);
    }


    protected final StatefulReader getStatefulReader(final HttpServletRequest request) {
        return (StatefulReader) request.getAttribute(RenderingKeys.READER_KEY);
    }


    protected final Resource getResource(final HttpServletRequest req) {
        return (Resource) req.getAttribute(SessionKeys.RESOURCE_KEY);
    }
}
