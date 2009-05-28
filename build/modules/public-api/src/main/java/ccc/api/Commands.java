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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Command API, used to update data in CCC.
 *
 * @author Civic Computing Ltd.
 */
public interface Commands {

    /** NAME : String. */
    String NAME = "PublicCommands";


    /**
     * Update the specified page on the server.
     *
     * @param pageId The id of the page to update.
     * @param delta The changes to apply.
     * @param comment A comment describing the changes.
     * @param isMajorEdit Is this a major change.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updatePage(ID pageId,
                    PageDelta delta,
                    String comment,
                    boolean isMajorEdit) throws CommandFailedException;

    /**
     * Update the working copy of the specified page.
     *
     * @param pageId The id of the page to update.
     * @param delta The changes to apply.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateWorkingCopy(ID pageId, PageDelta delta)
    throws CommandFailedException;

    /**
     * Update an alias.
     *
     * @param aliasId The id of the alias to update.
     * @param delta The changes to apply.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateAlias(ID aliasId, AliasDelta delta)
    throws CommandFailedException;

    /**
     * Updates the user in the system.
     *
     * @param userId The id of the user to update.
     * @param delta The changes to apply.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateUser(ID userId, UserDelta delta) throws CommandFailedException;

    /**
     * Update the specified template on the server.
     *
     * @param templateId The id of the template to update.
     * @param delta The changes to apply.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateTemplate(ID templateId, TemplateDelta delta)
    throws CommandFailedException;

    /**
     * Rename resource.
     *
     * @param resourceId The id of the resource to rename.
     * @param name The new name.
     *
     * @throws CommandFailedException If the method fails.
     */
    void rename(final ID resourceId, final String name)
    throws CommandFailedException;

    /**
     * Update the tags for a resource.
     *
     * @param resourceId The id of the resource to update.
     * @param tags The new tags to set.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateTags(ID resourceId, String tags) throws CommandFailedException;

    /**
     * Changes a resource's parent.
     *
     * @param resourceId The id of the resource to move.
     * @param newParentId The id of the folder to which the resource should be
     *  moved.
     *
     * @throws CommandFailedException If the method fails.
     */
    void move(ID resourceId, ID newParentId)
    throws CommandFailedException;

    /**
     * Update the specified resource's template on the server.
     *
     * @param resourceId The id of the resource to update.
     * @param templateId The new template to set for the resource.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateResourceTemplate(ID resourceId, ID templateId)
    throws CommandFailedException;

    /**
     * Lock the specified resource.
     * The resource will be locked by the currently logged in user.
     * If the resource is already locked a CCCException will be thrown.
     *
     * @param resourceId The uuid of the resource to lock.
     *
     * @throws CommandFailedException If the method fails.
     */
    void lock(ID resourceId) throws CommandFailedException;

    /**
     * Unlock the specified Resource.
     * If the logged in user does not have privileges to unlock this resource a
     * CCCException will be thrown.
     * Unlocking an unlocked resource has no effect.
     *
     * @param resourceId The resource to unlock.
     *
     * @throws CommandFailedException If the method fails.
     */
    void unlock(ID resourceId) throws CommandFailedException;

    /**
     * Publish the specified resource.
     *
     * @param resourceId The id of the resource to update.
     *
     * @throws CommandFailedException If the method fails.
     */
    void publish(ID resourceId) throws CommandFailedException;

    /**
     * Publish the specified resource.
     *
     * @param resourceId The id of the resource to update.
     * @param userId The id of the publishing user.
     * @param publishDate The date the resource was published.
     *
     * @throws CommandFailedException If the method fails.
     */
    void publish(ID resourceId, ID userId, Date publishDate)
    throws CommandFailedException;

    /**
     * Unpublish the specified resource.
     *
     * @param resourceId The id of the resource to update.
     *
     * @throws CommandFailedException If the method fails.
     */
    void unpublish(ID resourceId) throws CommandFailedException;

    /**
     * Unpublish the specified resource.
     *
     * @param resourceId The id of the resource to update.
     * @param userId The id of the un-publishing user.
     * @param publishDate The date the resource was un-published.
     *
     * @throws CommandFailedException If the method fails.
     */
    void unpublish(ID resourceId, ID userId, Date publishDate)
                                                  throws CommandFailedException;

    /**
     * Specify whether a resource should be included in a site's main menu.
     *
     * @param resourceId The id of the resource to update.
     * @param include True if the resource should be included, false otherwise.
     *
     * @throws CommandFailedException If the method fails.
     */
    void includeInMainMenu(ID resourceId, boolean include)
    throws CommandFailedException;

    /**
     * Update metadata of the resource.
     *
     * @param resourceId The id of the resource to update.
     * @param metadata The metadata to update.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateMetadata(ID resourceId, Map<String, String> metadata)
    throws CommandFailedException;

    /**
     * Update the sort order for the specified folder.
     *
     * @param folderId The id of the folder to update.
     * @param sortOrder The new sort order.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateFolderSortOrder(ID folderId, String sortOrder)
    throws CommandFailedException;

    /**
     * Validate a set of paragraphs against a given definition.
     *
     * @param delta The paragraphs.
     * @param definition The xml definition, as a string.
     * @return A list of errors, as strings.
     */
    List<String> validateFields(final Set<Paragraph> delta,
                                final String definition);

    /**
     * Delete the working copy for a page.
     *
     * @param pageId The id of the page with a working copy.
     *
     * @throws CommandFailedException If the method fails.
     */
    void clearWorkingCopy(ID pageId) throws CommandFailedException;

    /**
     * Create a new alias in CCC.
     *
     * @param parentId The folder in which the alias should be created.
     * @param name The name of the alias.
     * @param targetId The target resource to which the alias should link.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A resource summary describing the new alias.
     */
    ResourceSummary createAlias(ID parentId, String name, ID targetId)
    throws CommandFailedException;

    /**
     * Create a folder with the specified name.
     *
     * @param parentId The folder in which the new folder should be created.
     * @param name The name of the new folder.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A resource summary describing the new folder.
     */
    ResourceSummary createFolder(ID parentId, String name)
    throws CommandFailedException;

    /**
     * Create a folder with the specified name and title.
     *
     * @param parentId The folder in which the new folder should be created.
     * @param name The name of the new folder.
     * @param title The title of the folder.
     * @param publish True if the title should be published, false otherwise.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A resource summary describing the new folder.
     */
    ResourceSummary createFolder(ID parentId,
                                 String name,
                                 String title,
                                 boolean publish) throws CommandFailedException;

    /**
     * Create a root folder with the specified name.
     *
     * @param name The name of the root folder.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A resource summary describing the new root.
     */
    ResourceSummary createRoot(String name)
    throws CommandFailedException;

    /**
     * Create a new user in the system.
     *
     * @param delta The new user details.
     * @param password The new user's password.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A user summary describing the new user.
     */
    UserSummary createUser(UserDelta delta, String password)
    throws CommandFailedException;

    /**
     * Creates a new page.
     *
     * @param parentId The folder in which the page will be created.
     * @param delta The page's details.
     * @param name The page's name.
     * @param publish True if the folder should be published, false otherwise.
     * @param templateId The page's template.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A resource summary describing the new page.
     */
    ResourceSummary createPage(ID parentId,
                               PageDelta delta,
                               String name,
                               final boolean publish,
                               ID templateId) throws CommandFailedException;

    /**
     * Create a new template in CCC.
     *
     * @param parentId The folder in which the template should be created.
     * @param delta The template's details.
     * @param name The template's name.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A resource summary describing the new template.
     */
    ResourceSummary createTemplate(ID parentId,
                                   TemplateDelta delta,
                                   String name) throws CommandFailedException;

    /**
     * Creates a new search.
     *
     * @param parentId The parent folder where the search should be created.
     * @param title The title of the search.
     *
     * @return A summary of the newly created search.
     *
     * @throws CommandFailedException If the method fails.
     */
    ResourceSummary createSearch(ID parentId, String title)
    throws CommandFailedException;

    /**
     * Create a working copy for the specified resource, using the specified log
     * entry.
     *
     * @param resourceId The id of the resource.
     * @param index The index number of the log entry.
     *
     * @throws CommandFailedException If the method fails.
     */
    void createWorkingCopy(ID resourceId, long index)
    throws CommandFailedException;

    /**
     * Cancel a scheduled action.
     *
     * @param actionId The id of the action to cancel.
     *
     * @throws CommandFailedException If the method fails.
     */
    void cancelAction(ID actionId) throws CommandFailedException;

    /**
     * Create a new scheduled action.
     *
     * @param resourceId The id of the resource the action will operate on.
     * @param action The type of the action to be performed.
     * @param executeAfter The earliest date at which the action may be
     *  executed.
     * @param parameters Additional parameters for the action, as a JSON string.
     * @param comment The comment for the action.
     * @param isMajorEdit Is the action a major change.
     *
     * @throws CommandFailedException If the method fails.
     */
    void createAction(ID resourceId,
                      CommandType action,
                      Date executeAfter,
                      String parameters,
                      String comment,
                      boolean isMajorEdit) throws CommandFailedException;

    /**
     * Change the order of resources in a folder.
     *
     * @param folderId The id of the folder to update.
     * @param order The new order of the resources.
     *
     * @throws CommandFailedException If the method fails.
     */
    void reorder(ID folderId, List<String> order) throws CommandFailedException;

    /**
     * Change the security roles for a resource.
     *
     * @param resourceId The resource to update.
     * @param roles The new set of roles.
     *
     * @throws CommandFailedException If the method fails.
     */
    void changeRoles(ID resourceId, Collection<String> roles)
    throws CommandFailedException;

    /**
     * Apply a resource's working copy.
     *
     * @param resourceId The id of the resource.
     *
     * @throws CommandFailedException If the method fails.
     */
    void applyWorkingCopy(ID resourceId) throws CommandFailedException;

    /**
     * Apply a resource's working copy.
     *
     * @param resourceId The id of the resource.
     * @param userId The user applying the working copy.
     * @param happenedOn When the command happened.
     * @param isMajorEdit Was this a major change.
     * @param comment A comment describing the change.
     *
     * @throws CommandFailedException If the method fails.
     */
    void applyWorkingCopy(ID resourceId,
                          ID userId,
                          Date happenedOn,
                          boolean isMajorEdit,
                          String comment) throws CommandFailedException;

    /**
     * Update the period that a resource should be cached for.
     *
     * @param resourceId The resource to update.
     * @param duration The cache duration.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateCacheDuration(ID resourceId, Duration duration)
    throws CommandFailedException;

    /**
     * Update the password for the specified user.
     *
     * @param userId The user's id.
     * @param password The new password to set.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateUserPassword(ID userId, String password)
    throws CommandFailedException;
}

