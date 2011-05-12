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

import static ccc.api.types.Permission.FOLDER_CREATE;
import static ccc.api.types.Permission.FOLDER_READ;
import static ccc.api.types.Permission.FOLDER_UPDATE;
import static ccc.api.types.Permission.RESOURCE_READ;
import static ccc.api.types.Permission.ROOT_CREATE;
import static javax.ejb.TransactionAttributeType.REQUIRED;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.api.core.Folder;
import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceSummary;
import ccc.api.synchronous.Folders;
import ccc.api.types.ResourceName;
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
public class FoldersEJB
    extends
        AbstractEJB
    implements
        Folders {


    /** {@inheritDoc} */
    @Override
    public Folder create(final Folder folder) {
        checkPermission(FOLDER_CREATE);

        return createFolder(
            folder.getParent(), folder.getName().toString(), null, false);

    }


    /** {@inheritDoc} */
    @Override
    public Folder createFolder(final UUID parentId,
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

        return f.forCurrentRevision();
    }


    /** {@inheritDoc} */
    @Override
    public Folder createRoot(final String name) {
        checkPermission(ROOT_CREATE);

        final FolderEntity f = new FolderEntity(name);
        commands().createRootCommand(f)
                  .execute(currentUser(), new Date());
        return f.forCurrentRevision();
    }


    /** {@inheritDoc} */
    @Override
    public Folder update(final UUID folderId,
                         final Folder delta) {
        checkPermission(FOLDER_UPDATE);

        final List<UUID> list = new ArrayList<UUID>();

        for (final String item : delta.getSortList()) {
            list.add(UUID.fromString(item));
        }

        return
            execute(
                new UpdateFolderCommand(
                    getRepoFactory(),
                    folderId,
                    delta.getIndex(),
                    list));
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary nameExistsInFolder(final UUID folderId,
                                              final String name) {
        checkPermission(FOLDER_READ);

        // TODO: handle null folderId? (for root folders)
        // FIXME checkRead(f); ?
        final ResourceEntity e =
            getRepoFactory()
                .createResourceRepository()
                .find(FolderEntity.class, folderId)
                .getEntryWithName(new ResourceName(name));

        return (null==e) ? null : e.mapResource();

    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<ResourceSummary> roots() {
        checkPermission(FOLDER_READ);

        final List<ResourceSummary> roots = ResourceEntity.mapResources(
            getRepoFactory()
            .createResourceRepository()
            .roots());

        final PagedCollection<ResourceSummary> rootCollection =
            new PagedCollection<ResourceSummary>(
                roots.size(), ResourceSummary.class, roots);
        rootCollection.addLink(
            "element",
            ccc.api.synchronous.ResourceIdentifiers.Resource.ELEMENT);

        return rootCollection;
    }

    /* ====================================================================
     * UNSAFE METHODS.
     * ================================================================== */


    /** {@inheritDoc} */
    @Override
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
        return
            new PagedCollection<ResourceSummary>(
                entities.size(), ResourceSummary.class, entities);
    }
}
