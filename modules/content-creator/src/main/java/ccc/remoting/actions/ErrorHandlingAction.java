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

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.rendering.AuthenticationRequiredException;
import ccc.rendering.NotFoundException;
import ccc.rendering.RedirectRequiredException;
import ccc.rendering.UnsupportMethodException;


/**
 * A servlet action that catches and handles runtime exceptions.
 *
 * @author Civic Computing Ltd.
 */
public class ErrorHandlingAction
    extends
        AbstractServletAction {

    private final ServletAction  _delegate;
    private final ServletContext _context;
    private final String         _loginUri;

    /**
     * Constructor.
     *
     * @param delegate The next action in the chain.
     * @param context The servlet context.
     * @param loginUri The url of the login page for secure resources.
     */
    public ErrorHandlingAction(final ServletAction delegate,
                               final ServletContext context,
                               final String loginUri) {
        _delegate = delegate;
        _context = context;
        _loginUri = loginUri;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(final HttpServletRequest req,
                        final HttpServletResponse resp) throws ServletException,
                                                               IOException {
        try {
            _delegate.execute(req, resp);

        } catch (final NotFoundException e) {
            dispatchNotFound(req, resp);

        } catch (final RedirectRequiredException e) {
            final String relUri = e.getResource().absolutePath().toString();
            dispatchRedirect(req, resp, relUri);

        } catch (final AuthenticationRequiredException e) {
            final String relUri =
                _loginUri+e.getResource().absolutePath().toString();
            dispatchRedirect(req, resp, relUri);

        } catch (final UnsupportMethodException e) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);

        } catch (final RuntimeException e) {
            if(resp.isCommitted()) {
                /*
                 * Nothing we can do to rescue the response - the HTTP response
                 * code + headers has already been sent. Just log the error on
                 * the server.
                 */
                _context.log(
                    "Error caught after response was committed.",
                    e);
                resp.getWriter().write("\n\nAN INTERNAL ERROR OCCURED.");

            } else {
                _context.log(
                    "Error caught on uncommited response"
                    + " - sending error message.",
                    e);
                dispatchError(req, resp, e);
            }
        }
    }

//    private void handleException(final Writer output, final Exception e) {
//        final String msg = ""+e.getMessage(); // getMessage() is NULL for NPE
//        LOG.warn("Error in template: "+msg);
//
//        try {
//            output.write(msg);
//        } catch (final IOException e1) {
//            LOG.warn("Error writing to servlet response.", e1);
//        }
//    }
}
