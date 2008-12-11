/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3.local;

import static javax.ejb.TransactionAttributeType.*;

import java.util.List;
import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ccc.commons.DBC;
import ccc.domain.Entity;
import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.ResourceName;
import ccc.domain.Setting;
import ccc.domain.Setting.Name;
import ccc.services.QueryManager;


/**
 * QueryManagerEJB used by the business layer.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="QueryManager")
@TransactionAttribute(REQUIRED)
@Local(QueryManager.class)
public final class QueryManagerEJB implements QueryManager {

    @PersistenceContext(unitName = "ccc-persistence")
    private EntityManager _em;

    /**
     * Constructor.
     */
    @SuppressWarnings("unused")
    private QueryManagerEJB() { /* NO-OP */ }


    /**
     * Constructor.
     * @param em The entity manager used to perform queries.
     */
    QueryManagerEJB(final EntityManager em) {
        DBC.require().notNull(em);
        _em = em;
    }

    /** {@inheritDoc} */
    public Folder lookupRoot(final ResourceName name) {

        final Query q = _em.createNamedQuery("resourcesByName");
        q.setParameter("name", name);

        try {
            final Object singleResult = q.getSingleResult();
            final Folder folder = Folder.class.cast(singleResult);
            return folder;
        } catch (final NoResultException e) {
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public Setting findSetting(final Name name) {
        final Query q = _em.createNamedQuery("settingsByName");
        q.setParameter("name", name);

        try {
            final Setting s = (Setting) q.getSingleResult();
            return s;
        } catch (final NoResultException e) {
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public Folder findContentRoot() {
        return lookupRoot(new ResourceName(PredefinedResourceNames.CONTENT));
    }

    /** {@inheritDoc} */
    @Override
    public Folder findAssetsRoot() {
        return lookupRoot(new ResourceName(PredefinedResourceNames.ASSETS));
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked") // JPA query API isn't type safe.
    @Override
    public <T> List<T> list(final String queryName,
                            final Class<T> resultType,
                            final Object... params) {

        final Query q = _em.createNamedQuery(queryName);
        for (int i=0; i<params.length; i++) {
            q.setParameter((i+1), params[i]);
        }
        return q.getResultList();

    }


    /** {@inheritDoc} */
    @Override
    public <T extends Entity> T find(final Class<T> type, final String id) {
        return find(type, UUID.fromString(id));
    }


    /** {@inheritDoc} */
    @Override
    public <T extends Entity> T find(final Class<T> type, final UUID id) {
        return _em.find(type, id);
    }
}
