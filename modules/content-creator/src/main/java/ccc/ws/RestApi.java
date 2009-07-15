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

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ccc.api.ActionSummary;
import ccc.api.AliasDelta;
import ccc.api.CommandFailedException;
import ccc.api.Commands;
import ccc.api.Duration;
import ccc.api.FileDelta;
import ccc.api.FileSummary;
import ccc.api.ID;
import ccc.api.LogEntrySummary;
import ccc.api.PageDelta;
import ccc.api.Queries;
import ccc.api.ResourceSummary;
import ccc.api.RestCommands;
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
@Consumes("application/json")
@Produces("application/json")
public class RestApi
    implements
        Queries, RestCommands {

    private final Registry _reg = new JNDI();
    private final String _appName = CCCProperties.get("application.name"); // TODO: Refactor constant

    private Queries _queries;
    private Commands _commands;


    /**
     * Accessor.
     *
     * @return Returns the queries.
     */
    public final Queries getQueries() {
        return
            (null==_queries)
                ? (Queries) _reg.get(_appName+"/"+Queries.NAME+"/remote")
                : _queries;
    }


    /**
     * Mutator.
     *
     * @param queries The queries to set.
     */
    public final void setQueries(final Queries queries) {
        _queries = queries;
    }


    /**
     * Accessor.
     *
     * @return Returns the commands.
     */
    public final Commands getCommands() {
        return
            (null==_commands)
                ? (Commands) _reg.get(_appName+"/"+Commands.NAME+"/remote")
                : _commands;
    }


    /**
     * Mutator.
     *
     * @param commands The commands to set.
     */
    public final void setCommands(final Commands commands) {
        _commands = commands;
    }


    /** {@inheritDoc} */
    @Override
    public AliasDelta aliasDelta(final ID aliasId) {
        return getQueries().aliasDelta(aliasId);
    }


    /** {@inheritDoc} */
    @Override
    public Duration cacheDuration(final ID resourceId) {
        return getQueries().cacheDuration(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public TemplateSummary computeTemplate(final ID resourceId) {
        return getQueries().computeTemplate(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public FileDelta fileDelta(final ID fileId) {
        return getQueries().fileDelta(fileId);
    }


    /** {@inheritDoc} */
    @Override
    public String getAbsolutePath(final ID resourceId) {
        return getQueries().getAbsolutePath(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<FileSummary> getAllContentImages() {
        return getQueries().getAllContentImages();
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getChildren(final ID folderId) {
        return getQueries().getChildren(folderId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getFolderChildren(final ID folderId) {
        return getQueries().getFolderChildren(folderId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<LogEntrySummary> history(final ID resourceId) {
        return getQueries().history(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ActionSummary> listCompletedActions() {
        return getQueries().listCompletedActions();
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ActionSummary> listPendingActions() {
        return getQueries().listPendingActions();
    }


    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> listUsers() {
        return getQueries().listUsers();
    }


    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> listUsersWithEmail(final String email) {
        return getQueries().listUsersWithEmail(email);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> listUsersWithRole(final String role) {
        return getQueries().listUsersWithRole(role);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> listUsersWithUsername(final String username) {
        return getQueries().listUsersWithUsername(username);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> locked() {
        return getQueries().locked();
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> lockedByCurrentUser() {
        return getQueries().lockedByCurrentUser();
    }


    /** {@inheritDoc} */
    @Override
    public UserSummary loggedInUser() {
        return getQueries().loggedInUser();
    }


    /** {@inheritDoc} */
    @Override
    public Map<String, String> metadata(final ID resourceId) {
        return getQueries().metadata(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public boolean nameExistsInFolder(final ID folderId, final String name) {
        return getQueries().nameExistsInFolder(folderId, name);
    }


    /** {@inheritDoc} */
    @Override
    public PageDelta pageDelta(final ID pageId) {
        return getQueries().pageDelta(pageId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resource(final ID resourceId) {
        return getQueries().resource(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resourceForLegacyId(final String legacyId) {
        return getQueries().resourceForLegacyId(legacyId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resourceForPath(final String path) {
        return getQueries().resourceForPath(path);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<String> roles(final ID resourceId) {
        return getQueries().roles(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> roots() {
        return getQueries().roots();
    }


    /** {@inheritDoc} */
    @Override
    public TemplateDelta templateDelta(final ID templateId) {
        return getQueries().templateDelta(templateId);
    }


    /** {@inheritDoc} */
    @Override
    public boolean templateNameExists(final String templateName) {
        return getQueries().templateNameExists(templateName);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<TemplateSummary> templates() {
        return getQueries().templates();
    }


    /** {@inheritDoc} */
    @Override
    public UserDelta userDelta(final ID userId) {
        return getQueries().userDelta(userId);
    }


    /** {@inheritDoc} */
    @Override
    public boolean usernameExists(final Username username) {
        return getQueries().usernameExists(username);
    }


    /** {@inheritDoc} */
    @Override
    public void updateCacheDuration(final ID resourceId,
                                    final Duration duration) throws CommandFailedException {
        getCommands().updateCacheDuration(resourceId, duration);
    }


    /** {@inheritDoc} */
    @Override
    public void lock(final ID resourceId) throws CommandFailedException {
        getCommands().lock(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public UserSummary createUser(final UserDelta delta,
                                  final String password) throws CommandFailedException {
        return getCommands().createUser(delta, password);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createTemplate(final ID parentId,
                                          final TemplateDelta delta,
                                          final String title,
                                          final String description,
                                          final String name) throws CommandFailedException {
        return getCommands().createTemplate(parentId, delta, title, description, name);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createPage(final ID parentId,
                                      final PageDelta delta,
                                      final String name,
                                      final boolean publish,
                                      final ID templateId,
                                      final String title) throws CommandFailedException {
        return getCommands().createPage(parentId, delta, name, publish, templateId, title);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final ID parentId,
                                        final String name) throws CommandFailedException {
        return getCommands().createFolder(parentId, name);
    }


    /** {@inheritDoc} */
    @Override
    public void updateUserPassword(final ID userId,
                                   final String password) throws CommandFailedException {
        getCommands().updateUserPassword(userId, password);
    }


    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopy(final ID resourceId) throws CommandFailedException {
        getCommands().applyWorkingCopy(resourceId);
    }
}
