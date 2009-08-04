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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import ccc.api.ActionSummary;
import ccc.api.AliasDelta;
import ccc.api.CommandType;
import ccc.api.Duration;
import ccc.api.FailureCodes;
import ccc.api.FileDelta;
import ccc.api.FileSummary;
import ccc.api.ID;
import ccc.api.Json;
import ccc.api.JsonKeys;
import ccc.api.LogEntrySummary;
import ccc.api.PageDelta;
import ccc.api.Paragraph;
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;
import ccc.api.TemplateSummary;
import ccc.api.UserDelta;
import ccc.api.UserSummary;
import ccc.api.Username;
import ccc.commands.CommandFailedException;
import ccc.commons.CCCProperties;
import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.domain.Failure;
import ccc.services.Commands;
import ccc.services.Queries;


/**
 * This class exposes parts of our public API using JAX-RS.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure")
@Consumes("application/json")
@Produces("application/json")
public class RestApi
    implements
        Queries, RestCommands {

    private final Registry _reg = new JNDI();
    private final String _appName = CCCProperties.get("application.name"); // TODO: Refactor constant

    private @Context HttpServletRequest _request;
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


    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final ID resourceId, final ID templateId) throws CommandFailedException {
        getCommands().updateResourceTemplate(resourceId, templateId);
    }


    /** {@inheritDoc} */
    @Override
    public void changeRoles(final ID resourceId, final Collection<String> roles) throws CommandFailedException {
        getCommands().changeRoles(resourceId, roles);
    }


    /** {@inheritDoc} */
    @Override
    public void move(final ID resourceId, final ID newParentId) throws CommandFailedException {
        getCommands().move(resourceId, newParentId);
    }


    /** {@inheritDoc} */
    @Override
    public void publish(final ID resourceId) throws CommandFailedException {
        getCommands().publish(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void rename(final ID resourceId, final String name) throws CommandFailedException {
        getCommands().rename(resourceId, name);
    }


    /** {@inheritDoc} */
    @Override
    public void unlock(final ID resourceId) throws CommandFailedException {
        getCommands().unlock(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void unpublish(final ID resourceId) throws CommandFailedException {
        getCommands().unpublish(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void createAction(final ID resourceId,
                             final CommandType action,
                             final long executeAfter,
                             final Map<String, String> parameters) throws CommandFailedException {
        getCommands().createAction(
            resourceId, action, new Date(executeAfter), parameters);
    }


    /** {@inheritDoc} */
    @Override
    public void excludeFromMainMenu(final ID resourceId) throws CommandFailedException {
        getCommands().includeInMainMenu(resourceId, false);
    }


    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final ID resourceId) throws CommandFailedException {
        getCommands().includeInMainMenu(resourceId, true);
    }


    /** {@inheritDoc} */
    @Override
    public void updateMetadata(final ID resourceId, final Json json) throws CommandFailedException {
        final String title = json.getString("title");
        final String description = json.getString("description");
        final String tags = json.getString("tags");
        final Map<String, String> metadata = json.getStringMap("metadata");
        getCommands().updateMetadata(resourceId, title, description, tags, metadata);
    }


    /** {@inheritDoc} */
    @Override
    public List<String> validateFields(final Json json) {
        final String def = json.getString("definition");
        final Set<Paragraph> p = new HashSet<Paragraph>();
        for (final Json j : json.getCollection("paragraphs")) {
            p.add(new Paragraph(j));
        }
        return getCommands().validateFields(p, def);
    }


    /** {@inheritDoc} */
    @Override
    public void updateWorkingCopy(final ID pageId, final PageDelta delta) throws CommandFailedException {
        getCommands().updateWorkingCopy(pageId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final ID pageId) throws CommandFailedException {
        getCommands().clearWorkingCopy(pageId);
    }


    /** {@inheritDoc} */
    @Override
    public void cancelAction(final ID actionId) throws CommandFailedException {
        getCommands().cancelAction(actionId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createAlias(final ID parentId, final String name, final ID targetId) throws CommandFailedException {
        return getCommands().createAlias(parentId, name, targetId);
    }


    /** {@inheritDoc} */
    @Override
    public void createWorkingCopy(final ID resourceId, final long index) throws CommandFailedException {
        getCommands().createWorkingCopy(resourceId, index);
    }


    /** {@inheritDoc} */
    @Override
    public void updateTemplate(final ID templateId, final TemplateDelta delta) throws CommandFailedException {
        getCommands().updateTemplate(templateId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public void updateAlias(final ID aliasId, final AliasDelta delta) throws CommandFailedException {
        getCommands().updateAlias(aliasId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public void updatePage(final ID pageId, final Json json) throws CommandFailedException {
        final boolean majorEdit = json.getBool(JsonKeys.MAJOR_CHANGE).booleanValue();
        final String comment = json.getString(JsonKeys.COMMENT);
        final PageDelta delta = new PageDelta(json.getJson(JsonKeys.DELTA));
        getCommands().updatePage(pageId, delta, comment, majorEdit);
    }


    /** {@inheritDoc} */
    @Override
    public void updateFolder(final ID folderId, final String sortOrder, final ID indexPageId) throws CommandFailedException {
        getCommands().updateFolder(folderId, sortOrder, indexPageId);
    }


    /** {@inheritDoc} */
    @Override
    public void updateUser(final ID userId, final UserDelta delta) throws CommandFailedException {
        getCommands().updateUser(userId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public void reorder(final ID folderId, final List<String> order) throws CommandFailedException {
        getCommands().reorder(folderId, order);
    }


    /** {@inheritDoc} */
    @Override
    public void deleteCacheDuration(final ID id) throws CommandFailedException {
        getCommands().updateCacheDuration(id, null);
    }


    /** {@inheritDoc} */
    @Override
    public void fail() throws CommandFailedException {
        throw new CommandFailedException(new Failure(FailureCodes.PRIVILEGES, "a"));
    }
}