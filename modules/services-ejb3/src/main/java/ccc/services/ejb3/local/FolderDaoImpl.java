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

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;

import ccc.domain.Folder;
import ccc.domain.Resource;
import ccc.services.AuditLog;
import ccc.services.FolderDao;
import ccc.services.ejb3.support.BaseResourceDao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="FolderDao")
@TransactionAttribute(REQUIRED)
@Local(FolderDao.class)
public class FolderDaoImpl extends BaseResourceDao implements FolderDao {

    /** Constructor. */
    @SuppressWarnings("unused") private FolderDaoImpl() { super(); }

    /**
     * Constructor.
     *
     * @param em
     * @param al
     */
    public FolderDaoImpl(final EntityManager em, final AuditLog al) {
        _em = em;
        _audit = al;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Folder create(final UUID folderId, final Folder newFolder) {
        create(folderId, (Resource) newFolder);
        return newFolder;
    }

    /** {@inheritDoc} */
    @Override
    public Collection<Folder> roots() {
        return list("roots", Folder.class);
    }

    /** {@inheritDoc} */
    @Override
    public void createRoot(final Folder f) {
        // TODO: Factor into BaseResourceDao
        _em.persist(f);
        _audit.recordCreate(f);
    }
}
