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

import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;

import ccc.domain.Alias;
import ccc.domain.Resource;
import ccc.services.AliasDao;
import ccc.services.AuditLog;
import ccc.services.ejb3.support.BaseResourceDao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="AliasDao")
@TransactionAttribute(REQUIRED)
@Local(AliasDao.class)
public class AliasDaoImpl extends BaseResourceDao implements AliasDao {

    /** Constructor. */
    @SuppressWarnings("unused") private AliasDaoImpl() { super(); }

    /**
     * Constructor.
     *
     * @param em
     * @param al
     */
    public AliasDaoImpl(final EntityManager em, final AuditLog al) {
        _em = em;
        _audit =al;
    }


    /** {@inheritDoc} */
    @Override
    public void create(final UUID folderId, final Alias alias) {
        create(folderId, (Resource) alias);
    }


    /** {@inheritDoc} */
    @Override
    public void updateAlias(final UUID targetId, final UUID aliasId, final long aliasVersion) {
        final Resource target = find(Resource.class, targetId);
        final Alias alias = find(Alias.class, aliasId, aliasVersion);

        alias.target(target);
        _audit.recordUpdate(alias);
    }
}
