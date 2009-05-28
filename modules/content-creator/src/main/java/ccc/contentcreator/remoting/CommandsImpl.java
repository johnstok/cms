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
import java.util.Set;

import javax.ejb.EJB;

import ccc.api.AliasDelta;
import ccc.api.CommandFailedException;
import ccc.api.CommandType;
import ccc.api.Commands;
import ccc.api.Duration;
import ccc.api.ID;
import ccc.api.LocalCommands;
import ccc.api.PageDelta;
import ccc.api.Paragraph;
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;
import ccc.api.UserDelta;
import ccc.api.UserSummary;
import ccc.contentcreator.api.CommandService;


/**
 * GWT implementation of the {@link Commands} interface.
 *
 * @author Civic Computing Ltd.
 */
public class CommandsImpl
    extends CCCRemoteServiceServlet
    implements CommandService {

    @EJB(name=Commands.NAME) private LocalCommands _commands;

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createAlias(final ID parentId,
                            final String name,
                            final ID targetId) throws CommandFailedException {
        return _commands.createAlias(parentId, name, targetId);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final ID parentId,
                                        final String name)
    throws CommandFailedException {
        return _commands.createFolder(parentId, name);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final ID parentId,
                                        final String name,
                                        final String title,
                                        final boolean publish)
    throws CommandFailedException {
        return _commands.createFolder(parentId, name, title, publish);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createPage(final ID parentId,
                                      final PageDelta delta,
                                      final String name,
                                      final boolean publish,
                                      final ID templateId)
    throws CommandFailedException {
        return _commands.createPage(parentId, delta, name, publish, templateId);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createTemplate(final ID parentId,
                                          final TemplateDelta delta,
                                          final String name)
    throws CommandFailedException {
        return _commands.createTemplate(parentId, delta, name);
    }

    /** {@inheritDoc} */
    @Override
    public UserSummary createUser(final UserDelta delta,
                                  final String password)
    throws CommandFailedException {
        return _commands.createUser(delta, password);
    }

    /** {@inheritDoc} */
    @Override
    public void lock(final ID resourceId) throws CommandFailedException {
        _commands.lock(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public void move(final ID resourceId,
                     final ID newParentId) throws CommandFailedException {
        _commands.move(resourceId, newParentId);
    }

    /** {@inheritDoc} */
    @Override
    public void publish(final ID resourceId) throws CommandFailedException {
        _commands.publish(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public void publish(final ID resourceId,
                                   final ID userId,
                                   final Date publishOn)
    throws CommandFailedException {
        _commands.publish(resourceId, userId, publishOn);
    }

    /** {@inheritDoc} */
    @Override
    public void rename(final ID resourceId,
                       final String name) throws CommandFailedException {
        _commands.rename(resourceId, name);
    }

    /** {@inheritDoc} */
    @Override
    public void unlock(final ID resourceId) throws CommandFailedException {
        _commands.unlock(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void unpublish(final ID resourceId) throws CommandFailedException {
        _commands.unpublish(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public void updateAlias(final ID aliasId, final AliasDelta delta)
    throws CommandFailedException {
        _commands.updateAlias(aliasId, delta);
    }

    /** {@inheritDoc} */
    @Override
    public void updatePage(final ID pageId,
                           final PageDelta delta,
                           final String comment,
                           final boolean isMajorEdit)
    throws CommandFailedException {
        _commands.updatePage(
            pageId, delta, comment, isMajorEdit);
    }

    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final ID resourceId,
                                       final ID templateId)
    throws CommandFailedException {
        _commands.updateResourceTemplate(
            resourceId, templateId);
    }

    /** {@inheritDoc} */
    @Override
    public void updateTags(final ID resourceId,
                           final String tags) throws CommandFailedException {
        _commands.updateTags(resourceId, tags);
    }

    /** {@inheritDoc} */
    @Override
    public void updateTemplate(final ID templateId,
                                          final TemplateDelta delta)
    throws CommandFailedException {
        _commands.updateTemplate(templateId, delta);
    }

    /** {@inheritDoc} */
    @Override
    public void updateUser(final ID userId, final UserDelta delta)
    throws CommandFailedException {
        _commands.updateUser(userId, delta);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createRoot(final String name)
    throws CommandFailedException {
        return _commands.createRoot(name);
    }

    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final ID resourceId,
                                  final boolean include)
    throws CommandFailedException {
        _commands.includeInMainMenu(resourceId, include);
    }

    /** {@inheritDoc} */
    @Override
    public List<String> validateFields(final Set<Paragraph> delta,
                                       final String definition) {
        return _commands.validateFields(delta, definition);
    }

    /** {@inheritDoc} */
    @Override
    public void updateMetadata(final ID resourceId,
                                 final Map<String, String> metadata)
    throws CommandFailedException {
        _commands.updateMetadata(resourceId, metadata);
    }

    /** {@inheritDoc} */
    @Override
    public void updateFolderSortOrder(final ID folderId,
                                      final String sortOrder)
    throws CommandFailedException {
        _commands.updateFolderSortOrder(folderId, sortOrder);
    }

    /** {@inheritDoc} */
    @Override
    public void updateWorkingCopy(final ID pageId, final PageDelta delta)
    throws CommandFailedException {
        _commands.updateWorkingCopy(pageId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final ID pageId)
                                                 throws CommandFailedException {
        _commands.clearWorkingCopy(pageId);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createSearch(final ID parentId,
                                        final String title) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void createWorkingCopy(final ID resourceId, final long index)
    throws CommandFailedException {
        _commands.createWorkingCopy(resourceId, index);
    }

    /** {@inheritDoc} */
    @Override
    public void cancelAction(final ID actionId) throws CommandFailedException {
        _commands.cancelAction(actionId);
    }

    /** {@inheritDoc} */
    @Override
    public void createAction(final ID resourceId,
                             final CommandType action,
                             final Date executeAfter,
                             final Map<String, String> parameters,
                             final String comment,
                             final boolean isMajorEdit)
    throws CommandFailedException {
        _commands.createAction(
            resourceId, action, executeAfter, parameters, comment, isMajorEdit);
    }

    /** {@inheritDoc} */
    @Override
    public void reorder(final ID folderId, final List<String> order)
    throws CommandFailedException {
        _commands.reorder(folderId, order);
    }

    /** {@inheritDoc} */
    @Override
    public void changeRoles(final ID resourceId,
                            final Collection<String> roles)
    throws CommandFailedException {
        _commands.changeRoles(resourceId, roles);
    }

    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopy(final ID fileId)
    throws CommandFailedException {
        _commands.applyWorkingCopy(fileId);
    }

    /** {@inheritDoc} */
    @Override
    public void updateCacheDuration(final ID resourceId,
                                    final Duration duration)
    throws CommandFailedException {
        _commands.updateCacheDuration(resourceId, duration);
    }

    /** {@inheritDoc} */
    @Override
    public void updateUserPassword(final ID userId, final String password)
    throws CommandFailedException {
        _commands.updateUserPassword(userId, password);
    }

    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopy(final ID resourceId,
                                 final ID userId,
                                 final Date happenedOn,
                                 final boolean isMajorEdit,
                                 final String comment) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void unpublish(final ID resourceId,
                          final ID userId,
                          final Date publishDate) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


}
