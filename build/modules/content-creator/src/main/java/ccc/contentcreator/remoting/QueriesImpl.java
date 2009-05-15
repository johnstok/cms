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
import java.util.Map;

import javax.ejb.EJB;

import ccc.api.ActionSummary;
import ccc.api.AliasDelta;
import ccc.api.Duration;
import ccc.api.FileDelta;
import ccc.api.FileSummary;
import ccc.api.ID;
import ccc.api.LogEntrySummary;
import ccc.api.PageDelta;
import ccc.api.Queries;
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;
import ccc.api.TemplateSummary;
import ccc.api.UserDelta;
import ccc.api.UserSummary;
import ccc.api.Username;
import ccc.contentcreator.api.QueriesService;


/**
 * GWT implementation of the {@link Queries} interface.
 *
 * @author Civic Computing Ltd.
 */
public class QueriesImpl
    extends CCCRemoteServiceServlet
    implements QueriesService {

    @EJB(name=Queries.NAME) private transient Queries _queries;

    /** {@inheritDoc} */
    public String getAbsolutePath(final ID resourceId) {
        return _queries.getAbsolutePath(resourceId);
    }

    /** {@inheritDoc} */
    public Collection<ResourceSummary> getChildren(final ID folderId) {
        return _queries.getChildren(folderId);
    }

    /** {@inheritDoc} */
    public Collection<ResourceSummary> getFolderChildren(final ID folderId) {
        return _queries.getFolderChildren(folderId);
    }

    /** {@inheritDoc} */
    public Collection<LogEntrySummary> history(final ID resourceId) {
        return _queries.history(resourceId);
    }

    /** {@inheritDoc} */
    public Collection<UserSummary> listUsers() {
        return _queries.listUsers();
    }

    /** {@inheritDoc} */
    public Collection<UserSummary> listUsersWithEmail(final String email) {
        return _queries.listUsersWithEmail(email);
    }

    /** {@inheritDoc} */
    public Collection<UserSummary> listUsersWithRole(final String role) {
        return _queries.listUsersWithRole(role);
    }

    /** {@inheritDoc} */
    public Collection<UserSummary> listUsersWithUsername(
                                                    final String username) {
        return _queries.listUsersWithUsername(username);
    }

    /** {@inheritDoc} */
    public Collection<ResourceSummary> locked() {
        return _queries.locked();
    }

    /** {@inheritDoc} */
    public Collection<ResourceSummary> lockedByCurrentUser() {
        return _queries.lockedByCurrentUser();
    }

    /** {@inheritDoc} */
    public UserSummary loggedInUser() {
        return _queries.loggedInUser();
    }

    /** {@inheritDoc} */
    public boolean nameExistsInFolder(final ID folderId,
                                      final String name) {
        return _queries.nameExistsInFolder(folderId, name);
    }

    /** {@inheritDoc} */
    public ResourceSummary resource(final ID resourceId) {
        return _queries.resource(resourceId);
    }

    /** {@inheritDoc} */
    public Collection<ResourceSummary> roots() {
        return _queries.roots();
    }

    /** {@inheritDoc} */
    public boolean templateNameExists(final String templateName) {
        return _queries.templateNameExists(templateName);
    }

    /** {@inheritDoc} */
    public Collection<TemplateSummary> templates() {
        return _queries.templates();
    }

    /** {@inheritDoc} */
    public boolean usernameExists(final Username username) {
        return _queries.usernameExists(username);
    }

    /** {@inheritDoc} */
    @Override
    public TemplateDelta templateDelta(final ID templateId) {
        return _queries.templateDelta(templateId);
    }

    /** {@inheritDoc} */
    @Override
    public UserDelta userDelta(final ID userId) {
        return _queries.userDelta(userId);
    }

    /** {@inheritDoc} */
    @Override
    public AliasDelta aliasDelta(final ID aliasId) {
        return _queries.aliasDelta(aliasId);
    }

    /** {@inheritDoc} */
    @Override
    public PageDelta pageDelta(final ID pageId) {
        return _queries.pageDelta(pageId);
    }

    /** {@inheritDoc} */
    @Override
    public FileDelta fileDelta(final ID fileId) {
        return _queries.fileDelta(fileId);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<FileSummary> getAllContentImages() {
        return _queries.getAllContentImages();
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, String> metadata(final ID resourceId) {
        return _queries.metadata(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ActionSummary> listPendingActions() {
        return _queries.listPendingActions();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ActionSummary> listCompletedActions() {
        return _queries.listCompletedActions();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<String> roles(final ID resourceId) {
        return _queries.roles(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public Duration cacheDuration(final ID resourceId) {
        return _queries.cacheDuration(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public TemplateSummary computeTemplate(final ID resourceId) {
        return _queries.computeTemplate(resourceId);
    }
}
