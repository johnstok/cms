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
package ccc.remoting.filters;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
public class ReadWriteTxFilter
    implements
        Filter {
    private static final Logger LOG = Logger.getLogger(ReadWriteTxFilter.class);

    @Resource private transient UserTransaction _utx;


    /** {@inheritDoc} */
    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
                                        throws IOException, ServletException {
        try {
            _utx.begin();
        } catch (final NotSupportedException e) {
            throw new ServletException("Failed to start transaction.", e);
        } catch (final SystemException e) {
            throw new ServletException("Failed to start transaction.", e);
        }

        try {
            chain.doFilter(request, response);
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


    /** {@inheritDoc} */
    @Override
    public void destroy() { /* NO OP */ }


    /** {@inheritDoc} */
    @Override
    public void init(final FilterConfig filterConfig) { /* NO OP */ }
}
