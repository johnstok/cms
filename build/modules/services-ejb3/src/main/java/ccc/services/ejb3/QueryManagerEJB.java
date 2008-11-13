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
package ccc.services.ejb3;

import static javax.ejb.TransactionAttributeType.*;
import static javax.persistence.PersistenceContextType.*;

import java.util.List;
import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ccc.commons.DBC;
import ccc.commons.Maybe;
import ccc.domain.VersionedEntity;
import ccc.domain.Folder;
import ccc.domain.ResourceName;
import ccc.domain.Setting;
import ccc.domain.Setting.Name;
import ccc.services.QueryManagerLocal;


/**
 * QueryManagerEJB used by the business layer.
 *
 * @author Civic Computing Ltd.
 */
@Stateful(name="QueryManager")
@TransactionAttribute(REQUIRED)
@Local(QueryManagerLocal.class)
public final class QueryManagerEJB implements QueryManagerLocal {

    @PersistenceContext(
        unitName = "ccc-persistence",
        type     = EXTENDED)
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
    public Maybe<Folder> lookupRoot(final ResourceName name) {

        final Query q = _em.createNamedQuery("resourcesByName");
        q.setParameter("name", name);

        try {
            final Object singleResult = q.getSingleResult();
            final Folder folder = Folder.class.cast(singleResult);
            return new Maybe<Folder>(folder);
        } catch (final NoResultException e) {
            return new Maybe<Folder>();
        }
    }

    /** {@inheritDoc} */
    @Override
    public Maybe<Setting> findSetting(final Name name) {
        final Query q = _em.createNamedQuery("settingsByName");
        q.setParameter("name", name);

        try {
            final Setting s = (Setting) q.getSingleResult();
            return new Maybe<Setting>(s);
        } catch (final NoResultException e) {
            return new Maybe<Setting>();
        }
    }

    /** {@inheritDoc} */
    @Override
    public Maybe<Folder> findContentRoot() {
        final Maybe<Setting> rootId = findSetting(Name.CONTENT_ROOT_FOLDER_ID);

        if (rootId.isDefined()) {
            final Folder root =
                _em.find(
                    Folder.class,
                    UUID.fromString(rootId.get().value()));
            return new Maybe<Folder>(root);
        }

        return new Maybe<Folder>();
    }

    /** {@inheritDoc} */
    @Override
    public Maybe<Folder> findAssetsRoot() {
        final Maybe<Setting> rootId = findSetting(Name.ASSETS_ROOT_FOLDER_ID);

        if (rootId.isDefined()) {
            final Folder root =
                _em.find(
                    Folder.class,
                    UUID.fromString(rootId.get().value()));
            return new Maybe<Folder>(root);
        }

        return new Maybe<Folder>();
    }

    /** {@inheritDoc} */
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
    public <T extends VersionedEntity> T find(final Class<T> type, final String id) {
        return find(type, UUID.fromString(id));
    }


    /** {@inheritDoc} */
    @Override
    public <T extends VersionedEntity> T find(final Class<T> type, final UUID id) {
        return _em.find(type, id);
    }
}
