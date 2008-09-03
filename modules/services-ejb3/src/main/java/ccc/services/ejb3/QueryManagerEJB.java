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

import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ccc.commons.DBC;
import ccc.commons.Maybe;
import ccc.domain.Folder;
import ccc.domain.ResourceName;
import ccc.domain.Setting;
import ccc.domain.Setting.Name;
import ccc.services.QueryManager;


/**
 * QueryManagerEJB used by the business layer.
 *
 * @author Civic Computing Ltd.
 */
@Stateful
@TransactionAttribute(REQUIRED)
@Remote(QueryManager.class)
@Local(QueryManager.class)
public final class QueryManagerEJB implements QueryManager {

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

        final Query q = _em.createNamedQuery(RESOURCE_BY_URL);
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
        final Query q =
            _em.createQuery(NamedQueries.SETTING_BY_NAME.queryString());
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

        if (rootId.isPresent()) {
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

        if (rootId.isPresent()) {
            final Folder root =
                _em.find(
                    Folder.class,
                    UUID.fromString(rootId.get().value()));
            return new Maybe<Folder>(root);
        }

        return new Maybe<Folder>();
    }

    /** RESOURCE_BY_URL : String. */
    public static final String RESOURCE_BY_URL = "RESOURCE_BY_URL";

    /**
     * Available named queries.
     *
     * @author Civic Computing Ltd.
     */
    static enum NamedQueries {

        /** SETTING_BY_NAME : NamedQueries. */
        SETTING_BY_NAME("from ccc.domain.Setting s where s._name=:name");

        private final String _queryString;

        private NamedQueries(final String qString) {
            _queryString = qString;
        }

        /**
         * Accessor for the query string.
         *
         * @return The JPA query as a string.
         */
        String queryString() {
            return _queryString;
        }
    }
}
