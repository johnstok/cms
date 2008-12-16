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

package ccc.services.ejb3.local;

import static javax.ejb.TransactionAttributeType.*;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
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
    @SuppressWarnings("unused")
    private EntityManager _em; // Required to insure method calls are stateful.


    /**
     * Constructor.
     */
    @SuppressWarnings("unused")
    public StatefulReaderEJB() { /* NO-OP */ }

    /**
     * Constructor.
     *
     * @param entityManager A JPA entity manager.
     */
    StatefulReaderEJB(final EntityManager entityManager) {
        _em = entityManager;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Resource lookup(final ResourcePath path) {
        final Folder contentRoot = lookupRoot();
        try {
            return contentRoot.navigateTo(path);
        } catch (final CCCException e) {
            return null;
        }
    }

    private Folder lookupRoot() {
        final Query q = _em.createNamedQuery("resourcesByName");
        q.setParameter(
            "name",
            new ResourceName(PredefinedResourceNames.CONTENT));

        try {
            final Object singleResult = q.getSingleResult();
            final Folder folder = Folder.class.cast(singleResult);
            return folder;
        } catch (final NoResultException e) {
            return null;
        }
    }
}
