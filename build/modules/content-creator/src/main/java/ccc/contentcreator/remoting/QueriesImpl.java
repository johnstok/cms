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

import ccc.contentcreator.api.QueriesService;
import ccc.services.api.ActionSummary;
import ccc.services.api.AliasDelta;
import ccc.services.api.FileDelta;
import ccc.services.api.FileSummary;
import ccc.services.api.LogEntrySummary;
import ccc.services.api.PageDelta;
import ccc.services.api.ResourceDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class QueriesImpl
    extends CCCRemoteServiceServlet
    implements QueriesService {

    /** {@inheritDoc} */
    public String getAbsolutePath(final String resourceId) {
        return _services.lookupQueries().getAbsolutePath(resourceId);
    }

    /** {@inheritDoc} */
    public Collection<ResourceSummary> getChildren(final String folderId) {
        return _services.lookupQueries().getChildren(folderId);
    }

    /** {@inheritDoc} */
    public Collection<ResourceSummary> getFolderChildren(final String folderId) {
        return _services.lookupQueries().getFolderChildren(folderId);
    }

    /** {@inheritDoc} */
    public TemplateDelta getTemplateForResource(final String resourceId) {
        return _services.lookupQueries().getTemplateForResource(resourceId);
    }

    /** {@inheritDoc} */
    public Collection<LogEntrySummary> history(final String resourceId) {
        return _services.lookupQueries().history(resourceId);
    }

    /** {@inheritDoc} */
    public Collection<UserSummary> listUsers() {
        return _services.lookupQueries().listUsers();
    }

    /** {@inheritDoc} */
    public Collection<UserSummary> listUsersWithEmail(final String email) {
        return _services.lookupQueries().listUsersWithEmail(email);
    }

    /** {@inheritDoc} */
    public Collection<UserSummary> listUsersWithRole(final String role) {
        return _services.lookupQueries().listUsersWithRole(role);
    }

    /** {@inheritDoc} */
    public Collection<UserSummary> listUsersWithUsername(
                                                    final String username) {
        return _services.lookupQueries().listUsersWithUsername(username);
    }

    /** {@inheritDoc} */
    public Collection<ResourceSummary> locked() {
        return _services.lookupQueries().locked();
    }

    /** {@inheritDoc} */
    public Collection<ResourceSummary> lockedByCurrentUser() {
        return _services.lookupQueries().lockedByCurrentUser();
    }

    /** {@inheritDoc} */
    public UserSummary loggedInUser() {
        return _services.lookupQueries().loggedInUser();
    }

    /** {@inheritDoc} */
    public boolean nameExistsInFolder(final String folderId,
                                      final String name) {
        return _services.lookupQueries().nameExistsInFolder(folderId, name);
    }

    /** {@inheritDoc} */
    public ResourceSummary resource(final String resourceId) {
        return _services.lookupQueries().resource(resourceId);
    }

    /** {@inheritDoc} */
    public Collection<ResourceSummary> roots() {
        return _services.lookupQueries().roots();
    }

    /** {@inheritDoc} */
    public boolean templateNameExists(final String templateName) {
        return _services.lookupQueries().templateNameExists(templateName);
    }

    /** {@inheritDoc} */
    public Collection<TemplateDelta> templates() {
        return _services.lookupQueries().templates();
    }

    /** {@inheritDoc} */
    public boolean usernameExists(final String username) {
        return _services.lookupQueries().usernameExists(username);
    }

    /** {@inheritDoc} */
    @Override
    public TemplateDelta templateDelta(final String templateId) {
        return _services.lookupQueries().templateDelta(templateId);
    }

    /** {@inheritDoc} */
    @Override
    public UserDelta userDelta(final String userId) {
        return _services.lookupQueries().userDelta(userId);
    }

    /** {@inheritDoc} */
    @Override
    public AliasDelta aliasDelta(final String aliasId) {
        return _services.lookupQueries().aliasDelta(aliasId);
    }

    /** {@inheritDoc} */
    @Override
    public PageDelta pageDelta(final String pageId) {
        return _services.lookupQueries().pageDelta(pageId);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceDelta folderDelta(final String folderId) {
        return _services.lookupQueries().folderDelta(folderId);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceDelta resourceDelta(final String resourceId) {
        return _services.lookupQueries().resourceDelta(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public FileDelta fileDelta(final String fileId) {
        return _services.lookupQueries().fileDelta(fileId);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<FileSummary> getAllImages() {
        return _services.lookupQueries().getAllImages();
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, String> metadata(final String resourceId) {
        return _services.lookupQueries().metadata(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public PageDelta workingCopyDelta(final String pageId) {
        return _services.lookupQueries().workingCopyDelta(pageId);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ActionSummary> listPendingActions() {
        return _services.lookupQueries().listPendingActions();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ActionSummary> listCompletedActions() {
        return _services.lookupQueries().listCompletedActions();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<String> roles(final String resourceId) {
        return _services.lookupQueries().roles(resourceId);
    }
}
