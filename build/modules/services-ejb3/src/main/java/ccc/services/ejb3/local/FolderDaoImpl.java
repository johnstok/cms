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

import java.util.Collection;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.domain.Folder;
import ccc.domain.ResourceOrder;
import ccc.services.FolderDao;
import ccc.services.ResourceDao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="FolderDao")
@TransactionAttribute(REQUIRED)
@Local(FolderDao.class)
public class FolderDaoImpl implements FolderDao {

    @EJB(name="ResourceDao") private ResourceDao _dao;


    /** Constructor. */
    @SuppressWarnings("unused") public FolderDaoImpl() { super(); }

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     */
    public FolderDaoImpl(final ResourceDao dao) {
        _dao = dao;
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
        f.sortOrder(order);
    }
}
