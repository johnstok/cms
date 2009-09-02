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

package ccc.rest.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ccc.rest.CommandFailedException;
import ccc.rest.Queries;
import ccc.rest.RestCommands;
import ccc.rest.dto.ActionNew;
import ccc.rest.dto.ActionSummary;
import ccc.rest.dto.AliasDelta;
import ccc.rest.dto.AliasNew;
import ccc.rest.dto.FileDelta;
import ccc.rest.dto.FileSummary;
import ccc.rest.dto.LogEntrySummary;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.PageNew;
import ccc.rest.dto.ResourceCacheDurationPU;
import ccc.rest.dto.ResourceRevisionPU;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.ResourceTemplatePU;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateNew;
import ccc.rest.dto.TemplateSummary;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.types.Duration;
import ccc.types.Failure;
import ccc.types.FailureCode;
import ccc.types.ID;
import ccc.types.Paragraph;


/**
 * This class exposes parts of our public API using JAX-RS.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure")
@Consumes("application/json")
@Produces("application/json")
public class RestApi
    extends
        JaxrsCollection
    implements
        Queries,
        RestCommands {

    /** {@inheritDoc} */
    @Override
    public String aliasTargetName(final ID aliasId) {
        return getQueries().aliasTargetName(aliasId);
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
    public Map<String, String> metadata(final ID resourceId) {
        return getQueries().metadata(resourceId);
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
    public void deleteCacheDuration(final ID id) throws CommandFailedException {
        getCommands().updateCacheDuration(id, null);
    }


    /** {@inheritDoc} */
    @Override
    public void fail() throws CommandFailedException {
        throw new CommandFailedException(new Failure(FailureCode.PRIVILEGES));
    }
}
