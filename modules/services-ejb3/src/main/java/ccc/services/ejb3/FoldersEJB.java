/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import static ccc.types.CreatorRoles.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.commands.CreateFolderCommand;
import ccc.commands.CreateRootCommand;
import ccc.commands.PublishCommand;
import ccc.commands.UpdateFolderCommand;
import ccc.domain.CccCheckedException;
import ccc.domain.Folder;
import ccc.domain.Resource;
import ccc.domain.ResourceExistsException;
import ccc.domain.User;
import ccc.persistence.QueryNames;
import ccc.rest.RestException;
import ccc.rest.Folders;
import ccc.rest.dto.FolderDelta;
import ccc.rest.dto.FolderDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.extensions.FoldersExt;
import ccc.types.ResourceName;
import ccc.types.ResourceOrder;


/**
 * EJB implementation of the {@link FoldersExt} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Folders.NAME)
@TransactionAttribute(REQUIRES_NEW)
@Remote(FoldersExt.class)
@RolesAllowed({})
public class FoldersEJB
    extends
        AbstractEJB
    implements
        FoldersExt {


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public ResourceSummary createFolder(final FolderDto folder)
    throws RestException {
        return createFolder(
            folder.getParentId(), folder.getName(), null, false);

    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public ResourceSummary createFolder(final UUID parentId,
                                        final String name,
                                        final String title,
                                        final boolean publish)
    throws RestException {
        return createFolder(
            parentId,
            name,
            title,
            publish,
            currentUserId(),
            new Date());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public ResourceSummary createFolder(final UUID parentId,
                                        final String name,
                                        final String title,
                                        final boolean publish,
                                        final UUID actorId,
                                        final Date happenedOn)
    throws RestException {
        try {
            final User u = userForId(actorId);

            final Folder f =
                new CreateFolderCommand(_bdao, _audit).execute(
                    u, happenedOn, parentId, name, title);

            if (publish) {
                f.lock(u);
                new PublishCommand(_audit).execute(happenedOn, u, f);
                f.unlock(u);
            }

            return mapResource(f);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public ResourceSummary createRoot(final String name)
                                                 throws RestException {
        try {
            final Folder f = new Folder(name);
            new CreateRootCommand(_bdao, _audit).execute(
                currentUser(), new Date(), f);
            return mapResource(f);
        } catch (final ResourceExistsException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateFolder(final UUID folderId,
                             final FolderDelta delta)
                                                 throws RestException {
        try {
            final List<UUID> list = new ArrayList<UUID>();

            for (final String item : delta.getSortList()) {
                list.add(UUID.fromString(item));
            }

            new UpdateFolderCommand(_bdao, _audit).execute(
                currentUser(),
                 new Date(),
                 folderId,
                 ResourceOrder.valueOf(delta.getSortOrder()),
                 delta.getIndexPage(),
                 list);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<ResourceSummary> getChildren(final UUID folderId) {
        final Folder f =
            _resources.find(Folder.class, folderId);
        return mapResources(
            f != null ? f.entries() : new ArrayList<Resource>());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<ResourceSummary> getChildrenManualOrder(
                                                        final UUID folderId) {
        final Folder f =
            _resources.find(Folder.class, folderId);
        if (f != null) {
            f.sortOrder(ResourceOrder.MANUAL);
            return mapResources(f.entries());
        }
        return mapResources(new ArrayList<Resource>());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<ResourceSummary> getFolderChildren(final UUID folderId) {
        final Folder f =
            _resources.find(Folder.class, folderId);
        return mapResources(f != null ? f.folders() : new ArrayList<Folder>());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public boolean nameExistsInFolder(final UUID folderId, final String name) {
        // TODO handle null folderId? (for root folders)
        return
        _resources.find(Folder.class, folderId)
                  .hasEntryWithName(new ResourceName(name));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<ResourceSummary> roots() {
        return mapResources(_resources.list(QueryNames.ROOTS, Folder.class));
    }
}
