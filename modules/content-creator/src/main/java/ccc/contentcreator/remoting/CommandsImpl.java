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
import ccc.services.api.Commands;
import ccc.services.api.PageDelta;
import ccc.services.api.ResourceSummary;
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
        new JNDI().get("application-ear-7.0.0-SNAPSHOT/PublicCommands/remote"); // TODO: Externalise string

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
    public void createPage(final String parentId,
                           final PageDelta delta,
                           final String templateId) {
        _delegate.createPage(parentId, delta, templateId);
    }

    /** {@inheritDoc} */
    public ResourceSummary createTemplate(final String parentId,
                                          final TemplateDelta delta) {
        return _delegate.createTemplate(parentId, delta);
    }

    /** {@inheritDoc} */
    public void createUser(final UserDelta delta) {
        _delegate.createUser(delta);
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
                     final long version,
                     final String newParentId) {
        _delegate.move(resourceId, version, newParentId);
    }

    /** {@inheritDoc} */
    public ResourceSummary publish(final String resourceId) {
        return _delegate.publish(resourceId);
    }

    /** {@inheritDoc} */
    public void rename(final String resourceId,
                       final long version,
                       final String name) {
        _delegate.rename(resourceId, version, name);
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
        _delegate.updatePage(delta);
    }

    /** {@inheritDoc} */
    public void updateResourceTemplate(final String resourceId,
                                       final long version,
                                       final String templateId) {
        _delegate.updateResourceTemplate(resourceId, version, templateId);
    }

    /** {@inheritDoc} */
    public void updateTags(final String resourceId,
                           final long version,
                           final String tags) {
        _delegate.updateTags(resourceId, version, tags);
    }

    /** {@inheritDoc} */
    public ResourceSummary updateTemplate(final TemplateDelta delta) {
        return _delegate.updateTemplate(delta);
    }

    /** {@inheritDoc} */
    public UserSummary updateUser(final UserDelta delta) {
        return _delegate.updateUser(delta);
    }
}
