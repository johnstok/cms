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

import java.util.List;

import ccc.commons.JNDI;
import ccc.contentcreator.api.CommandService;
import ccc.services.api.AliasDelta;
import ccc.services.api.CCCRemoteException;
import ccc.services.api.Commands;
import ccc.services.api.PageDelta;
import ccc.services.api.ParagraphDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.ServiceNames;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CommandsImpl
    extends RemoteServiceServlet
    implements CommandService {

    private final Commands _delegate =
        new JNDI().get(ServiceNames.PUBLIC_COMMANDS);

    /** {@inheritDoc} */
    @Override
    public void createAlias(final String parentId,
                            final String name,
                            final String targetId) {
        _delegate.createAlias(parentId, name, targetId);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final String parentId,
                                        final String name) {
        return _delegate.createFolder(parentId, name);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createPage(final String parentId,
                           final PageDelta delta,
                           final String templateId) {
        return _delegate.createPage(parentId, delta, templateId);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createTemplate(final String parentId,
                                          final TemplateDelta delta) {
        return _delegate.createTemplate(parentId, delta);
    }

    /** {@inheritDoc} */
    @Override
    public UserSummary createUser(final UserDelta delta) {
        return _delegate.createUser(delta);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary lock(final String resourceId) {
        return _delegate.lock(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public void move(final String resourceId,
                     final String newParentId) {
        _delegate.move(resourceId, newParentId);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary publish(final String resourceId) {
        return _delegate.publish(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary publish(final String resourceId,
                                   final String userId) {
        return _delegate.publish(resourceId, userId);
    }

    /** {@inheritDoc} */
    @Override
    public void rename(final String resourceId,
                       final String name) {
        _delegate.rename(resourceId, name);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary unlock(final String resourceId) {
        return _delegate.unlock(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary unpublish(final String resourceId) {
        return _delegate.unpublish(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public void updateAlias(final AliasDelta delta) {
        _delegate.updateAlias(delta);
    }

    /** {@inheritDoc} */
    @Override
    public void updatePage(final PageDelta delta) {
        try {
            _delegate.updatePage(delta);
        } catch (final RuntimeException e) {
            final CCCRemoteException re = new CCCRemoteException();
            re._errorCode = 0;
            throw re;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final String resourceId,
                                       final String templateId) {
        _delegate.updateResourceTemplate(resourceId, templateId);
    }

    /** {@inheritDoc} */
    @Override
    public void updateTags(final String resourceId,
                           final String tags) {
        _delegate.updateTags(resourceId, tags);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary updateTemplate(final TemplateDelta delta) {
        return _delegate.updateTemplate(delta);
    }

    /** {@inheritDoc} */
    @Override
    public UserSummary updateUser(final UserDelta delta) {
        return _delegate.updateUser(delta);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createRoot(final String name) {
        return _delegate.createRoot(name);
    }

    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final String resourceId,
                                  final boolean include) {
        _delegate.includeInMainMenu(resourceId, include);
    }

    /** {@inheritDoc} */
    @Override
    public List<String> validateFields(final List<ParagraphDelta> delta,
                                 final String definition) {
        return _delegate.validateFields(delta, definition);
    }

    /** {@inheritDoc} */
    @Override
    public void updateStyleSheet(final String _id, final String styleSheet) {
        _delegate.updateStyleSheet(_id, styleSheet);
    }
}
