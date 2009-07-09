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

package ccc.ws;

import java.util.Collection;
import java.util.Map;

import javax.ws.rs.Path;

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
import ccc.commons.CCCProperties;
import ccc.commons.JNDI;
import ccc.commons.Registry;


/**
 * This class exposes parts of our public API using JAX-RS.
 *
 * @author Civic Computing Ltd.
 */
@Path("/")
public class RestApi
    implements
        Queries {

    private final Queries _delegate;

    // /**
    // * Constructor.
    // *
    // * @param queries
    // */
    // RestApi(final Queries queries) {
    // _queries = queries;
    // }

    /**
     * Constructor.
     *
     * @param queries
     */
    public RestApi() {
        final Registry r = new JNDI();
        final String appName = CCCProperties.get("application.name"); // TODO: Refactor constant
        _delegate = r.get(appName + "/" + Queries.NAME + "/remote");
    }

    /**
     * @param aliasId
     * @return
     * @see ccc.api.Queries#aliasDelta(ccc.api.ID)
     */
    public AliasDelta aliasDelta(final ID aliasId) {
        return _delegate.aliasDelta(aliasId);
    }

    /**
     * @param resourceId
     * @return
     * @see ccc.api.Queries#cacheDuration(ccc.api.ID)
     */
    public Duration cacheDuration(final ID resourceId) {
        return _delegate.cacheDuration(resourceId);
    }

    /**
     * @param resourceId
     * @return
     * @see ccc.api.Queries#computeTemplate(ccc.api.ID)
     */
    public TemplateSummary computeTemplate(final ID resourceId) {
        return _delegate.computeTemplate(resourceId);
    }

    /**
     * @param fileId
     * @return
     * @see ccc.api.Queries#fileDelta(ccc.api.ID)
     */
    public FileDelta fileDelta(final ID fileId) {
        return _delegate.fileDelta(fileId);
    }

    /**
     * @param resourceId
     * @return
     * @see ccc.api.Queries#getAbsolutePath(ccc.api.ID)
     */
    public String getAbsolutePath(final ID resourceId) {
        return _delegate.getAbsolutePath(resourceId);
    }

    /**
     * @return
     * @see ccc.api.Queries#getAllContentImages()
     */
    public Collection<FileSummary> getAllContentImages() {
        return _delegate.getAllContentImages();
    }

    /**
     * @param folderId
     * @return
     * @see ccc.api.Queries#getChildren(ccc.api.ID)
     */
    public Collection<ResourceSummary> getChildren(final ID folderId) {
        return _delegate.getChildren(folderId);
    }

    /**
     * @param folderId
     * @return
     * @see ccc.api.Queries#getFolderChildren(ccc.api.ID)
     */
    public Collection<ResourceSummary> getFolderChildren(final ID folderId) {
        return _delegate.getFolderChildren(folderId);
    }

    /**
     * @param resourceId
     * @return
     * @see ccc.api.Queries#history(ccc.api.ID)
     */
    public Collection<LogEntrySummary> history(final ID resourceId) {
        return _delegate.history(resourceId);
    }

    /**
     * @return
     * @see ccc.api.Queries#listCompletedActions()
     */
    public Collection<ActionSummary> listCompletedActions() {
        return _delegate.listCompletedActions();
    }

    /**
     * @return
     * @see ccc.api.Queries#listPendingActions()
     */
    public Collection<ActionSummary> listPendingActions() {
        return _delegate.listPendingActions();
    }

    /**
     * @return
     * @see ccc.api.Queries#listUsers()
     */
    public Collection<UserSummary> listUsers() {
        return _delegate.listUsers();
    }

    /**
     * @param email
     * @return
     * @see ccc.api.Queries#listUsersWithEmail(java.lang.String)
     */
    public Collection<UserSummary> listUsersWithEmail(final String email) {
        return _delegate.listUsersWithEmail(email);
    }

    /**
     * @param role
     * @return
     * @see ccc.api.Queries#listUsersWithRole(java.lang.String)
     */
    public Collection<UserSummary> listUsersWithRole(final String role) {
        return _delegate.listUsersWithRole(role);
    }

    /**
     * @param username
     * @return
     * @see ccc.api.Queries#listUsersWithUsername(java.lang.String)
     */
    public Collection<UserSummary> listUsersWithUsername(final String username) {
        return _delegate.listUsersWithUsername(username);
    }

    /**
     * @return
     * @see ccc.api.Queries#locked()
     */
    public Collection<ResourceSummary> locked() {
        return _delegate.locked();
    }

    /**
     * @return
     * @see ccc.api.Queries#lockedByCurrentUser()
     */
    public Collection<ResourceSummary> lockedByCurrentUser() {
        return _delegate.lockedByCurrentUser();
    }

    /**
     * @return
     * @see ccc.api.Queries#loggedInUser()
     */
    public UserSummary loggedInUser() {
        return _delegate.loggedInUser();
    }

    /**
     * @param resourceId
     * @return
     * @see ccc.api.Queries#metadata(ccc.api.ID)
     */
    public Map<String, String> metadata(final ID resourceId) {
        return _delegate.metadata(resourceId);
    }

    /**
     * @param folderId
     * @param name
     * @return
     * @see ccc.api.Queries#nameExistsInFolder(ccc.api.ID, java.lang.String)
     */
    public boolean nameExistsInFolder(final ID folderId, final String name) {
        return _delegate.nameExistsInFolder(folderId, name);
    }

    /**
     * @param pageId
     * @return
     * @see ccc.api.Queries#pageDelta(ccc.api.ID)
     */
    public PageDelta pageDelta(final ID pageId) {
        return _delegate.pageDelta(pageId);
    }

    /**
     * @param resourceId
     * @return
     * @see ccc.api.Queries#resource(ccc.api.ID)
     */
    public ResourceSummary resource(final ID resourceId) {
        return _delegate.resource(resourceId);
    }

    /**
     * @param legacyId
     * @return
     * @see ccc.api.Queries#resourceForLegacyId(java.lang.String)
     */
    public ResourceSummary resourceForLegacyId(final String legacyId) {
        return _delegate.resourceForLegacyId(legacyId);
    }

    /**
     * @param path
     * @return
     * @see ccc.api.Queries#resourceForPath(java.lang.String)
     */
    public ResourceSummary resourceForPath(final String path) {
        return _delegate.resourceForPath(path);
    }

    /**
     * @param resourceId
     * @return
     * @see ccc.api.Queries#roles(ccc.api.ID)
     */
    public Collection<String> roles(final ID resourceId) {
        return _delegate.roles(resourceId);
    }

    /**
     * @return
     * @see ccc.api.Queries#roots()
     */
    public Collection<ResourceSummary> roots() {
        return _delegate.roots();
    }

    /**
     * @param templateId
     * @return
     * @see ccc.api.Queries#templateDelta(ccc.api.ID)
     */
    public TemplateDelta templateDelta(final ID templateId) {
        return _delegate.templateDelta(templateId);
    }

    /**
     * @param templateName
     * @return
     * @see ccc.api.Queries#templateNameExists(java.lang.String)
     */
    public boolean templateNameExists(final String templateName) {
        return _delegate.templateNameExists(templateName);
    }

    /**
     * @return
     * @see ccc.api.Queries#templates()
     */
    public Collection<TemplateSummary> templates() {
        return _delegate.templates();
    }

    /**
     * @param userId
     * @return
     * @see ccc.api.Queries#userDelta(ccc.api.ID)
     */
    public UserDelta userDelta(final ID userId) {
        return _delegate.userDelta(userId);
    }

    /**
     * @param username
     * @return
     * @see ccc.api.Queries#usernameExists(ccc.api.Username)
     */
    public boolean usernameExists(final Username username) {
        return _delegate.usernameExists(username);
    }
}
