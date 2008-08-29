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

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import ccc.commons.DBC;
import ccc.domain.Folder;
import ccc.domain.ResourceName;
import ccc.domain.Setting;
import ccc.domain.Setting.Name;
import ccc.services.QueryManager;


/**
 * Queries used by the business layer.
 *
 * @author Civic Computing Ltd.
 */
@Stateful
@TransactionAttribute(REQUIRED)
@Remote(QueryManager.class)
@Local(QueryManager.class)
public final class Queries implements QueryManager {

    private final EntityManager _em;

    /**
     * Constructor.
     * @param em The entity manager used to perform queries.
     */
    Queries(final EntityManager em) {
        DBC.require().notNull(em);
        _em = em;
    }

    /**
     * Look up the root folder for the content hierarchy.
     *
     * @param name The name of the resource.
     * @return The folder with the specified name.
     */
    protected Folder lookupRoot(final ResourceName name) {

        final Query q = _em.createNamedQuery(RESOURCE_BY_URL);
        q.setParameter("name", name);
        final Object singleResult = q.getSingleResult();

        final Folder folder = Folder.class.cast(singleResult);
        return folder;
    }

    /** {@inheritDoc} */
    @Override
    public Setting findSetting(final Name name) {
        final Query q =
            _em.createQuery(NamedQueries.SETTING_BY_NAME.queryString());
        q.setParameter("name", name);
        return (Setting) q.getSingleResult();
    }

    /** RESOURCE_BY_URL : String. */
    public static final String RESOURCE_BY_URL = "RESOURCE_BY_URL";

    /**
     * Available named queries.
     *
     * @author Civic Computing Ltd.
     */
    private static enum NamedQueries {

        SETTING_BY_NAME("from Setting s where s.name=:name");

        private final String _queryString;

        private NamedQueries(final String qString) {
            _queryString = qString;
        }

        String queryString() {
            return _queryString;
        }
    }

}
