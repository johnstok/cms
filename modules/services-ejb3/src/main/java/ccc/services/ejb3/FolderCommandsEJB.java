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

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.commands.CreateFolderCommand;
import ccc.commands.CreateRootCommand;
import ccc.commands.PublishCommand;
import ccc.commands.UpdateFolderCommand;
import ccc.domain.CccCheckedException;
import ccc.domain.Folder;
import ccc.domain.Resource;
import ccc.domain.ResourceExistsException;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.LogEntryRepositoryImpl;
import ccc.persistence.QueryNames;
import ccc.persistence.ResourceRepositoryImpl;
import ccc.persistence.UserRepositoryImpl;
import ccc.persistence.jpa.JpaRepository;
import ccc.rest.CommandFailedException;
import ccc.rest.Folders;
import ccc.rest.dto.FolderDelta;
import ccc.rest.dto.FolderNew;
import ccc.rest.dto.ResourceSummary;
import ccc.types.ID;
import ccc.types.ResourceName;
import ccc.types.ResourceOrder;


/**
 * EJB implementation of the {@link Folders} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Folders.NAME)
@TransactionAttribute(REQUIRES_NEW)
@Remote(Folders.class)
@RolesAllowed({})
public class FolderCommandsEJB
    extends
        BaseCommands
    implements
        Folders {

    @PersistenceContext private EntityManager _em;
    @javax.annotation.Resource private EJBContext _context;
    private LogEntryRepository           _audit;

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public ResourceSummary createFolder(final FolderNew folder)
    throws CommandFailedException {
        return createFolder(
            folder.getParentId(), folder.getName(), null, false);

    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public ResourceSummary createFolder(final ID parentId,
                                        final String name,
                                        final String title,
                                        final boolean publish)
    throws CommandFailedException {
        return createFolder(
            parentId,
            name,
            title,
            publish,
            loggedInUserId(_context),
            new Date());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public ResourceSummary createFolder(final ID parentId,
                                        final String name,
                                        final String title,
                                        final boolean publish,
                                        final ID actorId,
                                        final Date happenedOn)
    throws CommandFailedException {
        try {
            final User u = userForId(actorId);

            final Folder f =
                new CreateFolderCommand(_bdao, _audit).execute(
                    u, happenedOn, toUUID(parentId), name, title);

            if (publish) {
                f.lock(u);
                new PublishCommand(_audit).execute(happenedOn, u, f);
                f.unlock(u);
            }

            return mapResource(f);

        } catch (final CccCheckedException e) {
            throw fail(_context, e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public ResourceSummary createRoot(final String name)
                                                 throws CommandFailedException {
        try {
            final Folder f = new Folder(name);
            new CreateRootCommand(_bdao, _audit).execute(
                loggedInUser(_context), new Date(), f);
            return mapResource(f);
        } catch (final ResourceExistsException e) {
            throw fail(_context, e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateFolder(final ID folderId,
                             final FolderDelta delta)
                                                 throws CommandFailedException {
        try {
            final List<UUID> list = new ArrayList<UUID>();

            for (final String item : delta.getSortList()) {
                list.add(UUID.fromString(item));
            }

            new UpdateFolderCommand(_bdao, _audit).execute(
                loggedInUser(_context),
                 new Date(),
                 toUUID(folderId),
                 ResourceOrder.valueOf(delta.getSortOrder()),
                 toUUID(delta.getIndexPage()),
                 list);

        } catch (final CccCheckedException e) {
            throw fail(_context, e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<ResourceSummary> getChildren(final ID folderId) {
        final Folder f =
            _resources.find(Folder.class, toUUID(folderId));
        return mapResources(
            f != null ? f.entries() : new ArrayList<Resource>());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<ResourceSummary> getChildrenManualOrder(
                                                            final ID folderId) {
        final Folder f =
            _resources.find(Folder.class, toUUID(folderId));
        if (f != null) {
            f.sortOrder(ResourceOrder.MANUAL);
            return mapResources(f.entries());
        }
        return mapResources(new ArrayList<Resource>());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<ResourceSummary> getFolderChildren(final ID folderId) {
        final Folder f =
            _resources.find(Folder.class, toUUID(folderId));
        return mapResources(f != null ? f.folders() : new ArrayList<Folder>());
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public boolean nameExistsInFolder(final ID folderId, final String name) {
        // TODO handle null folderId? (for root folders)
        return
        _resources.find(Folder.class, toUUID(folderId))
                  .hasEntryWithName(new ResourceName(name));
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<ResourceSummary> roots() {
        return mapResources(_resources.list(QueryNames.ROOTS, Folder.class));
    }


    /* ==============
     * Helper methods
     * ============== */
    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _bdao = new JpaRepository(_em);
        _audit = new LogEntryRepositoryImpl(_bdao);
        _users = new UserRepositoryImpl(_bdao);
        _resources = new ResourceRepositoryImpl(_bdao);
    }

}
