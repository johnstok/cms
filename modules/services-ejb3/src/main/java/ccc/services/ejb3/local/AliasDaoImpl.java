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

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.domain.Alias;
import ccc.domain.Resource;
import ccc.services.AliasDao;
import ccc.services.AuditLog;
import ccc.services.ResourceDao;
import ccc.services.UserManager;
import ccc.services.ejb3.support.BaseDao;
import ccc.services.ejb3.support.Dao;


/**
 * DAO with methods specific to a template.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=AliasDao.NAME)
@TransactionAttribute(REQUIRED)
@Local(AliasDao.class)
public class AliasDaoImpl implements AliasDao {

    @EJB(name=UserManager.NAME) private UserManager    _users;
    @PersistenceContext private EntityManager _em;
    private ResourceDao _dao;


    /** Constructor. */
    @SuppressWarnings("unused") public AliasDaoImpl() { super(); }

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     */
    public AliasDaoImpl(final ResourceDao dao) {
        _dao = dao;
    }


    /** {@inheritDoc} */
    @Override
    public void updateAlias(final UUID targetId,
                            final UUID aliasId) {
        final Resource target = _dao.find(Resource.class, targetId);
        final Alias alias = _dao.findLocked(Alias.class, aliasId);

        alias.target(target);

        _dao.update(alias);
    }

    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        final Dao bdao = new BaseDao(_em);
        final AuditLog audit = new AuditLogEJB(_em);
        _dao = new ResourceDaoImpl(_users, audit, bdao);
    }
}
