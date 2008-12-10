/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see SubVersion log
 *-----------------------------------------------------------------------------
 */

package ccc.services.ejb3;

import static javax.ejb.TransactionAttributeType.*;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.services.QueryManagerLocal;
import ccc.services.StatefulReader;


/**
 * EJB3 implementation of {@link StatefulReader}.
 *
 * @author Civic Computing Ltd
 */
@Stateful(name="StatefulReader")
@TransactionAttribute(REQUIRED)
@Local(StatefulReader.class)
public final class StatefulReaderEJB
    implements
        StatefulReader {

    @PersistenceContext(
        unitName = "ccc-persistence",
        type=PersistenceContextType.EXTENDED)
    private EntityManager _em;

    @EJB(name="QueryManager", beanInterface=QueryManagerLocal.class)
    private QueryManagerLocal _qm;

    /**
     * Constructor.
     */
    @SuppressWarnings("unused")
    private StatefulReaderEJB() { /* NO-OP */ }

    /**
     * Constructor.
     *
     * @param entityManager A JPA entity manager.
     * @param queryManager A CCC QueryManager.
     */
    StatefulReaderEJB(final EntityManager entityManager,
                      final QueryManagerLocal queryManager) {
        _em = entityManager;
        _qm = queryManager;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Resource lookup(final ResourcePath path) {
        final Folder contentRoot = _qm.findContentRoot();
        try {
            return contentRoot.navigateTo(path);
        } catch (final CCCException e) {
            return null;
        }
    }
}
