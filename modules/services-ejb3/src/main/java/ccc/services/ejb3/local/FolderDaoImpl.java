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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.domain.Folder;
import ccc.domain.Resource;
import ccc.domain.ResourceOrder;
import ccc.domain.User;
import ccc.persistence.jpa.BaseDao;
import ccc.services.AuditLog;
import ccc.services.AuditLogEJB;
import ccc.services.Dao;
import ccc.services.FolderDao;
import ccc.services.ResourceDao;
import ccc.services.ResourceDaoImpl;
import ccc.services.UserManager;


/**
 * EJB implementation of the {@link FolderDao} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=FolderDao.NAME)
@TransactionAttribute(REQUIRED)
@Local(FolderDao.class)
public class FolderDaoImpl implements FolderDao {

    @PersistenceContext private EntityManager _em;
    private  ResourceDao    _dao;
    private  AuditLog       _audit;
    @EJB(name=UserManager.NAME) private UserManager    _users;

    /** Constructor. */
    @SuppressWarnings("unused") public FolderDaoImpl() { super(); }

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     */
    public FolderDaoImpl(final ResourceDao dao,
                         final AuditLog audit,
                         final UserManager users) {
        _dao = dao;
        _audit = audit;
        _users = users;
    }


    /** {@inheritDoc} */
    @Override
    public Collection<Folder> roots() {
        return _dao.list("roots", Folder.class);
    }

    /** {@inheritDoc} */
    @Override
    public void updateSortOrder(final UUID folderId,
                                final ResourceOrder order) {

        final Folder f = _dao.findLocked(Folder.class, folderId);
        final User u = _users.loggedInUser();
        f.sortOrder(order);
        _audit.recordUpdateSortOrder(f, u, new Date());
    }

    /** {@inheritDoc} */
    @Override
    public void reorder(final UUID folderId, final List<UUID> order) {
        final Folder f = _dao.findLocked(Folder.class, folderId);
        final User u = _users.loggedInUser();
        final List<Resource> newOrder = new ArrayList<Resource>();
        final List<Resource> currentOrder = f.entries();
        for (final UUID resourceId : order) {
            for (final Resource r : currentOrder) {
                if (r.id().equals(resourceId)) {
                    newOrder.add(r);
                }
            }
        }
        f.reorder(newOrder);
        _audit.recordReorder(f, u, new Date());
    }

    @PostConstruct
    public void configure() {
        final Dao bdao = new BaseDao(_em);
        _audit = new AuditLogEJB(bdao);
        _dao = new ResourceDaoImpl(_users, _audit, bdao);
    }
}
