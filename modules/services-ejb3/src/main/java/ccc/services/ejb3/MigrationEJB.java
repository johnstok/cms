/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3;

import static ccc.api.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.api.core.Page;
import ccc.api.core.ResourceSummary;
import ccc.api.types.ACL;
import ccc.commands.ApplyWorkingCopyCommand;
import ccc.commands.ChangeTemplateForResourceCommand;
import ccc.commands.IncludeInMainMenuCommand;
import ccc.commands.UpdatePageCommand;
import ccc.commands.UpdateResourceAclCommand;
import ccc.commands.UpdateResourceMetadataCommand;
import ccc.domain.FolderEntity;
import ccc.domain.PageEntity;
import ccc.domain.UserEntity;
import ccc.services.Migration;


/**
 * EJB3 implementation of the {@link Migration} API.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Migration.NAME)
@TransactionAttribute(REQUIRED)
@Remote(Migration.class)
@RolesAllowed(MIGRATE)
public class MigrationEJB
    extends
        AbstractEJB
    implements
        Migration {



    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final UUID parentId,
                                        final String name,
                                        final String title,
                                        final boolean publish,
                                        final UUID actorId,
                                        final Date happenedOn) {
        final UserEntity u = userForId(actorId);

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
    public ResourceSummary createPage(final UUID parentId,
                                      final Page delta,
                                      final boolean publish,
                                      final UUID actorId,
                                      final Date happenedOn) {
        final UserEntity u = userForId(actorId);

        final PageEntity p =
            commands()
                .createPageCommand(parentId, delta)
                .execute(u, happenedOn);

        if (publish) {
            p.lock(u);
            commands().publishResource(p.getId()).execute(u, happenedOn);
            p.unlock(u);
        }

        return p.mapResource();
    }


    /** {@inheritDoc} */
    @Override
    public void updatePage(final UUID pageId,
                           final Page delta,
                           final UUID actorId,
                           final Date happenedOn) {
        sudoExecute(
            new UpdatePageCommand(
                getRepoFactory(),
                pageId,
                delta),
            actorId,
            happenedOn);
    }



    /** {@inheritDoc} */
    @Override
    public void lock(final UUID resourceId,
                     final UUID actorId,
                     final Date happenedOn) {
        sudoExecute(
            commands().lockResourceCommand(resourceId), actorId, happenedOn);
    }



    /** {@inheritDoc} */
    @Override
    public void publish(final UUID resourceId,
                        final UUID userId,
                        final Date date) {
        sudoExecute(
            commands().publishResource(resourceId), userId, date);
    }



    /** {@inheritDoc} */
    @Override
    public void unlock(final UUID resourceId,
                       final UUID actorId,
                       final Date happenedOn) {
        sudoExecute(
            commands().unlockResourceCommand(resourceId), actorId, happenedOn);
    }



    /** {@inheritDoc} */
    @Override
    public void unpublish(final UUID resourceId,
                          final UUID userId,
                          final Date publishDate) {
        sudoExecute(
            commands().unpublishResourceCommand(resourceId),
            userId,
            publishDate);
    }



    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final UUID resourceId,
                                       final UUID templateId,
                                       final UUID actorId,
                                       final Date happenedOn) {
        new ChangeTemplateForResourceCommand(getRepoFactory())
            .execute(
                userForId(actorId),
                happenedOn,
                resourceId,
                templateId);
    }



    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final UUID resourceId,
                                  final boolean include,
                                  final UUID actorId,
                                  final Date happenedOn) {
        new IncludeInMainMenuCommand(getRepoFactory())
            .execute(userForId(actorId), happenedOn, resourceId, include);
    }



    /** {@inheritDoc} */
    @Override
    public void updateMetadata(final UUID resourceId,
                               final String title,
                               final String description,
                               final Set<String> tags,
                               final Map<String, String> metadata,
                               final UUID actorId,
                               final Date happenedOn) {
        new UpdateResourceMetadataCommand(getRepoFactory())
            .execute(
                userForId(actorId),
                happenedOn,
                resourceId,
                title,
                description,
                tags,
                metadata);
    }



    /** {@inheritDoc} */
    @Override
    public void changeAcl(final UUID resourceId,
                            final ACL acl,
                            final UUID actorId,
                            final Date happenedOn) {
        sudoExecute(
            new UpdateResourceAclCommand(
                getRepoFactory(),
                resourceId,
                acl),
            actorId,
            happenedOn);
    }



    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopy(final UUID resourceId,
                                 final UUID userId,
                                 final Date happenedOn,
                                 final boolean isMajorEdit,
                                 final String comment) {
        sudoExecute(
            new ApplyWorkingCopyCommand(
                getRepoFactory(),
                resourceId,
                comment,
                isMajorEdit),
            userId,
            happenedOn);
    }



    /** {@inheritDoc} */
    @Override
    public void deleteResource(final UUID resourceId,
                               final UUID actorId,
                               final Date happenedOn) {
        sudoExecute(
            commands().createDeleteResourceCmd(resourceId),
            actorId,
            happenedOn);
    }
}
