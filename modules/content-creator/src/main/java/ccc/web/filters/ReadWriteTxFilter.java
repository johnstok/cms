/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.web.filters;

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
        final long then = System.currentTimeMillis();
        try {
            _utx.begin();
            LOG.debug("Transaction started.");
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

        final long now = System.currentTimeMillis();
        LOG.debug("Request finished in "+(now-then)+"ms.");
    }


    private void commit() {
        try {
            _utx.commit();
            LOG.debug("Transaction committed.");
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
            LOG.debug("Transaction rolled back.");
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
