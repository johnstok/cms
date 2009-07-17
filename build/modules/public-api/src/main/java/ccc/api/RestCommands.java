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
package ccc.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;


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
        Duration duration) throws CommandFailedException;

    @POST @Path("/resources/{id}/lock")
    void lock(
        @PathParam("id") ID resourceId) throws CommandFailedException;

    @POST @Path("/users")
    UserSummary createUser(
        UserDelta delta,
        @QueryParam("pw") String password) throws CommandFailedException;

    @POST @Path("/templates")
    ResourceSummary createTemplate(
        @QueryParam("id") ID parentId,
        TemplateDelta delta,
        @QueryParam("t") String title,
        @QueryParam("d") String description,
        @QueryParam("n") String name) throws CommandFailedException;

    @POST @Path("/pages")
    ResourceSummary createPage(
        @QueryParam("id") ID parentId,
        PageDelta delta,
        @QueryParam("n") String name,
        @QueryParam("p") boolean publish,
        @QueryParam("m") ID templateId,
        @QueryParam("t") String title) throws CommandFailedException;

    @POST @Path("/folders")
    ResourceSummary createFolder(
        @QueryParam("id") ID parentId,
        @QueryParam("n") String name) throws CommandFailedException;

    @POST @Path("/users/{id}/password")
    void updateUserPassword(
        @PathParam("id") ID userId,
        String password) throws CommandFailedException;

    @POST @Path("/resources/{id}/wc-apply")
    void applyWorkingCopy(
        @PathParam("id") ID resourceId) throws CommandFailedException;

    @POST @Path("/resources/{id}/template")
    void updateResourceTemplate(
        @PathParam("id") ID resourceId,
        @QueryParam("t") ID templateId) throws CommandFailedException;

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
    void createAction(
        @QueryParam("r") ID resourceId,
        @QueryParam("c") CommandType action,
        @QueryParam("x") long executeAfter,
        Map<String, String> parameters) throws CommandFailedException;

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
        @QueryParam("v") long index) throws CommandFailedException;

    @POST @Path("/aliases")
    ResourceSummary createAlias(
        @QueryParam("id") ID parentId,
        @QueryParam("n") String name,
        @QueryParam("g") ID targetId) throws CommandFailedException;

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
        @QueryParam("s") String sortOrder,
        @QueryParam("i") ID indexPageId) throws CommandFailedException;

    @POST @Path("/users/{id}")
    void updateUser(
        @PathParam("id") ID userId,
        UserDelta delta) throws CommandFailedException;

    @POST @Path("/folders/{id}/order")
    void reorder(
        @PathParam("id") ID folderId,
        List<String> order) throws CommandFailedException;

    @DELETE  @Path("/resources/{id}/duration")
    void deleteCacheDuration(
        @PathParam("id") ID id) throws CommandFailedException;

    @GET @Path("/fail")
    void fail() throws CommandFailedException;

    @POST @Path("/sessions")
    public Boolean login(
          @QueryParam("u") final String username,
          @QueryParam("p") final String password);

    @GET @Path("/sessions/current")
    public Boolean isLoggedIn();

    @GET @Path("/sessions/properties")
    public String readProperty(
          @QueryParam("key") final String key);

    @POST @Path("/sessions/current")
    public void logout();
}
