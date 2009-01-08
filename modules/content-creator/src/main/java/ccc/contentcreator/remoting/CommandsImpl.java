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

import ccc.commons.JNDI;
import ccc.contentcreator.api.CommandService;
import ccc.services.api.AliasDelta;
import ccc.services.api.CCCRemoteException;
import ccc.services.api.Commands;
import ccc.services.api.PageDelta;
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
    public void createAlias(final String parentId,
                            final String name,
                            final String targetId) {
        _delegate.createAlias(parentId, name, targetId);
    }

    /** {@inheritDoc} */
    public ResourceSummary createFolder(final String parentId,
                                        final String name) {
        return _delegate.createFolder(parentId, name);
    }

    /** {@inheritDoc} */
    public ResourceSummary createPage(final String parentId,
                           final PageDelta delta,
                           final String templateId) {
        return _delegate.createPage(parentId, delta, templateId);
    }

    /** {@inheritDoc} */
    public ResourceSummary createTemplate(final String parentId,
                                          final TemplateDelta delta) {
        return _delegate.createTemplate(parentId, delta);
    }

    /** {@inheritDoc}
     * @return */
    public UserSummary createUser(final UserDelta delta) {
        return _delegate.createUser(delta);
    }

    /** {@inheritDoc} */
    public ResourceSummary lock(final String resourceId) {
        return _delegate.lock(resourceId);
    }

    /** {@inheritDoc} */
    public void logout() {
        getThreadLocalRequest().getSession().invalidate();
    }

    /** {@inheritDoc} */
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
    public void rename(final String resourceId,
                       final String name) {
        _delegate.rename(resourceId, name);
    }

    /** {@inheritDoc} */
    public ResourceSummary unlock(final String resourceId) {
        return _delegate.unlock(resourceId);
    }


    /** {@inheritDoc} */
    @Override public ResourceSummary unpublish(final String resourceId) {
        return _delegate.unpublish(resourceId);
    }

    /** {@inheritDoc} */
    public void updateAlias(final AliasDelta delta) {
        _delegate.updateAlias(delta);
    }

    /** {@inheritDoc} */
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
    public void updateResourceTemplate(final String resourceId,
                                       final String templateId) {
        _delegate.updateResourceTemplate(resourceId, templateId);
    }

    /** {@inheritDoc} */
    public void updateTags(final String resourceId,
                           final String tags) {
        _delegate.updateTags(resourceId, tags);
    }

    /** {@inheritDoc} */
    public ResourceSummary updateTemplate(final TemplateDelta delta) {
        return _delegate.updateTemplate(delta);
    }

    /** {@inheritDoc} */
    public UserSummary updateUser(final UserDelta delta) {
        return _delegate.updateUser(delta);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createRoot(final String name) {
        return _delegate.createRoot(name);
    }
}
