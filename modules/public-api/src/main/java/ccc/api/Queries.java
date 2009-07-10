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
package ccc.api;

import java.util.Collection;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jboss.resteasy.annotations.cache.NoCache;


/**
 * Query methods available for CCC.
 *
 * @author Civic Computing Ltd.
 */
public interface Queries {

    /** NAME : String. */
    String NAME = "PublicQueries";

    /**
     * List the root folders available.
     *
     * @return A collection of resource summaries - one for each root folder.
     */
    @GET @Path("/roots") @NoCache
    Collection<ResourceSummary> roots();

    /**
     * Get the resource located at the specified path.
     *
     * @param resourceId The id of the existing resource.
     * @return A summary of the resource.
     */
    @GET @Path("/resources/{id}") @NoCache
    ResourceSummary resource(@PathParam("id") ID resourceId);

    /**
     * List all the templates currently available in CCC.
     *
     * @return A list of templates.
     */
    @GET @Path("/templates") @NoCache
    Collection<TemplateSummary> templates();

    /**
     * List all of the folders that are children of the specified parent folder.
     *
     * @param folderId The id of the folder.
     * @return The list of child folders.
     */
    @GET @Path("/folders/{id}/folder-children") @NoCache
    Collection<ResourceSummary> getFolderChildren(@PathParam("id") ID folderId);

    /**
     * List all of the children of the specified folder.
     *
     * @param folderId The folder.
     * @return The folder's of children.
     */
    @GET @Path("/folders/{id}/children") @NoCache
    Collection<ResourceSummary> getChildren(@PathParam("id") ID folderId);

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
    String getAbsolutePath(@PathParam("id") ID resourceId);

    /**
     * Query whether given folder has a resource with given name.
     *
     * @param folderId The id of the folder to check.
     * @param name The name of the resource.
     * @return Returns true in case folder has a resource with given name,
     *  false otherwise.
     */
    @GET @Path("/folders/{id}/{name}/exists") @NoCache
    boolean nameExistsInFolder(@PathParam("id") final ID folderId,
                               @PathParam("name") final String name);

    /**
     * Query all users.
     *
     * @return Returns list of users.
     */
    @GET @Path("/users") @NoCache
    Collection<UserSummary> listUsers();

    /**
     * Query users with specified role.
     *
     * @param role The role as a string.
     * @return Returns list of users.
     */
    @GET @Path("/users/role/{role}") @NoCache
    Collection<UserSummary> listUsersWithRole(@PathParam("role") String role);

    /**
     * Query users with specified username.
     *
     * @param username The username as a string.
     * @return Returns list of users.
     */
    @GET @Path("/users/username/{uname}") @NoCache
    Collection<UserSummary> listUsersWithUsername(@PathParam("uname") String username);

    /**
     * Query whether the specified username is in use.
     *
     * @param username The username to check
     * @return True if the username is in use, false otherwise.
     */
    @GET @Path("/users/{uname}/exists") @NoCache
    boolean usernameExists(@PathParam("uname") Username username);

    /**
     * Query users with specified email.
     *
     * @param email The email as a string.
     * @return Returns list of users.
     */
    @GET @Path("/users/email/{email}") @NoCache
    Collection<UserSummary> listUsersWithEmail(@PathParam("email") String email);

    /**
     * Returns true if template name exists in the template folder.
     *
     * @param templateName The name to look up.
     * @return True if name exists.
     */
    @GET @Path("/templates/{name}/exists") @NoCache
    boolean templateNameExists(@PathParam("name") final String templateName);

    /**
     * Returns currently logged in user.
     *
     * @return UserDTO
     */
    @GET @Path("/users/me") @NoCache
    UserSummary loggedInUser();

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
    Collection<LogEntrySummary> history(@PathParam("id") ID resourceId);

    /**
     * Retrieve the metadata for a resource.
     *
     * @param resourceId The id of the resource.
     * @return The metadata in a hashmap.
     */
    @GET @Path("/resources/{id}/metadata") @NoCache
    Map<String, String> metadata(@PathParam("id") ID resourceId);

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
    Collection<String> roles(@PathParam("id") ID resourceId);

    /**
     * Retrieve resource's cache duration.
     *
     * @param resourceId The id of the resource.
     * @return Duration.
     */
    @GET @Path("/resources/{id}/duration") @NoCache
    Duration cacheDuration(@PathParam("id") ID resourceId);

    /**
     * Returns summary of the template assigned for a resource.
     *
     * @param resourceId Id of the resource.
     * @return TemplateSummary.
     */
    @GET @Path("/resources/{id}/template") @NoCache
    TemplateSummary computeTemplate(@PathParam("id") ID resourceId);

    /**
     * Retrieve the delta for a template.
     *
     * @param templateId The template's id.
     * @return The corresponding delta.
     */
    @GET @Path("/templates/{id}/delta") @NoCache
    TemplateDelta templateDelta(@PathParam("id") ID templateId);

    /**
     * Retrieve the delta for a user.
     *
     * @param userId The user's id.
     * @return The corresponding delta.
     */
    @GET @Path("/users/{id}/delta") @NoCache
    UserDelta userDelta(@PathParam("id") ID userId);

    /**
     * Retrieve the delta for a alias.
     *
     * @param aliasId The alias' id.
     * @return The corresponding delta.
     */
    @GET @Path("/aliases/{id}/delta") @NoCache
    AliasDelta aliasDelta(@PathParam("id") ID aliasId);

    /**
     * Retrieve the delta for a page.
     *
     * @param pageId The page's id.
     * @return The corresponding delta.
     */
    @GET @Path("/pages/{id}/delta") @NoCache
    PageDelta pageDelta(@PathParam("id") ID pageId);

    /**
     * Retrieve the delta for a file.
     *
     * @param fileId The file's id.
     * @return The corresponding delta.
     */
    @GET @Path("/files/{id}/delta") @NoCache
    FileDelta fileDelta(@PathParam("id") ID fileId);

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
