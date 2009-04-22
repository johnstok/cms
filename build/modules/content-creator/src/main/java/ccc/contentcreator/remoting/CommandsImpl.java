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
package ccc.contentcreator.remoting;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ccc.contentcreator.api.CommandService;
import ccc.services.api.AliasDelta;
import ccc.services.api.PageDelta;
import ccc.services.api.ParagraphDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;


/**
 * TODO: Add Description for this type.
 * TODO: Inject delegate implementation.
 *
 * @author Civic Computing Ltd.
 */
public class CommandsImpl
    extends CCCRemoteServiceServlet
    implements CommandService {

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createAlias(final String parentId,
                            final String name,
                            final String targetId) {
        return _services.lookupCommands().createAlias(parentId, name, targetId);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final String parentId,
                                        final String name) {
        return _services.lookupCommands().createFolder(parentId, name);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final String parentId,
                                        final String name,
                                        final String title) {
        return _services.lookupCommands().createFolder(parentId, name, title);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createPage(final String parentId,
                           final PageDelta delta,
                           final String templateId) {
        return _services.lookupCommands().createPage(
            parentId, delta, templateId);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createTemplate(final String parentId,
                                          final TemplateDelta delta) {
        return _services.lookupCommands().createTemplate(parentId, delta);
    }

    /** {@inheritDoc} */
    @Override
    public UserSummary createUser(final UserDelta delta) {
        return _services.lookupCommands().createUser(delta);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary lock(final String resourceId) {
        return _services.lookupCommands().lock(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public void move(final String resourceId,
                     final String newParentId) {
        _services.lookupCommands().move(resourceId, newParentId);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary publish(final String resourceId) {
        return _services.lookupCommands().publish(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary publish(final String resourceId,
                                   final String userId,
                                   final Date publishOn) {
        return _services.lookupCommands().publish(
            resourceId, userId, publishOn);
    }

    /** {@inheritDoc} */
    @Override
    public void rename(final String resourceId,
                       final String name) {
        _services.lookupCommands().rename(resourceId, name);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary unlock(final String resourceId) {
        return _services.lookupCommands().unlock(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary unpublish(final String resourceId) {
        return _services.lookupCommands().unpublish(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public void updateAlias(final AliasDelta delta) {
        _services.lookupCommands().updateAlias(delta);
    }

    /** {@inheritDoc} */
    @Override
    public void updatePage(final PageDelta delta,
                           final String comment,
                           final boolean isMajorEdit) {
        _services.lookupCommands().updatePage(delta, comment, isMajorEdit);
    }

    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final String resourceId,
                                       final String templateId) {
        _services.lookupCommands().updateResourceTemplate(
            resourceId, templateId);
    }

    /** {@inheritDoc} */
    @Override
    public void updateTags(final String resourceId,
                           final String tags) {
        _services.lookupCommands().updateTags(resourceId, tags);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary updateTemplate(final TemplateDelta delta) {
        return _services.lookupCommands().updateTemplate(delta);
    }

    /** {@inheritDoc} */
    @Override
    public UserSummary updateUser(final UserDelta delta) {
        return _services.lookupCommands().updateUser(delta);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createRoot(final String name) {
        return _services.lookupCommands().createRoot(name);
    }

    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final String resourceId,
                                  final boolean include) {
        _services.lookupCommands().includeInMainMenu(resourceId, include);
    }

    /** {@inheritDoc} */
    @Override
    public List<String> validateFields(final List<ParagraphDelta> delta,
                                 final String definition) {
        return _services.lookupCommands().validateFields(delta, definition);
    }

    /** {@inheritDoc} */
    @Override
    public void updateMetadata(final String resourceId,
                                 final Map<String, String> metadata) {
        _services.lookupCommands().updateMetadata(resourceId, metadata);
    }

    /** {@inheritDoc} */
    @Override
    public void updateFolderSortOrder(final String folderId,
                                      final String sortOrder) {
        _services.lookupCommands().updateFolderSortOrder(folderId, sortOrder);
    }

    /** {@inheritDoc} */
    @Override
    public void updateWorkingCopy(final PageDelta delta) {
        _services.lookupCommands().updateWorkingCopy(delta);
    }


    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final String pageId) {
        _services.lookupCommands().clearWorkingCopy(pageId);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createSearch(final String parentId,
                                        final String title) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void createWorkingCopy(final String resourceId, final long index) {
        _services.lookupCommands().createWorkingCopy(resourceId, index);
    }

    /** {@inheritDoc} */
    @Override
    public void cancelAction(final String actionId) {
        _services.lookupCommands().cancelAction(actionId);
    }

    /** {@inheritDoc} */
    @Override
    public void createAction(final String resourceId,
                             final String action,
                             final Date executeAfter,
                             final String parameters) {
        _services.lookupCommands().createAction(
            resourceId, action, executeAfter, parameters);
    }

    /** {@inheritDoc} */
    @Override
    public void reorder(final String folderId, final List<String> order) {
        _services.lookupCommands().reorder(folderId, order);
    }

    /** {@inheritDoc} */
    @Override
    public void changeRoles(final String resourceId,
                            final Collection<String> roles) {
        _services.lookupCommands().changeRoles(resourceId, roles);
    }

    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopyToFile(final String fileId) {
        _services.lookupCommands().applyWorkingCopyToFile(fileId);
    }
}
