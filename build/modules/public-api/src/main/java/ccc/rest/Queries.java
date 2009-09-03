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
package ccc.rest;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.dto.ActionSummary;
import ccc.rest.dto.FileDelta;
import ccc.rest.dto.FileSummary;
import ccc.rest.dto.LogEntrySummary;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateSummary;
import ccc.types.Duration;


/**
 * Query methods available for CCC.
 *
 * @author Civic Computing Ltd.
 */
public interface Queries {

    /** NAME : String. */
    String NAME = "PublicQueries";


    /**
     * Get the resource located at the specified path.
     *
     * @param resourceId The id of the existing resource.
     * @return A summary of the resource.
     */
    @GET @Path("/resources/{id}") @NoCache
    ResourceSummary resource(@PathParam("id") UUID resourceId);

    /**
     * List all the templates currently available in CCC.
     *
     * @return A list of templates.
     */
    @GET @Path("/templates") @NoCache
    Collection<TemplateSummary> templates();

    /**
     * List all content images.
     *
     * @return The list of images.
     */
    @GET @Path("/images") @NoCache
    Collection<FileSummary> getAllContentImages();

    /**
     * Determine the absolute path to a resource.
     *
     * @param resourceId The id of the resource.
     * @return The absolute path as a string.
     */
    @GET @Path("/resources/{id}/path") @NoCache
    String getAbsolutePath(@PathParam("id") UUID resourceId);

    /**
     * Returns true if template name exists in the template folder.
     *
     * @param templateName The name to look up.
     * @return True if name exists.
     */
    @GET @Path("/templates/{name}/exists") @NoCache
    boolean templateNameExists(@PathParam("name") final String templateName);

    /**
     * List the resources locked by the currently logged in user.
     *
     * @return The list of resources.
     */
    @GET @Path("/resources/locked/me") @NoCache
    Collection<ResourceSummary> lockedByCurrentUser();

    /**
     * List all locked resources.
     *
     * @return The list of resources.
     */
    @GET @Path("/resources/locked") @NoCache
    Collection<ResourceSummary> locked();

    /**
     * Retrieve the history of a resource.
     *
     * @param resourceId The id of the resource whose history we will look up.
     * @return The list of resources.
     */
    @GET @Path("/resources/{id}/revisions") @NoCache
    Collection<LogEntrySummary> history(@PathParam("id") UUID resourceId);

    /**
     * Retrieve the metadata for a resource.
     *
     * @param resourceId The id of the resource.
     * @return The metadata in a hashmap.
     */
    @GET @Path("/resources/{id}/metadata") @NoCache
    Map<String, String> metadata(@PathParam("id") UUID resourceId);

    /**
     * List all CCC actions that haven't yet been executed.
     *
     * @return A collection of action summaries, one per outstanding action.
     */
    @GET @Path("/actions/pending") @NoCache
    Collection<ActionSummary> listPendingActions();

    /**
     * List all CCC actions that have been executed.
     *
     * @return A collection of action summaries, one per completed action.
     */
    @GET @Path("/actions/completed") @NoCache
    Collection<ActionSummary> listCompletedActions();

    /**
     * List the roles for a resource.
     *
     * @param resourceId The resource's id.
     *
     * @return The roles, as a collection of strings.
     */
    @GET @Path("/resources/{id}/roles") @NoCache
    Collection<String> roles(@PathParam("id") UUID resourceId);

    /**
     * Retrieve resource's cache duration.
     *
     * @param resourceId The id of the resource.
     * @return Duration.
     */
    @GET @Path("/resources/{id}/duration") @NoCache
    Duration cacheDuration(@PathParam("id") UUID resourceId);

    /**
     * Returns summary of the template assigned for a resource.
     *
     * @param resourceId Id of the resource.
     * @return TemplateSummary.
     */
    @GET @Path("/resources/{id}/template") @NoCache
    TemplateSummary computeTemplate(@PathParam("id") UUID resourceId);

    /**
     * Retrieve the delta for a template.
     *
     * @param templateId The template's id.
     * @return The corresponding delta.
     */
    @GET @Path("/templates/{id}/delta") @NoCache
    TemplateDelta templateDelta(@PathParam("id") UUID templateId);

    /**
     * Retrieve the target name for a alias.
     *
     * @param aliasId The alias' id.
     * @return The corresponding target name.
     */
    @GET @Path("/aliases/{id}/targetname") @NoCache
    String aliasTargetName(@PathParam("id") UUID aliasId);

    /**
     * Retrieve the delta for a file.
     *
     * @param fileId The file's id.
     * @return The corresponding delta.
     */
    @GET @Path("/files/{id}/delta") @NoCache
    FileDelta fileDelta(@PathParam("id") UUID fileId);

    /**
     * Look up the resource for a specified path.
     *
     * @param path The absolute path.
     * @return A summary of the corresponding resource.
     */
    @GET @Path("/resources-by-path/{path:.*}") @NoCache
    ResourceSummary resourceForPath(@PathParam("path") String path);

    /**
     * Look up the resource for a specified legacy id.
     *
     * @param legacyId The legacy id of the resource.
     * @return A summary of the corresponding resource.
     */
    @GET @Path("/resources-by-legacy-id/{id}") @NoCache
    ResourceSummary resourceForLegacyId(@PathParam("id") String legacyId);

}
