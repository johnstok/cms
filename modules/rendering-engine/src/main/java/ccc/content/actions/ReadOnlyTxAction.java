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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ReadOnlyTxAction
    implements
        ServletAction {
    private static final Logger LOG = Logger.getLogger(ReadOnlyTxAction.class);

    private final UserTransaction _utx;
    private final ServletAction _delegate;

    /**
     * Constructor.
     *
     * @param delegate
     * @param utx
     */
    public ReadOnlyTxAction(final ServletAction delegate,
                            final UserTransaction utx) {
        _delegate = delegate;
        _utx = utx;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(final HttpServletRequest req,
                        final HttpServletResponse resp) throws ServletException,
                                                               IOException {
        try {
            _utx.begin();
        } catch (final NotSupportedException e) {
            throw new ServletException("Failed to start transaction.", e);
        } catch (final SystemException e) {
            throw new ServletException("Failed to start transaction.", e);
        }

        try {
            _delegate.execute(req, resp);

        } finally {
            try {
                _utx.rollback();
            } catch (final IllegalStateException e) {
                LOG.warn("Error rolling back transaction.", e);
            } catch (final SecurityException e) {
                LOG.warn("Error rolling back transaction.", e);
            } catch (final SystemException e) {
                LOG.warn("Error rolling back transaction.", e);
            }
        }
    }
}
