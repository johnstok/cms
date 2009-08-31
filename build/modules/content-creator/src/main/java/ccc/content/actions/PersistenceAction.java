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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.persistence.jpa.JpaRepository;
import ccc.services.Repository;



/**
 * A servlet action that manages JPA persistence contexts.
 *
 * @author Civic Computing Ltd.
 */
public class PersistenceAction
    implements
        ServletAction {

    private final EntityManagerFactory _emf;
    private final ServletAction _delegate;

    /**
     * Constructor.
     *
     * @param delegate The next action in the chain.
     * @param emf The entity manager factory used to create entity mangers.
     */
    public PersistenceAction(final ServletAction delegate,
                             final EntityManagerFactory emf) {
        _delegate = delegate;
        _emf = emf;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(final HttpServletRequest req,
                        final HttpServletResponse resp) throws ServletException,
                                                               IOException {

        final EntityManager em = _emf.createEntityManager();

        try {
            final Repository repository = new JpaRepository(em);
            req.setAttribute(
                SessionKeys.DAO_KEY,
                repository);

            _delegate.execute(req, resp);

        } finally {
            em.close();
        }
    }
}
