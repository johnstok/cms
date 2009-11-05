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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import ccc.remoting.actions.SessionKeys;



/**
 * A servlet action that manages JPA persistence contexts.
 *
 * @author Civic Computing Ltd.
 */
public class PersistenceAction
    implements
        Filter {

    @PersistenceUnit private transient EntityManagerFactory _emf;


    /** {@inheritDoc} */
    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
                                        throws IOException, ServletException {

        final EntityManager em = _emf.createEntityManager();

        try {
            request.setAttribute(
                SessionKeys.EM_KEY,
                em);

            chain.doFilter(request, response);

        } finally {
            try {
                em.close();
            } finally {
                // Prevent access to a closed entity manager.
                request.removeAttribute(SessionKeys.EM_KEY);
            }
        }
    }


    /** {@inheritDoc} */
    @Override
    public void destroy() { /* NO OP */ }


    /** {@inheritDoc} */
    @Override
    public void init(final FilterConfig filterConfig) { /* NO OP */ }
}
