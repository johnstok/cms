/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.api.core.ACL;
import ccc.api.core.PagedCollection;
import ccc.api.core.Resource;
import ccc.api.core.ResourceCriteria;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Revision;
import ccc.api.core.Template;
import ccc.api.synchronous.Resources;
import ccc.api.types.Duration;
import ccc.api.types.ResourcePath;
import ccc.api.types.ResourceType;
import ccc.api.types.SortOrder;
import ccc.commands.ApplyWorkingCopyCommand;
import ccc.commands.ChangeTemplateForResourceCommand;
import ccc.commands.ClearWorkingCopyCommand;
import ccc.commands.IncludeInMainMenuCommand;
import ccc.commands.MoveResourceCommand;
import ccc.commands.RenameResourceCommand;
import ccc.commands.UpdateCachingCommand;
import ccc.commands.UpdateResourceAclCommand;
import ccc.commands.UpdateResourceMetadataCommand;
import ccc.commands.UpdateWorkingCopyCommand;
import ccc.commons.streams.ReadToStringAction;
import ccc.domain.ActionEntity;
import ccc.domain.FileEntity;
import ccc.domain.FolderEntity;
import ccc.domain.LogEntry;
import ccc.domain.ResourceEntity;
import ccc.domain.RevisionEntity;
import ccc.domain.TemplateEntity;
import ccc.domain.UserEntity;
import ccc.persistence.ResourceRepository;
import ccc.rest.extensions.ResourcesExt;


/**
 * EJB implementation of the {@link ResourcesExt} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Resources.NAME)
@TransactionAttribute(REQUIRED)
@Remote(ResourcesExt.class)
@Local(Resources.class)
public class ResourcesEJB
    extends
        AbstractEJB
    implements
        Resources,
        ResourcesExt {


    /** {@inheritDoc} */
    @Override
    @TransactionAttribute(REQUIRES_NEW)
    @RolesAllowed(ACTION_EXECUTE)
    public void executeAction(final UUID actionId) {
        final ActionEntity a =
            getRepoFactory().createActionRepository().find(actionId);

        if (new Date().before(a.getExecuteAfter())) {
            return; // Too early.
        }

        executeAction(a);
    }


    private void executeAction(final ActionEntity action) {
        switch (action.getType()) {

            case RESOURCE_UNPUBLISH:
                executeUnpublish(action);
                break;

            case RESOURCE_PUBLISH:
                executePublish(action);
                break;

            case PAGE_UPDATE:
                executeUpdate(action);
                break;

            case RESOURCE_DELETE:
                executeDelete(action);
                break;

            default:
                throw new UnsupportedOperationException(
                    "Unsupported action type: "+action.getType());

        }

        action.complete();
    }


    private void executeDelete(final ActionEntity action) {
        sudoExecute(
            commands().createDeleteResourceCmd(action.getSubject().getId()),
            action.getActor().getId(),
            new Date());
    }


    private void executeUpdate(final ActionEntity action) {
        sudoExecute(
            new ApplyWorkingCopyCommand(
                getRepoFactory(),
                getProducer(),
                action.getSubject().getId(),
                action.getParams().get("COMMENT"),
                Boolean
                    .valueOf(action.getParams().get("MAJOR"))
                    .booleanValue()),
            action.getActor().getId(),
            new Date());
    }


    private void executePublish(final ActionEntity action) {
        sudoExecute(
            commands().publishResource(action.getSubject().getId()),
            action.getActor().getId(),
            new Date());
    }


    private void executeUnpublish(final ActionEntity action) {
        sudoExecute(
            commands().unpublishResourceCommand(action.getSubject().getId()),
            action.getActor().getId(),
            new Date());
    }


    /** {@inheritDoc} */
    @Override
    public void lock(final UUID resourceId) {
        checkPermission(RESOURCE_LOCK);

        execute(
            commands().lockResourceCommand(resourceId));
    }


    /** {@inheritDoc} */
    @Override
    public void move(final UUID resourceId, final UUID newParentId) {
        checkPermission(RESOURCE_MOVE);

        execute(
            new MoveResourceCommand(
                getRepoFactory(),
                getProducer(),
                resourceId,
                newParentId));
    }


    /** {@inheritDoc} */
    @Override
    public Resource publish(final UUID resourceId) {
        checkPermission(RESOURCE_PUBLISH);

        return execute(
            commands().publishResource(resourceId));
    }


    /** {@inheritDoc} */
    @Override
    public void rename(final UUID resourceId, final String name) {
        checkPermission(RESOURCE_RENAME);

        execute(
            new RenameResourceCommand(
                getRepoFactory().createResourceRepository(),
                getRepoFactory().createLogEntryRepo(),
                getProducer(),
                resourceId,
                name));
    }


    /** {@inheritDoc} */
    @Override
    public void unlock(final UUID resourceId) {
        checkPermission(RESOURCE_UNLOCK);
        execute(
            commands().unlockResourceCommand(resourceId));
    }


    /** {@inheritDoc} */
    @Override
    public void unpublish(final UUID resourceId) {
        checkPermission(RESOURCE_UNPUBLISH);

        execute(
            commands().unpublishResourceCommand(resourceId));
    }


    /** {@inheritDoc} */
    @Override
    public void createWorkingCopy(final UUID resourceId, final long index) {
        checkPermission(RESOURCE_UPDATE);

        execute(
            new UpdateWorkingCopyCommand(
                getRepoFactory(), resourceId, index));
    }


    /** {@inheritDoc} */
    @Override
    public void createWorkingCopy(final UUID resourceId,
                                  final Resource pu) {
        checkPermission(RESOURCE_UPDATE);

        createWorkingCopy(resourceId, pu.getRevision());
    }


    /** {@inheritDoc} */
    @Override
    public Resource updateResourceTemplate(final UUID resourceId,
                                        final UUID templateId) {
        checkPermission(RESOURCE_UPDATE);

        return
            execute(
                new ChangeTemplateForResourceCommand(
                    getRepoFactory(), resourceId, templateId));
    }


    /** {@inheritDoc} */
    @Override
    public Resource updateResourceTemplate(final UUID resourceId,
                                           final Resource pu) {
        checkPermission(RESOURCE_UPDATE);

        return updateResourceTemplate(resourceId, pu.getTemplate());
    }


    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final UUID resourceId,
                                  final boolean include) {
        checkPermission(RESOURCE_MM);

        execute(
            new IncludeInMainMenuCommand(
                getRepoFactory(), resourceId, include));
    }


    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final UUID resourceId) {
        checkPermission(RESOURCE_MM);

        includeInMainMenu(resourceId, true);
    }


    /** {@inheritDoc} */
    @Override
    public void excludeFromMainMenu(final UUID resourceId) {
        checkPermission(RESOURCE_MM);

        includeInMainMenu(resourceId, false);
    }


    /** {@inheritDoc} */
    @Override
    public void updateMetadata(final UUID resourceId,
                               final Resource resource) {
        checkPermission(RESOURCE_UPDATE);


        updateMetadata(
            resourceId,
            resource.getTitle(),
            resource.getDescription(),
            resource.getTags(),
            resource.getMetadata());
    }


    /** {@inheritDoc} */
    @Override
    public void updateMetadata(final UUID resourceId,
                               final String title,
                               final String description,
                               final Set<String> tags,
                               final Map<String, String> metadata) {
        checkPermission(RESOURCE_UPDATE);

        execute(
            new UpdateResourceMetadataCommand(
                getRepoFactory(),
                getProducer(),
                resourceId,
                title,
                description,
                tags,
                metadata));
    }


    /** {@inheritDoc} */
    @Override
    public void changeAcl(final UUID resourceId, final ACL acl) {
        checkPermission(RESOURCE_ACL_UPDATE);

        execute(
            new UpdateResourceAclCommand(
                getRepoFactory(),
                getProducer(),
                resourceId,
                acl));
    }


    /** {@inheritDoc} */
    @Override
    public Resource applyWorkingCopy(final UUID resourceId) {
        checkPermission(RESOURCE_UPDATE);

        return
            execute(
                new ApplyWorkingCopyCommand(
                    getRepoFactory(),
                    getProducer(),
                    resourceId,
                    null,
                    false));
    }


    /** {@inheritDoc} */
    @Override
    public void updateCacheDuration(final UUID resourceId,
                                    final Duration duration) {
        checkPermission(RESOURCE_CACHE_UPDATE);

        execute(
            new UpdateCachingCommand(
                getRepoFactory().createResourceRepository(),
                getRepoFactory().createLogEntryRepo(),
                resourceId,
                duration));
    }


    /** {@inheritDoc} */
    @Override
    public void updateCacheDuration(final UUID resourceId,
                                    final Resource pu) {
        checkPermission(RESOURCE_CACHE_UPDATE);

        updateCacheDuration(resourceId, pu.getCacheDuration());
    }


    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final UUID resourceId) {
        checkPermission(RESOURCE_UPDATE);

        execute(new ClearWorkingCopyCommand(getRepoFactory(), resourceId));
    }


    /** {@inheritDoc} */
    @Override
    public void deleteCacheDuration(final UUID id) {
        checkPermission(RESOURCE_CACHE_UPDATE);

        updateCacheDuration(id, (Duration) null);
    }


    /** {@inheritDoc} */
    @Override
    public void delete(final UUID resourceId) {
        checkPermission(RESOURCE_DELETE);

        execute(commands().createDeleteResourceCmd(resourceId));
    }



    /* ====================================================================
     * Queries API.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    public PagedCollection<Revision> history(final UUID resourceId) {
        checkPermission(RESOURCE_READ);

        final List<Revision> revisions = RevisionEntity.mapRevisions(
            getRepoFactory()
            .createResourceRepository()
            .history(resourceId));
        return
            new PagedCollection<Revision>(
                revisions.size(), Revision.class, revisions);
    }


    /** {@inheritDoc} */
    @Override
    public ACL acl(final UUID resourceId) {
        checkPermission(RESOURCE_READ);

        final ACL acl =
            getResources().find(ResourceEntity.class, resourceId).getAcl();
        return acl;
    }


    /** {@inheritDoc} */
    @Override
    public Duration cacheDuration(final UUID resourceId) {
        checkPermission(RESOURCE_READ);

        final ResourceEntity r =
            getResources().find(ResourceEntity.class, resourceId);
        return r.getCacheDuration();
    }


    /** {@inheritDoc} */
    @Override
    public Template computeTemplate(final UUID resourceId) {
        checkPermission(RESOURCE_READ);

        final ResourceEntity r =
            getResources().find(ResourceEntity.class, resourceId);
        final TemplateEntity t = r.computeTemplate(null);
        return (null==t) ? null : t.summarize();
    }


    /** {@inheritDoc} */
    @Override
    public Resource resourceForPath(final String rootPath) {
        checkPermission(RESOURCE_READ);

        final ResourceEntity r =
            getResources().lookup(new ResourcePath(rootPath));

        if (r == null) {
            return null;
        }
        return r.forCurrentRevision();
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<ResourceSummary> resourceForMetadataKey(
        final String key) {
        checkPermission(RESOURCE_READ);
        ResourceCriteria rc = new ResourceCriteria();
        rc.matchMetadatum(key, "%");
        return list(rc, 1, 2000);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary resourceForLegacyId(final String legacyId) {
        checkPermission(RESOURCE_READ);
        ResourceCriteria rc = new ResourceCriteria();
        rc.matchMetadatum("legacyId", legacyId);
        List<ResourceSummary> result = list(rc, 1, 1).getElements();
        if (result == null || result.size() == 0) {
            return null;
        } else {
            return result.get(0);
        }
    }



    /* ====================================================================
     * UNSAFE METHODS.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    public String getAbsolutePath(final UUID resourceId) {
        checkPermission(RESOURCE_READ);
        return
            getResources().find(ResourceEntity.class, resourceId)
                      .getAbsolutePath()
                      .removeTop()
                      .toString();
    }


    /** {@inheritDoc} */
    @Override
    public Resource resourceForPathSecure(final String rootPath) {
        checkPermission(RESOURCE_READ);

        final ResourcePath rp = new ResourcePath(rootPath);
        final ResourceEntity r = getResources().lookup(rp);
        if (r == null) {
            return null;
        }
        checkRead(r);
        return r.forCurrentRevision();
    }


    /** {@inheritDoc} */
    @Override
    public Resource workingCopyForPath(final String rootPath) {
        checkPermission(RESOURCE_READ);

        final ResourcePath rp = new ResourcePath(rootPath);
        final ResourceEntity r =
            getResources().lookup(rp);
        if (r == null) {
            return null;
        }
        checkRead(r);
        return r.forWorkingCopy();
    }


    /** {@inheritDoc} */
    @Override
    public Resource revisionForPath(final String path,
                                            final int version) {
        checkPermission(RESOURCE_READ);

        final ResourcePath rp = new ResourcePath(path);
        final ResourceEntity r =
            getResources().lookup(rp);
        if (r == null) {
            return null;
        }
        checkRead(r);
        return r.forSpecificRevision(version);
    }


    /** {@inheritDoc} */
    @Override
    public String fileContentsFromPath(final String absolutePath,
                                       final String charset) {
        checkPermission(RESOURCE_READ);

        final ResourcePath rp = new ResourcePath(absolutePath);

        final ResourceEntity r = getResources().lookup(rp);
        if (r != null && r instanceof FileEntity) {
            final StringBuilder sb = new StringBuilder();
            final FileEntity f = (FileEntity) r;
            if (f.isText()) {
                getRepoFactory().createDataRepository().retrieve(
                    f.getData(),
                    new ReadToStringAction(sb, charset)
                );
            }
            return sb.toString();
        }

        return null;
    }


    /** {@inheritDoc} */
    @Override
    public Map<String, String> metadata(final UUID resourceId) {
        checkPermission(RESOURCE_READ);

        final ResourceEntity r =
            getResources().find(ResourceEntity.class, resourceId);
        return r.getMetadata();
    }


    /** {@inheritDoc} */
    @Override
    public Resource retrieve(final UUID resourceId) {
        checkPermission(RESOURCE_READ);

        final ResourceEntity r =
            getResources().find(ResourceEntity.class, resourceId);
        if (r == null) {
            return null;
        }
        checkRead(r);

        return r.forCurrentRevision();
    }

    /** {@inheritDoc} */
    @Override
    public void createLogEntry(final UUID resourceId,
                               final String action,
                               final String detail) {
        checkPermission(LOG_ENTRY_CREATE);

        final LogEntry le = new LogEntry(currentUser(),
                                        action,
                                        new Date(),
                                        resourceId,
                                        detail);
        getRepoFactory().createLogEntryRepo().record(le);
    }

    /** {@inheritDoc} */
    @Override
    public PagedCollection<ResourceSummary> list(final UUID parent,
                                                 final String tag,
                                                 final Long before,
                                                 final Long after,
                                                 final String mainMenu,
                                                 final String type,
                                                 final String locked,
                                                 final String published,
                                                 final String sort,
                                                 final SortOrder order,
                                                 final int pageNo,
                                                 final int pageSize) {
        checkPermission(RESOURCE_READ);

        final ResourceCriteria criteria = new ResourceCriteria();
        criteria.setParent(parent);
        criteria.setTag(tag);
        criteria.setChangedBefore(
            (null==before)?null:new Date(before.longValue()));
        criteria.setChangedAfter(
            (null==after)?null:new Date(after.longValue()));
        criteria.setMainmenu(
            (null==mainMenu) ? null : Boolean.valueOf(mainMenu));
        final ResourceType t =
            (null==type)
                ? null
                : ResourceType.valueOf(type.toUpperCase(Locale.US));
        criteria.setType(t);
        if (null != published) {
            criteria.setPublished(Boolean.valueOf(published));
        }
        criteria.setLocked(
            (null==locked) ? null : Boolean.valueOf(locked));
        criteria.setSortField(sort);
        criteria.setSortOrder(order);

        return list(criteria, pageNo, pageSize);
    }


    @Deprecated
    private ResourceRepository getResources() {
        return getRepoFactory().createResourceRepository();
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<ResourceSummary> list(
                                                final ResourceCriteria criteria,
                                                final int pageNo,
                                                final int pageSize) {
        checkPermission(RESOURCE_READ);

        final UserEntity u = currentUser();

        final UUID parent = criteria.getParent();
        FolderEntity f = null;
        if (parent != null) {
            f =
                getRepoFactory()
                .createResourceRepository()
                .find(FolderEntity.class, parent);
            checkRead(f);
            criteria.setParent(parent);
        }

        final List<ResourceSummary> list = ResourceEntity.mapResources(
            filterAccessibleTo(u,
                getResources().list(criteria,
                                    f,
                                    criteria.getSortField(),
                                    criteria.getSortOrder(),
                                    pageNo,
                                    pageSize)));

        final long count = getResources().totalCount(criteria, f);

        return
            new PagedCollection<ResourceSummary>(
                count, ResourceSummary.class, list);
    }
}
