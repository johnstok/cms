/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ccc.rest.dto.ActionNew;
import ccc.rest.dto.AliasDelta;
import ccc.rest.dto.AliasNew;
import ccc.rest.dto.ResourceCacheDurationPU;
import ccc.rest.dto.ResourceRevisionPU;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.ResourceTemplatePU;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateNew;
import ccc.serialization.Json;
import ccc.types.ID;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
@Consumes("application/json")
@Produces("application/json")
public interface RestCommands {

    @POST @Path("/resources/{id}/duration")
    void updateCacheDuration(
        @PathParam("id") ID resourceId,
        ResourceCacheDurationPU duration) throws CommandFailedException;

    @POST @Path("/resources/{id}/lock")
    void lock(
        @PathParam("id") ID resourceId) throws CommandFailedException;

    @POST @Path("/templates")
    ResourceSummary createTemplate(TemplateNew template) throws CommandFailedException;

    @POST @Path("/resources/{id}/wc-apply")
    void applyWorkingCopy(
        @PathParam("id") ID resourceId) throws CommandFailedException;

    @POST @Path("/resources/{id}/template")
    void updateResourceTemplate(
        @PathParam("id") ID resourceId,
        ResourceTemplatePU template) throws CommandFailedException;

    @POST @Path("/resources/{id}/unlock")
    void unlock(
        @PathParam("id") ID resourceId) throws CommandFailedException;

    @POST @Path("/resources/{id}/unpublish")
    void unpublish(
        @PathParam("id") ID resourceId) throws CommandFailedException;

    @POST @Path("/resources/{id}/publish")
    void publish(
        @PathParam("id") ID resourceId) throws CommandFailedException;

    @POST @Path("/resources/{id}/parent")
    void move(
        @PathParam("id") ID resourceId,
        ID newParentId) throws CommandFailedException;

    @POST @Path("/resources/{id}/name")
    void rename(
        @PathParam("id") final ID resourceId,
        final String name) throws CommandFailedException;

    @POST @Path("/resources/{id}/roles")
    void changeRoles(
        @PathParam("id") ID resourceId,
        Collection<String> roles) throws CommandFailedException;

    @POST @Path("/resources/{id}/exclude-mm")
    void excludeFromMainMenu(
        @PathParam("id") ID resourceId) throws CommandFailedException;

    @POST @Path("/resources/{id}/include-mm")
    void includeInMainMenu(
        @PathParam("id") ID resourceId) throws CommandFailedException;

    @POST @Path("/actions")
    void createAction(ActionNew action) throws CommandFailedException;

    @POST @Path("/resources/{id}/metadata")
    void updateMetadata(
        @PathParam("id") ID resourceId,
        Json json) throws CommandFailedException;

    @POST @Path("/resources/{id}/wc-clear")
    void clearWorkingCopy(
        @PathParam("id") ID pageId) throws CommandFailedException;

    @POST @Path("/actions/{id}/cancel")
    void cancelAction(
        @PathParam("id") ID actionId) throws CommandFailedException;

    @POST @Path("/resources/{id}/wc-create")
    void createWorkingCopy(
        @PathParam("id") ID resourceId,
        ResourceRevisionPU pu) throws CommandFailedException;

    @POST @Path("/aliases")
    ResourceSummary createAlias(AliasNew alias) throws CommandFailedException;

    @POST @Path("/templates/{id}")
    void updateTemplate(
        @PathParam("id") ID templateId,
        TemplateDelta delta) throws CommandFailedException;

    @POST @Path("/aliases/{id}")
    void updateAlias(
        @PathParam("id") ID aliasId,
        AliasDelta delta) throws CommandFailedException;

    @DELETE  @Path("/resources/{id}/duration")
    void deleteCacheDuration(
        @PathParam("id") ID id) throws CommandFailedException;

    @GET @Path("/fail")
    void fail() throws CommandFailedException;
}
