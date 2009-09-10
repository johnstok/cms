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
package ccc.persistence.jpa;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ccc.domain.Entity;
import ccc.domain.EntityNotFoundException;
import ccc.persistence.Repository;
import ccc.types.DBC;


/**
 * Implementation of {@link Repository} interface using a JPA entity manager.
 *
 * @author Civic Computing Ltd.
 */
public class JpaRepository implements Repository {

    private final EntityManager _em;

    /**
     * Constructor.
     *
     * @param em The JPA entity manager for this DAO.
     */
    public JpaRepository(final EntityManager em) {
        _em = em;
    }


    /** {@inheritDoc} */
    public <T extends Entity> T find(final Class<T> type, final UUID id)
    throws EntityNotFoundException {
        DBC.require().notNull(id);
        final T entity = _em.find(type, id);
        if (null==entity) {
            throw new EntityNotFoundException(id);
        }
        return entity;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked") // JPA query API isn't type safe.
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
    public <T> Collection<T> uniquify(final String queryName,
                                      final Class<T> resultType,
                                      final Object... params) {
        return new HashSet<T>(list(queryName, resultType, params));
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked") // JPA query API isn't type safe.
    public <T> T find(final String queryName,
                      final Class<T> resultType,
                      final Object... params) throws EntityNotFoundException {

        final Query q = _em.createNamedQuery(queryName);
        for (int i=0; i<params.length; i++) {
            q.setParameter((i+1), params[i]);
        }

        try { // Should we handle the possibility of multiple results?
            return (T) q.getSingleResult();
        } catch (final NoResultException e) {
            throw new EntityNotFoundException(null);
        }
    }

    /** {@inheritDoc} */
    public <T> boolean exists(final String queryName,
                              final Class<T> resultType,
                              final Object... params) {
        try {
            find(queryName, resultType, params);
            return true;
        } catch (final EntityNotFoundException e) {
            return false;
        }
    }

    /** {@inheritDoc} */
    public void create(final Entity entity) {
        _em.persist(entity);
    }
}
