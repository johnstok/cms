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

import org.apache.log4j.Logger;



/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PersistenceAction
    implements
        ServletAction {
    private static final Logger LOG = Logger.getLogger(PersistenceAction.class);

    private final EntityManagerFactory _emf;
    private final ServletAction _delegate;

    /**
     * Constructor.
     *
     * @param delegate
     * @param utx
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
            req.setAttribute(SessionKeys.PERSISTENCE_KEY, em);
            _delegate.execute(req, resp);
        } finally {
            em.close();
        }
    }

}
