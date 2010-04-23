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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.api.Folders;
import ccc.api.dto.FolderDto;
import ccc.api.dto.ResourceSummary;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourceOrder;
import ccc.commands.UpdateFolderCommand;
import ccc.domain.Folder;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.domain.sorting.Sorter;


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
    public ResourceSummary createFolder(final FolderDto folder) {
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

        final User u = currentUser();
        final Date happenedOn = new Date();

        final Folder f =
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
        final Folder f = new Folder(name);
        commands().createRootCommand(f)
                  .execute(currentUser(), new Date());
        return f.mapResource();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(FOLDER_UPDATE)
    public void updateFolder(final UUID folderId,
                             final FolderDto delta) {
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
    @RolesAllowed(FOLDER_READ)
    public Collection<ResourceSummary> getChildrenManualOrder(
                                                        final UUID folderId) {
        final Folder f =
            getRepoFactory()
                .createResourceRepository()
                .find(Folder.class, folderId);

        checkRead(f);

        if (f != null) {
            final List<Resource> list = f.getEntries();
            Sorter.sort(list, ResourceOrder.MANUAL);
            return Resource.mapResources(list);
        }
        return Resource.mapResources(new ArrayList<Resource>());
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
                    .find(Folder.class, folderId)
                    .hasEntryWithName(new ResourceName(name)));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(FOLDER_READ)
    public Collection<ResourceSummary> roots() {
        return
            Resource.mapResources(
                getRepoFactory()
                    .createResourceRepository()
                    .roots());
    }

    /* ====================================================================
     * UNSAFE METHODS.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    @PermitAll
    public Collection<ResourceSummary> getFolderChildren(final UUID folderId) {
        checkPermission(RESOURCE_READ);

        final Folder f =
            getRepoFactory()
                .createResourceRepository()
                .find(Folder.class, folderId);

        checkRead(f);

        final List<Folder> folderChildren = f.getFolders();
        Sorter.sort(folderChildren, ResourceOrder.NAME_ALPHANUM_CI_ASC);
        return Resource.mapResources(folderChildren);
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public Collection<ResourceSummary> getChildren(final UUID folderId) {
        checkPermission(RESOURCE_READ);

        final Folder f =
            getRepoFactory()
                .createResourceRepository()
                .find(Folder.class, folderId);

        checkRead(f);

        return Resource.mapResources(f.getEntries());
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public Collection<ResourceSummary> getAccessibleChildren(final UUID folderId) {
        checkPermission(RESOURCE_READ);

        final Folder f =
            getRepoFactory()
                .createResourceRepository()
                .find(Folder.class, folderId);

        checkRead(f);

        final List<Resource> filtered = new ArrayList<Resource>();
        final User user = currentUser();
        for (final Resource r : f.getEntries()) {
            if (r.isReadableBy(user)) {
                filtered.add(r);
            }
        }
        return Resource.mapResources(filtered);
    }

}
