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
import ccc.services.api.ActionType;
import ccc.services.api.AliasDelta;
import ccc.services.api.CCCRemoteException;
import ccc.services.api.Commands;
import ccc.services.api.Duration;
import ccc.services.api.ID;
import ccc.services.api.PageDelta;
import ccc.services.api.ParagraphDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;


/**
 * GWT implementation of the {@link Commands} interface.
 * TODO: Inject delegate implementation.
 *
 * @author Civic Computing Ltd.
 */
public class CommandsImpl
    extends CCCRemoteServiceServlet
    implements CommandService {

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createAlias(final ID parentId,
                            final String name,
                            final ID targetId) throws CCCRemoteException {
        return _services.lookupCommands().createAlias(parentId, name, targetId);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final ID parentId,
                                        final String name) throws CCCRemoteException {
        return _services.lookupCommands().createFolder(parentId, name);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final ID parentId,
                                        final String name,
                                        final String title) throws CCCRemoteException {
        return _services.lookupCommands().createFolder(parentId, name, title);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createPage(final ID parentId,
                                      final PageDelta delta,
                                      final String name,
                                      final boolean publish,
                                      final ID templateId) throws CCCRemoteException {
        return _services.lookupCommands().createPage(
            parentId, delta, name, publish, templateId);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createTemplate(final ID parentId,
                                          final TemplateDelta delta,
                                          final String name) throws CCCRemoteException {
        return _services.lookupCommands().createTemplate(parentId, delta, name);
    }

    /** {@inheritDoc} */
    @Override
    public UserSummary createUser(final UserDelta delta,
                                  final String password) throws CCCRemoteException {
        return _services.lookupCommands().createUser(delta, password);
    }

    /** {@inheritDoc} */
    @Override
    public void lock(final ID resourceId) throws CCCRemoteException {
        _services.lookupCommands().lock(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public void move(final ID resourceId,
                     final ID newParentId) throws CCCRemoteException {
        _services.lookupCommands().move(resourceId, newParentId);
    }

    /** {@inheritDoc} */
    @Override
    public void publish(final ID resourceId) throws CCCRemoteException {
        _services.lookupCommands().publish(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public void publish(final ID resourceId,
                                   final ID userId,
                                   final Date publishOn) throws CCCRemoteException {
        _services.lookupCommands().publish(resourceId, userId, publishOn);
    }

    /** {@inheritDoc} */
    @Override
    public void rename(final ID resourceId,
                       final String name) throws CCCRemoteException {
        _services.lookupCommands().rename(resourceId, name);
    }

    /** {@inheritDoc} */
    @Override
    public void unlock(final ID resourceId) throws CCCRemoteException {
        _services.lookupCommands().unlock(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void unpublish(final ID resourceId) throws CCCRemoteException {
        _services.lookupCommands().unpublish(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public void updateAlias(final ID aliasId, final AliasDelta delta) throws CCCRemoteException {
        _services.lookupCommands().updateAlias(aliasId, delta);
    }

    /** {@inheritDoc} */
    @Override
    public void updatePage(final ID pageId,
                           final PageDelta delta,
                           final String comment,
                           final boolean isMajorEdit) throws CCCRemoteException {
        _services.lookupCommands().updatePage(
            pageId, delta, comment, isMajorEdit);
    }

    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final ID resourceId,
                                       final ID templateId) throws CCCRemoteException {
        _services.lookupCommands().updateResourceTemplate(
            resourceId, templateId);
    }

    /** {@inheritDoc} */
    @Override
    public void updateTags(final ID resourceId,
                           final String tags) throws CCCRemoteException {
        _services.lookupCommands().updateTags(resourceId, tags);
    }

    /** {@inheritDoc} */
    @Override
    public void updateTemplate(final ID templateId,
                                          final TemplateDelta delta) throws CCCRemoteException {
        _services.lookupCommands().updateTemplate(templateId, delta);
    }

    /** {@inheritDoc} */
    @Override
    public void updateUser(final ID userId, final UserDelta delta) throws CCCRemoteException {
        _services.lookupCommands().updateUser(userId, delta);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createRoot(final String name) throws CCCRemoteException {
        return _services.lookupCommands().createRoot(name);
    }

    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final ID resourceId,
                                  final boolean include) throws CCCRemoteException {
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
    public void updateMetadata(final ID resourceId,
                                 final Map<String, String> metadata) throws CCCRemoteException {
        _services.lookupCommands().updateMetadata(resourceId, metadata);
    }

    /** {@inheritDoc} */
    @Override
    public void updateFolderSortOrder(final ID folderId,
                                      final String sortOrder) throws CCCRemoteException {
        _services.lookupCommands().updateFolderSortOrder(folderId, sortOrder);
    }

    /** {@inheritDoc} */
    @Override
    public void updateWorkingCopy(final ID pageId, final PageDelta delta) throws CCCRemoteException {
        _services.lookupCommands().updateWorkingCopy(pageId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final ID pageId) throws CCCRemoteException {
        _services.lookupCommands().clearWorkingCopy(pageId);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createSearch(final ID parentId,
                                        final String title) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void createWorkingCopy(final ID resourceId, final long index) throws CCCRemoteException {
        _services.lookupCommands().createWorkingCopy(resourceId, index);
    }

    /** {@inheritDoc} */
    @Override
    public void cancelAction(final ID actionId) throws CCCRemoteException {
        _services.lookupCommands().cancelAction(actionId);
    }

    /** {@inheritDoc} */
    @Override
    public void createAction(final ID resourceId,
                             final ActionType action,
                             final Date executeAfter,
                             final String parameters) throws CCCRemoteException {
        _services.lookupCommands().createAction(
            resourceId, action, executeAfter, parameters);
    }

    /** {@inheritDoc} */
    @Override
    public void reorder(final ID folderId, final List<String> order) throws CCCRemoteException {
        _services.lookupCommands().reorder(folderId, order);
    }

    /** {@inheritDoc} */
    @Override
    public void changeRoles(final ID resourceId,
                            final Collection<String> roles) throws CCCRemoteException {
        _services.lookupCommands().changeRoles(resourceId, roles);
    }

    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopyToFile(final ID fileId) throws CCCRemoteException {
        _services.lookupCommands().applyWorkingCopyToFile(fileId);
    }

    /** {@inheritDoc} */
    @Override
    public void updateCacheDuration(final ID resourceId,
                                    final Duration duration) throws CCCRemoteException {
        _services.lookupCommands().updateCacheDuration(resourceId, duration);
    }

    /** {@inheritDoc} */
    @Override
    public void updateUserPassword(final ID userId, final String password) throws CCCRemoteException {
        _services.lookupCommands().updateUserPassword(userId, password);
    }


}
