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
import java.util.List;

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
import ccc.rest.dto.FolderDelta;
import ccc.rest.dto.FolderNew;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.PageNew;
import ccc.rest.dto.ResourceCacheDurationPU;
import ccc.rest.dto.ResourceRevisionPU;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.ResourceTemplatePU;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateNew;
import ccc.rest.dto.UserSummary;
import ccc.serialization.Json;
import ccc.types.ID;


/**
 * TODO: Add a description for this type.
 * FIXME: Move to api-rest module.
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

    @POST @Path("/users")
    UserSummary createUser(UserSummary user) throws CommandFailedException;

    @POST @Path("/templates")
    ResourceSummary createTemplate(TemplateNew template) throws CommandFailedException;

    @POST @Path("/pages")
    ResourceSummary createPage(PageNew page) throws CommandFailedException;

    @POST @Path("/folders")
    ResourceSummary createFolder(FolderNew folder) throws CommandFailedException;

    @POST @Path("/users/{id}/password")
    void updateUserPassword(
        @PathParam("id") ID userId,
        UserSummary pu) throws CommandFailedException;

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

    @POST @Path("/page-validator")
    List<String> validateFields(Json json);

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

    @POST @Path("/pages/{id}/wc")
    void updateWorkingCopy(
        @PathParam("id") ID pageId,
        PageDelta delta) throws CommandFailedException;

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

    @POST @Path("/pages/{id}")
    void updatePage(
        @PathParam("id") ID pageId,
        Json json) throws CommandFailedException;

    @POST @Path("/folders/{id}")
    void updateFolder(
        @PathParam("id") ID folderId,
        FolderDelta delta) throws CommandFailedException;

    @POST @Path("/users/{id}")
    void updateUser(
        @PathParam("id") ID userId,
        UserSummary delta) throws CommandFailedException;

    @DELETE  @Path("/resources/{id}/duration")
    void deleteCacheDuration(
        @PathParam("id") ID id) throws CommandFailedException;

    @GET @Path("/fail")
    void fail() throws CommandFailedException;


    @POST @Path("/users/{id}/currentuser")
    void updateYourUser(
        @PathParam("id") ID userId,
        UserSummary user) throws CommandFailedException;
}
