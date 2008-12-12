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

import java.util.UUID;

import javax.ejb.EJB;

import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.Resource;
import ccc.services.AuditLog;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class BaseResourceDao extends BaseDao {

    @EJB(name="AuditLog") protected AuditLog _audit;

    protected void create(final UUID folderId, final Resource newResource) {
        final Folder folder = find(Folder.class, folderId);
        if (null==folder) {
            throw new CCCException("No folder exists with id: "+folderId);
        }
        folder.add(newResource);
        _em.persist(newResource);
        _audit.recordCreate(newResource);
    }
}
