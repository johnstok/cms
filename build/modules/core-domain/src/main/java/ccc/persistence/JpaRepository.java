/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.persistence;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ccc.domain.Entity;
import ccc.domain.EntityNotFoundException;
import ccc.types.DBC;


/**
 * Implementation of {@link Repository} interface using a JPA entity manager.
 *
 * @author Civic Computing Ltd.
 */
class JpaRepository implements Repository {

    private final EntityManager _em;

    /**
     * Constructor.
     *
     * @param em The JPA entity manager for this DAO.
     */
    JpaRepository(final EntityManager em) {
        DBC.require().notNull(em);
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