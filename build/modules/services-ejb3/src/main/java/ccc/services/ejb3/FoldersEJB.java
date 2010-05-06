/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3;

import static ccc.api.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.api.core.Folder;
import ccc.api.core.Folders;
import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceSummary;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourceOrder;
import ccc.commands.UpdateFolderCommand;
import ccc.domain.FolderEntity;
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;


/**
 * EJB implementation of the {@link FoldersExt} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Folders.NAME)
@TransactionAttribute(REQUIRED)
@Local(Folders.class)
@RolesAllowed({})
public class FoldersEJB
    extends
        AbstractEJB
    implements
        Folders {


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public ResourceSummary createFolder(final Folder folder) {
        checkPermission(FOLDER_CREATE);

        return createFolder(
            folder.getParent(), folder.getName().toString(), null, false);

    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public ResourceSummary createFolder(final UUID parentId,
                                        final String name,
                                        final String title,
                                        final boolean publish) {
        checkPermission(FOLDER_CREATE);

        final UserEntity u = currentUser();
        final Date happenedOn = new Date();

        final FolderEntity f =
            commands().createFolderCommand(parentId, name, title)
            .execute(u, happenedOn);

        if (publish) {
            f.lock(u);
            commands().publishResource(f.getId()).execute(u, happenedOn);
            f.unlock(u);
        }

        return f.mapResource();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(ROOT_CREATE)
    public ResourceSummary createRoot(final String name) {
        final FolderEntity f = new FolderEntity(name);
        commands().createRootCommand(f)
                  .execute(currentUser(), new Date());
        return f.mapResource();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(FOLDER_UPDATE)
    public void updateFolder(final UUID folderId,
                             final Folder delta) {
        final List<UUID> list = new ArrayList<UUID>();

        for (final String item : delta.getSortList()) {
            list.add(UUID.fromString(item));
        }

        new UpdateFolderCommand(
            getRepoFactory(),
            folderId,
            ResourceOrder.valueOf(delta.getSortOrder()),
            delta.getIndexPage(),
            list)
        .execute(
            currentUser(),
             new Date());
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public Boolean nameExistsInFolder(final UUID folderId, final String name) {
        checkPermission(FOLDER_READ);

        // TODO: handle null folderId? (for root folders)
        // FIXME checkRead(f); ?

        return
            Boolean.valueOf(
                getRepoFactory()
                    .createResourceRepository()
                    .find(FolderEntity.class, folderId)
                    .hasEntryWithName(new ResourceName(name)));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(FOLDER_READ)
    public PagedCollection<ResourceSummary> roots() {
        final List<ResourceSummary> roots = ResourceEntity.mapResources(
            getRepoFactory()
            .createResourceRepository()
            .roots());
        return new PagedCollection<ResourceSummary>(roots.size(), roots);
    }

    /* ====================================================================
     * UNSAFE METHODS.
     * ================================================================== */


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public PagedCollection<ResourceSummary> getAccessibleChildren(
        final UUID folderId) {
        checkPermission(RESOURCE_READ);

        final FolderEntity f =
            getRepoFactory()
                .createResourceRepository()
                .find(FolderEntity.class, folderId);

        checkRead(f);

        final List<ResourceEntity> filtered = new ArrayList<ResourceEntity>();
        final UserEntity user = currentUser();
        for (final ResourceEntity r : f.getEntries()) {
            if (r.isReadableBy(user)) {
                filtered.add(r);
            }
        }
        final List<ResourceSummary> entities =
            ResourceEntity.mapResources(filtered);
        return new PagedCollection<ResourceSummary>(entities.size(), entities);
    }

}
