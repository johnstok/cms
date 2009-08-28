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

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ccc.api.ActionSummary;
import ccc.api.AliasDelta;
import ccc.api.FileDelta;
import ccc.api.FileSummary;
import ccc.api.LogEntrySummary;
import ccc.api.PageDelta;
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;
import ccc.api.TemplateSummary;
import ccc.api.UserSummary;
import ccc.api.rest.ActionNew;
import ccc.api.rest.AliasNew;
import ccc.api.rest.FolderDelta;
import ccc.api.rest.FolderNew;
import ccc.api.rest.PageNew;
import ccc.api.rest.ResourceCacheDurationPU;
import ccc.api.rest.ResourceRevisionPU;
import ccc.api.rest.ResourceTemplatePU;
import ccc.api.rest.TemplateNew;
import ccc.commons.CCCProperties;
import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.domain.CommandFailedException;
import ccc.domain.Failure;
import ccc.rest.Queries;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.services.Commands;
import ccc.services.FolderCommands;
import ccc.services.PageCommands;
import ccc.services.UserCommands;
import ccc.types.Duration;
import ccc.types.FailureCode;
import ccc.types.ID;
import ccc.types.Paragraph;
import ccc.types.Username;


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

    private Queries _queries;
    private Commands _commands;
    private PageCommands _pageCommands;
    private FolderCommands _folderCommands;
    private UserCommands _userCommands;


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
     * Accessor.
     *
     * @return Returns the commands.
     */
    public final PageCommands getPageCommands() {
        return
        (null==_pageCommands)
        ? (PageCommands) _reg.get(_appName+"/"+PageCommands.NAME+"/remote")
            : _pageCommands;
    }

    /**
     * Accessor.
     *
     * @return Returns the commands.
     */
    public final FolderCommands getFolderCommands() {
        return
        (null==_folderCommands)
        ? (FolderCommands) _reg.get(_appName+"/"+FolderCommands.NAME+"/remote")
            : _folderCommands;
    }

    /**
     * Accessor.
     *
     * @return Returns the commands.
     */
    public final UserCommands getUserCommands() {
        return
        (null==_userCommands)
        ? (UserCommands) _reg.get(_appName+"/"+UserCommands.NAME+"/remote")
            : _userCommands;
    }


    /**
     * Mutator.
     *
     * @param commands The commands to set.
     */
    public final void setCommands(final Commands commands) {
        _commands = commands;
    }

    /**
     * Mutator.
     *
     * @param commands The commands to set.
     */
    public final void setPageCommands(final PageCommands pageCommands) {
        _pageCommands = pageCommands;
    }

    /**
     * Mutator.
     *
     * @param commands The commands to set.
     */
    public final void setFolderCommands(final FolderCommands folderCommands) {
        _folderCommands = folderCommands;
    }

    /**
     * Mutator.
     *
     * @param commands The commands to set.
     */
    public final void setUserCommands(final UserCommands userCommands) {
        _userCommands = userCommands;
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
    public Collection<ResourceSummary> getChildrenManualOrder(final ID folderId) {
        return getQueries().getChildrenManualOrder(folderId);
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
    public UserSummary userDelta(final ID userId) {
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
                                    final ResourceCacheDurationPU pu) throws CommandFailedException {
        getCommands().updateCacheDuration(resourceId, pu.getCacheDuration());
    }


    /** {@inheritDoc} */
    @Override
    public void lock(final ID resourceId) throws CommandFailedException {
        getCommands().lock(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public UserSummary createUser(final UserSummary user) throws CommandFailedException {
        return getUserCommands().createUser(user);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createTemplate(final TemplateNew template) throws CommandFailedException {
        return getCommands().createTemplate(
            template.getParentId(),
            template.getDelta(),
            template.getTitle(),
            template.getDescription(),
            template.getName());
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createPage(final PageNew page) throws CommandFailedException {
        return getPageCommands().createPage(
            page.getParentId(),
            page.getDelta(),
            page.getName(),
            false,
            page.getTemplateId(),
            page.getTitle(),
            page.getComment(),
            page.getMajorChange());
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final FolderNew folder) throws CommandFailedException {
        return getFolderCommands().createFolder(folder.getParentId(), folder.getName());
    }


    /** {@inheritDoc} */
    @Override
    public void updateUserPassword(final ID userId,
                                   final UserSummary pu) throws CommandFailedException {
        getUserCommands().updateUserPassword(userId, pu.getPassword());
    }


    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopy(final ID resourceId) throws CommandFailedException {
        getCommands().applyWorkingCopy(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final ID resourceId,
                                       final ResourceTemplatePU pu) throws CommandFailedException {
        getCommands().updateResourceTemplate(resourceId, pu.getTemplateId());
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
    public void createAction(final ActionNew action) throws CommandFailedException {
        getCommands().createAction(
            action.getResourceId(),
            action.getAction(),
            new Date(action.getExecuteAfter()),
            action.getParameters());
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
        getPageCommands().updateWorkingCopy(pageId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final ID pageId) throws CommandFailedException {
        getPageCommands().clearWorkingCopy(pageId);
    }


    /** {@inheritDoc} */
    @Override
    public void cancelAction(final ID actionId) throws CommandFailedException {
        getCommands().cancelAction(actionId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createAlias(final AliasNew alias) throws CommandFailedException {
        return getCommands().createAlias(
            alias.getParentId(), alias.getName(), alias.getTargetId());
    }


    /** {@inheritDoc} */
    @Override
    public void createWorkingCopy(final ID resourceId, final ResourceRevisionPU pu) throws CommandFailedException {
        getCommands().createWorkingCopy(resourceId, pu.getRevision());
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
        getPageCommands().updatePage(pageId, delta, comment, majorEdit);
    }


    /** {@inheritDoc} */
    @Override
    public void updateFolder(final ID folderId,
                             final FolderDelta delta) throws CommandFailedException {
        getFolderCommands().updateFolder(folderId,
                                   delta.getSortOrder(),
                                   delta.getIndexPage(),
                                   delta.getSortList());
    }


    /** {@inheritDoc} */
    @Override
    public void updateUser(final ID userId, final UserSummary delta) throws CommandFailedException {
        getUserCommands().updateUser(userId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public void deleteCacheDuration(final ID id) throws CommandFailedException {
        getCommands().updateCacheDuration(id, null);
    }


    /** {@inheritDoc} */
    @Override
    public void fail() throws CommandFailedException {
        throw new CommandFailedException(new Failure(FailureCode.PRIVILEGES));
    }

    /** {@inheritDoc} */
    @Override
    public void updateYourUser(final ID userId, final UserSummary user) throws CommandFailedException {
        getUserCommands().updateYourUser(userId, user.getEmail(), user.getPassword());
    }
}
