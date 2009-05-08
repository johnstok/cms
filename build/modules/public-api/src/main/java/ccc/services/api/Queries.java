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
package ccc.services.api;

import java.util.Collection;
import java.util.Map;


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
    Collection<ResourceSummary> roots();

    /**
     * Get the resource located at the specified path.
     *
     * @param resourceId The UUID of the existing resource.
     * @return A summary of the resource.
     */
    ResourceSummary resource(String resourceId);

    /**
     * List all the templates currently available in CCC.
     *
     * @return A list of templates.
     */
    Collection<TemplateDelta> templates();

    /**
     * List all of the folders that are children of the specified parent folder.
     *
     * @param folderId The id of the folder.
     * @return The list of child folders.
     */
    Collection<ResourceSummary> getFolderChildren(String folderId);

    /**
     * List all of the children of the specified folder.
     *
     * @param folderId The folder.
     * @return The folder's of children.
     */
    Collection<ResourceSummary> getChildren(String folderId);

    /**
     * List all content images.
     *
     * @return The list of images.
     */
    Collection<FileSummary> getAllContentImages();

    /**
     * Determine the absolute path to a resource.
     *
     * @param resourceId The id of the resource.
     * @return The absolute path as a string.
     */
    String getAbsolutePath(String resourceId);

    /**
     * Query whether given folder has a resource with given name.
     *
     * @param folderId The id of the folder to check.
     * @param name The name of the resource.
     * @return Returns true in case folder has a resource with given name,
     *  false otherwise.
     */
    boolean nameExistsInFolder(final String folderId, final String name);

    /**
     * Query all users.
     *
     * @return Returns list of users.
     */
    Collection<UserSummary> listUsers();

    /**
     * Query users with specified role.
     *
     * @param role The role as a string.
     * @return Returns list of users.
     */
    Collection<UserSummary> listUsersWithRole(String role);

    /**
     * Query users with specified username.
     *
     * @param username The username as a string.
     * @return Returns list of users.
     */
    Collection<UserSummary> listUsersWithUsername(String username);

    /**
     * Query whether the specified username is in use.
     *
     * @param username The username to check
     * @return True if the username is in use, false otherwise.
     */
    boolean usernameExists(String username);

    /**
     * Query users with specified email.
     *
     * @param email The email as a string.
     * @return Returns list of users.
     */
    Collection<UserSummary> listUsersWithEmail(String email);

    /**
     * Returns true if template name exists in the template folder.
     *
     * @param templateName The name to look up.
     * @return True if name exists.
     */
     boolean templateNameExists(final String templateName);

     /**
     * Returns TemplateDTO of the template assigned for a resource.
     *
     * @param resourceId Id of the resource.
     * @return TemplateDTO
     */
    TemplateDelta getTemplateForResource(final String resourceId);

    /**
     * Returns currently logged in user.
     *
     * @return UserDTO
     */
    UserSummary loggedInUser();

    /**
     * List the resources locked by the currently logged in user.
     *
     * @return The list of resources.
     */
    Collection<ResourceSummary> lockedByCurrentUser();

    /**
     * List all locked resources.
     *
     * @return The list of resources.
     */
    Collection<ResourceSummary> locked();

    /**
     * Retrieve the history of a resource.
     *
     * @param resourceId The id of the resource whose history we will look up.
     * @return The list of resources.
     */
    Collection<LogEntrySummary> history(String resourceId);

    /**
     * Retrieve the metadata for a resource.
     *
     * @param resourceId The id of the resource.
     * @return The metadata in a hashmap.
     */
    Map<String, String> metadata(String resourceId);

    /**
     * Merge page and its working copy to a page delta.
     *
     * @param pageId The id of the resource.
     * @return Page delta of the merge of page and working copy.
     */
    PageDelta workingCopyDelta(String pageId);

    /**
     * List all CCC actions that haven't yet been executed.
     *
     * @return A collection of action summaries, one per outstanding action.
     */
    Collection<ActionSummary> listPendingActions();

    /**
     * List all CCC actions that have been executed.
     *
     * @return A collection of action summaries, one per completed action.
     */
    Collection<ActionSummary> listCompletedActions();

    Collection<String> roles(String resourceId);

    /**
     * Retrieve resource's cache duration.
     *
     * @param resourceId The id of the resource.
     * @return Duration.
     */
    Duration cacheDuration(String resourceId);

    TemplateDelta templateDelta(String templateId);
    UserDelta userDelta(String userId);
    AliasDelta aliasDelta(String aliasId);
    PageDelta pageDelta(String pageId);
    ResourceDelta folderDelta(String folderId);
    ResourceDelta resourceDelta(String resourceId);
    FileDelta fileDelta(String fileId);

}
