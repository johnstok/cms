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
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ccc.rest.dto.AliasDelta;
import ccc.rest.dto.AliasDto;
import ccc.rest.dto.ResourceDto;
import ccc.rest.dto.ResourceSummary;
import ccc.serialization.Json;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
@Consumes("application/json")
@Produces("application/json")
public interface ResourcesBasic {

    @POST @Path("/resources/{id}/duration")
    void updateCacheDuration(
        @PathParam("id") UUID resourceId,
        ResourceDto duration) throws CommandFailedException;

    @POST @Path("/resources/{id}/lock")
    void lock(
        @PathParam("id") UUID resourceId) throws CommandFailedException;

    @POST @Path("/resources/{id}/wc-apply")
    void applyWorkingCopy(
        @PathParam("id") UUID resourceId) throws CommandFailedException;

    @POST @Path("/resources/{id}/template")
    void updateResourceTemplate(
        @PathParam("id") UUID resourceId,
        ResourceDto template) throws CommandFailedException;

    @POST @Path("/resources/{id}/unlock")
    void unlock(
        @PathParam("id") UUID resourceId) throws CommandFailedException;

    @POST @Path("/resources/{id}/unpublish")
    void unpublish(
        @PathParam("id") UUID resourceId) throws CommandFailedException;

    @POST @Path("/resources/{id}/publish")
    void publish(
        @PathParam("id") UUID resourceId) throws CommandFailedException;

    @POST @Path("/resources/{id}/parent")
    void move(
        @PathParam("id") UUID resourceId,
        UUID newParentId) throws CommandFailedException;

    @POST @Path("/resources/{id}/name")
    void rename(
        @PathParam("id") final UUID resourceId,
        final String name) throws CommandFailedException;

    @POST @Path("/resources/{id}/roles")
    void changeRoles(
        @PathParam("id") UUID resourceId,
        Collection<String> roles) throws CommandFailedException;

    @POST @Path("/resources/{id}/exclude-mm")
    void excludeFromMainMenu(
        @PathParam("id") UUID resourceId) throws CommandFailedException;

    @POST @Path("/resources/{id}/include-mm")
    void includeInMainMenu(
        @PathParam("id") UUID resourceId) throws CommandFailedException;

    @POST @Path("/resources/{id}/metadata")
    void updateMetadata(
        @PathParam("id") UUID resourceId,
        Json json) throws CommandFailedException;

    @POST @Path("/resources/{id}/wc-clear")
    void clearWorkingCopy(
        @PathParam("id") UUID pageId) throws CommandFailedException;

    @POST @Path("/resources/{id}/wc-create")
    void createWorkingCopy(
        @PathParam("id") UUID resourceId,
        ResourceDto pu) throws CommandFailedException;

    @POST @Path("/aliases")
    ResourceSummary createAlias(AliasDto alias) throws CommandFailedException;

    @POST @Path("/aliases/{id}")
    void updateAlias(
        @PathParam("id") UUID aliasId,
        AliasDelta delta) throws CommandFailedException;

    @DELETE  @Path("/resources/{id}/duration")
    void deleteCacheDuration(
        @PathParam("id") UUID id) throws CommandFailedException;

    @GET @Path("/fail")
    void fail() throws CommandFailedException;
}
