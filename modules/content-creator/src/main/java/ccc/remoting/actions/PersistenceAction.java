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
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * A servlet action that manages JPA persistence contexts.
 *
 * @author Civic Computing Ltd.
 */
public class PersistenceAction
    extends
        SerialAction {

    private final EntityManagerFactory _emf;

    /**
     * Constructor.
     *
     * @param emf The entity manager factory used to create entity mangers.
     * @param actions The actions to perform.
     */
    public PersistenceAction(final EntityManagerFactory emf,
                             final ServletAction... actions) {
        super(actions);
        _emf = emf;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(final HttpServletRequest req,
                        final HttpServletResponse resp) throws ServletException,
                                                               IOException {

        final EntityManager em = _emf.createEntityManager();

        try {
            req.setAttribute(
                SessionKeys.EM_KEY,
                em);

            super.execute(req, resp);

        } finally {
            em.close();
        }
    }
}
