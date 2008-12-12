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
package ccc.services.ejb3.support;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ccc.domain.CCCException;
import ccc.domain.Entity;
import ccc.domain.Resource;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class BaseDao {

    @PersistenceContext(unitName = "ccc-persistence")
    public EntityManager _em; // TODO: Make private

    /**
     * TODO: Add a description of this method.
     * TODO: Remove - use UUID version instead.
     *
     * @param <T>
     * @param type
     * @param id
     * @return
     */
    @Deprecated
    public <T extends Entity> T find(final Class<T> type, final String id) {
        return find(type, UUID.fromString(id));
    }

    public <T extends Entity> T find(final Class<T> type, final UUID id) {
        return _em.find(type, id);
    }

    protected <T extends Resource> T find(final Class<T> type,
                                          final UUID id,
                                          final long version) {
          final T current = find(type, id);
          if (!(current.version()==version)) { // Move to Resource class
              throw new CCCException("Stale object"); // Use better exception
          }
          return current;
      }

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

    public <T> Collection<T> uniquify(final Collection<T> collection) {
        return new HashSet<T>(collection);
    }

    @SuppressWarnings("unchecked") // JPA query API isn't type safe.
    public <T> T find(final String queryName,
                        final Class<T> resultType,
                        final Object... params) {

        final Query q = _em.createNamedQuery(queryName);
        for (int i=0; i<params.length; i++) {
            q.setParameter((i+1), params[i]);
        }

        try {
            return (T) q.getSingleResult();
        } catch (final NoResultException e) {
            return null;
        }
    }

    public boolean exists(final Object o) {
        return null!=o;
    }
}
