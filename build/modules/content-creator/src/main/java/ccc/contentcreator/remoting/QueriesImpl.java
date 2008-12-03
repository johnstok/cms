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

import ccc.commons.JNDI;
import ccc.contentcreator.api.QueriesService;
import ccc.services.api.AliasDelta;
import ccc.services.api.LogEntrySummary;
import ccc.services.api.Queries;
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
public class QueriesImpl
    extends RemoteServiceServlet
    implements QueriesService {

    private final Queries _delegate =
        new JNDI().get("application-ear-7.0.0-SNAPSHOT/PublicQueries/remote"); // TODO: Externalise string

    /** {@inheritDoc} */
    public String getAbsolutePath(final String resourceId) {
        return _delegate.getAbsolutePath(resourceId);
    }

    /** {@inheritDoc} */
    public Collection<ResourceSummary> getChildren(final String folderId) {
        return _delegate.getChildren(folderId);
    }

    /** {@inheritDoc} */
    public Collection<ResourceSummary> getFolderChildren(final String folderId) {
        return _delegate.getFolderChildren(folderId);
    }

    /** {@inheritDoc} */
    public TemplateDelta getTemplateForResource(final String resourceId) {
        return _delegate.getTemplateForResource(resourceId);
    }

    /** {@inheritDoc} */
    public Collection<LogEntrySummary> history(final String resourceId) {
        return _delegate.history(resourceId);
    }

    /** {@inheritDoc} */
    public Collection<UserSummary> listUsers() {
        return _delegate.listUsers();
    }

    /** {@inheritDoc} */
    public Collection<UserSummary> listUsersWithEmail(final String email) {
        return _delegate.listUsersWithEmail(email);
    }

    /** {@inheritDoc} */
    public Collection<UserSummary> listUsersWithRole(final String role) {
        return _delegate.listUsersWithRole(role);
    }

    /** {@inheritDoc} */
    public Collection<UserSummary> listUsersWithUsername(
                                                    final String username) {
        return _delegate.listUsersWithUsername(username);
    }

    /** {@inheritDoc} */
    public Collection<ResourceSummary> locked() {
        return _delegate.locked();
    }

    /** {@inheritDoc} */
    public Collection<ResourceSummary> lockedByCurrentUser() {
        return _delegate.lockedByCurrentUser();
    }

    /** {@inheritDoc} */
    public UserSummary loggedInUser() {
        return _delegate.loggedInUser();
    }

    /** {@inheritDoc} */
    public boolean nameExistsInFolder(final String folderId,
                                      final String name) {
        return _delegate.nameExistsInFolder(folderId, name);
    }

    /** {@inheritDoc} */
    public boolean nameExistsInParentFolder(final String id,
                                            final String name) {
        return _delegate.nameExistsInParentFolder(id, name);
    }

    /** {@inheritDoc} */
    public ResourceSummary resource(final String resourceId) {
        return _delegate.resource(resourceId);
    }

    /** {@inheritDoc} */
    public Collection<ResourceSummary> roots() {
        return _delegate.roots();
    }

    /** {@inheritDoc} */
    public boolean templateNameExists(final String templateName) {
        return _delegate.templateNameExists(templateName);
    }

    /** {@inheritDoc} */
    public Collection<TemplateDelta> templates() {
        return _delegate.templates();
    }

    /** {@inheritDoc} */
    public boolean usernameExists(final String username) {
        return _delegate.usernameExists(username);
    }

    /** {@inheritDoc} */
    @Override
    public TemplateDelta templateDelta(final String templateId) {
        return _delegate.templateDelta(templateId);
    }

    /** {@inheritDoc} */
    @Override
    public UserDelta userDelta(final String userId) {
        return _delegate.userDelta(userId);
    }

    /** {@inheritDoc} */
    @Override
    public AliasDelta aliasDelta(final String aliasId) {
        return _delegate.aliasDelta(aliasId);
    }
}
