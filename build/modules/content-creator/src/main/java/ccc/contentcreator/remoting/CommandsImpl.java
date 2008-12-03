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
import ccc.services.api.Commands;
import ccc.services.api.PageDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserDelta;

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

    /**
     * @param parentId
     * @param name
     * @param targetId
     * @see ccc.services.api.Commands#createAlias(java.lang.String, java.lang.String, java.lang.String)
     */
    public void createAlias(final String parentId, final String name, final String targetId) {

        _delegate.createAlias(parentId, name, targetId);
    }

    /**
     * @param parentId
     * @param name
     * @see ccc.services.api.Commands#createFolder(java.lang.String, java.lang.String)
     */
    public ResourceSummary createFolder(final String parentId, final String name) {

        return _delegate.createFolder(parentId, name);
    }

    /**
     * @param parentId
     * @param delta
     * @param templateId
     * @see ccc.services.api.Commands#createPage(java.lang.String, ccc.services.api.PageDelta, java.lang.String)
     */
    public void createPage(final String parentId, final PageDelta delta, final String templateId) {

        _delegate.createPage(parentId, delta, templateId);
    }

    /** {@inheritDoc} */
    public ResourceSummary createTemplate(final String parentId,
                                          final TemplateDelta delta) {
        return _delegate.createTemplate(parentId, delta);
    }

    /**
     * @param delta
     * @see ccc.services.api.Commands#createUser(ccc.services.api.UserDelta)
     */
    public void createUser(final UserDelta delta) {

        _delegate.createUser(delta);
    }

    /**
     * @param resourceId
     * @return
     * @see ccc.services.api.Commands#lock(java.lang.String)
     */
    public ResourceSummary lock(final String resourceId) {

        return _delegate.lock(resourceId);
    }

    /** {@inheritDoc} */
    public void logout() {
        getThreadLocalRequest().getSession().invalidate();
    }

    /**
     * @param resourceId
     * @param version
     * @param newParentId
     * @see ccc.services.api.Commands#move(java.lang.String, long, java.lang.String)
     */
    public void move(final String resourceId, final long version, final String newParentId) {

        _delegate.move(resourceId, version, newParentId);
    }

    /**
     * @param resourceId
     * @return
     * @see ccc.services.api.Commands#publish(java.lang.String)
     */
    public ResourceSummary publish(final String resourceId) {

        return _delegate.publish(resourceId);
    }

    /**
     * @param resourceId
     * @param version
     * @param name
     * @see ccc.services.api.Commands#rename(java.lang.String, long, java.lang.String)
     */
    public void rename(final String resourceId, final long version, final String name) {

        _delegate.rename(resourceId, version, name);
    }

    /**
     * @param resourceId
     * @return
     * @see ccc.services.api.Commands#unlock(java.lang.String)
     */
    public ResourceSummary unlock(final String resourceId) {

        return _delegate.unlock(resourceId);
    }


    /** {@inheritDoc} */
    @Override public ResourceSummary unpublish(final String resourceId) {
        return _delegate.unpublish(resourceId);
    }

    /**
     * @param aliasId
     * @param version
     * @param targetId
     * @see ccc.services.api.Commands#updateAlias(java.lang.String, long, java.lang.String)
     */
    public void updateAlias(final String aliasId, final long version, final String targetId) {

        _delegate.updateAlias(aliasId, version, targetId);
    }

    /**
     * @param pageId
     * @param version
     * @param delta
     * @see ccc.services.api.Commands#updatePage(java.lang.String, long, ccc.services.api.PageDelta)
     */
    public void updatePage(final String pageId, final long version, final PageDelta delta) {

        _delegate.updatePage(pageId, version, delta);
    }

    /**
     * @param resourceId
     * @param version
     * @param templateId
     * @see ccc.services.api.Commands#updateResourceTemplate(java.lang.String, long, java.lang.String)
     */
    public void updateResourceTemplate(final String resourceId,
                                       final long version,
                                       final String templateId) {

        _delegate.updateResourceTemplate(resourceId, version, templateId);
    }

    /**
     * @param resourceId
     * @param version
     * @param tags
     * @see ccc.services.api.Commands#updateTags(java.lang.String, long, java.lang.String)
     */
    public void updateTags(final String resourceId, final long version, final String tags) {

        _delegate.updateTags(resourceId, version, tags);
    }

    /** {@inheritDoc} */
    public ResourceSummary updateTemplate(final TemplateDelta delta) {
        return _delegate.updateTemplate(delta);
    }

    /**
     * @param userId
     * @param version
     * @param delta
     * @see ccc.services.api.Commands#updateUser(java.lang.String, long, ccc.services.api.UserDelta)
     */
    public void updateUser(final String userId, final long version, final UserDelta delta) {

        _delegate.updateUser(userId, version, delta);
    }
}
