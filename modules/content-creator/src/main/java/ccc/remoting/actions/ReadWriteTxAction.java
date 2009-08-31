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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;


/**
 * A servlet action that manages a read/write J2EE transaction.
 * <p>The transaction rolls back if an exception is thrown and commits
 * otherwise.
 *
 * @author Civic Computing Ltd.
 */
public class ReadWriteTxAction
    implements
        ServletAction {
    private static final Logger LOG = Logger.getLogger(ReadWriteTxAction.class);

    private final UserTransaction _utx;
    private final ServletAction _delegate;

    /**
     * Constructor.
     *
     * @param delegate The action to call next in the chain.
     * @param utx The J2EE transaction to use.
     */
    public ReadWriteTxAction(final ServletAction delegate,
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
            commit();

        } catch (final ServletException e) {
            rollback();
            throw e;
        } catch (final IOException e) {
            rollback();
            throw e;
        } catch (final RuntimeException e) {
            rollback();
            throw e;
        }
    }

    private void commit() {
        try {
            _utx.commit();
        } catch (final SecurityException e) {
            LOG.warn("Error commiting transaction.", e);
        } catch (final IllegalStateException e) {
            LOG.warn("Error commiting transaction.", e);
        } catch (final RollbackException e) {
            LOG.warn("Error commiting transaction.", e);
        } catch (final HeuristicMixedException e) {
            LOG.warn("Error commiting transaction.", e);
        } catch (final HeuristicRollbackException e) {
            LOG.warn("Error commiting transaction.", e);
        } catch (final SystemException e) {
            LOG.warn("Error commiting transaction.", e);
        }
    }

    private void rollback() {
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
