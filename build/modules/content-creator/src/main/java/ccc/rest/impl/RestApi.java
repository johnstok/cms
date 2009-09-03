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
import java.util.Map;
import java.util.UUID;

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
import ccc.rest.dto.ResourceCacheDurationPU;
import ccc.rest.dto.ResourceRevisionPU;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.ResourceTemplatePU;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateNew;
import ccc.rest.dto.TemplateSummary;
import ccc.serialization.Json;
import ccc.types.Duration;
import ccc.types.Failure;
import ccc.types.FailureCode;


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
    public String aliasTargetName(final UUID aliasId) {
        return getQueries().aliasTargetName(aliasId);
    }


    /** {@inheritDoc} */
    @Override
    public Duration cacheDuration(final UUID resourceId) {
        return getQueries().cacheDuration(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public TemplateSummary computeTemplate(final UUID resourceId) {
        return getQueries().computeTemplate(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public FileDelta fileDelta(final UUID fileId) {
        return getQueries().fileDelta(fileId);
    }


    /** {@inheritDoc} */
    @Override
    public String getAbsolutePath(final UUID resourceId) {
        return getQueries().getAbsolutePath(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<FileSummary> getAllContentImages() {
        return getQueries().getAllContentImages();
    }


    /** {@inheritDoc} */
    @Override
    public Collection<LogEntrySummary> history(final UUID resourceId) {
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
    public Map<String, String> metadata(final UUID resourceId) {
        return getQueries().metadata(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resource(final UUID resourceId) {
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
    public Collection<String> roles(final UUID resourceId) {
        return getQueries().roles(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public TemplateDelta templateDelta(final UUID templateId) {
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
    public void updateCacheDuration(final UUID resourceId,
                                    final ResourceCacheDurationPU pu) throws CommandFailedException {
        getCommands().updateCacheDuration(resourceId, pu.getCacheDuration());
    }


    /** {@inheritDoc} */
    @Override
    public void lock(final UUID resourceId) throws CommandFailedException {
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
    public void applyWorkingCopy(final UUID resourceId) throws CommandFailedException {
        getCommands().applyWorkingCopy(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final UUID resourceId,
                                       final ResourceTemplatePU pu) throws CommandFailedException {
        getCommands().updateResourceTemplate(resourceId, pu.getTemplateId());
    }


    /** {@inheritDoc} */
    @Override
    public void changeRoles(final UUID resourceId, final Collection<String> roles) throws CommandFailedException {
        getCommands().changeRoles(resourceId, roles);
    }


    /** {@inheritDoc} */
    @Override
    public void move(final UUID resourceId, final UUID newParentId) throws CommandFailedException {
        getCommands().move(resourceId, newParentId);
    }


    /** {@inheritDoc} */
    @Override
    public void publish(final UUID resourceId) throws CommandFailedException {
        getCommands().publish(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void rename(final UUID resourceId, final String name) throws CommandFailedException {
        getCommands().rename(resourceId, name);
    }


    /** {@inheritDoc} */
    @Override
    public void unlock(final UUID resourceId) throws CommandFailedException {
        getCommands().unlock(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void unpublish(final UUID resourceId) throws CommandFailedException {
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
    public void excludeFromMainMenu(final UUID resourceId) throws CommandFailedException {
        getCommands().includeInMainMenu(resourceId, false);
    }


    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final UUID resourceId) throws CommandFailedException {
        getCommands().includeInMainMenu(resourceId, true);
    }


    /** {@inheritDoc} */
    @Override
    public void updateMetadata(final UUID resourceId, final Json json) throws CommandFailedException {
        final String title = json.getString("title");
        final String description = json.getString("description");
        final String tags = json.getString("tags");
        final Map<String, String> metadata = json.getStringMap("metadata");
        getCommands().updateMetadata(resourceId, title, description, tags, metadata);
    }


    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final UUID pageId) throws CommandFailedException {
        getPageCommands().clearWorkingCopy(pageId);
    }


    /** {@inheritDoc} */
    @Override
    public void cancelAction(final UUID actionId) throws CommandFailedException {
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
    public void createWorkingCopy(final UUID resourceId, final ResourceRevisionPU pu) throws CommandFailedException {
        getCommands().createWorkingCopy(resourceId, pu.getRevision());
    }


    /** {@inheritDoc} */
    @Override
    public void updateTemplate(final UUID templateId, final TemplateDelta delta) throws CommandFailedException {
        getCommands().updateTemplate(templateId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public void updateAlias(final UUID aliasId, final AliasDelta delta) throws CommandFailedException {
        getCommands().updateAlias(aliasId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public void deleteCacheDuration(final UUID id) throws CommandFailedException {
        getCommands().updateCacheDuration(id, null);
    }


    /** {@inheritDoc} */
    @Override
    public void fail() throws CommandFailedException {
        throw new CommandFailedException(new Failure(FailureCode.PRIVILEGES));
    }
}
