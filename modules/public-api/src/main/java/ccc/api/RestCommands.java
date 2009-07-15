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

import javax.ws.rs.Consumes;
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
}
