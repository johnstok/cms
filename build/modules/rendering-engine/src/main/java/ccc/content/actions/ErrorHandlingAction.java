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

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ErrorHandlingAction
    extends
        AbstractServletAction
    implements
        ServletAction {

    private final ServletAction _delegate;
    private final ServletContext _context;

    /**
     * Constructor.
     *
     * @param delegate
     * @param context
     */
    public ErrorHandlingAction(final ServletAction delegate,
                               final ServletContext context) {
        _delegate = delegate;
        _context = context;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(final HttpServletRequest req,
                        final HttpServletResponse resp) throws ServletException,
                                                               IOException {
        try {
            _delegate.execute(req, resp);

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

            } else {
                _context.log(
                    "Error caught on uncommited response"
                    + " - sending error message.",
                    e);
                dispatchError(req, resp, e);
            }
        }
    }

}
