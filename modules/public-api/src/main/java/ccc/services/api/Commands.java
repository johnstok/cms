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
import java.util.Date;
import java.util.List;
import java.util.Map;





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
     */
    void updatePage(ID pageId,
                    PageDelta delta,
                    String comment,
                    boolean isMajorEdit) throws CCCRemoteException;

    /**
     * Update the working copy of the specified page.
     *
     * @param pageId The id of the page to update.
     * @param delta The changes to apply.
     */
    void updateWorkingCopy(ID pageId, PageDelta delta) throws CCCRemoteException;

    /**
     * Update an alias.
     *
     * @param aliasId The id of the alias to update.
     * @param delta The changes to apply.
     */
    void updateAlias(ID aliasId, AliasDelta delta) throws CCCRemoteException;

    /**
     * Updates the user in the system.
     *
     * @param userId The id of the user to update.
     * @param delta The changes to apply.
     */
    void updateUser(ID userId, UserDelta delta) throws CCCRemoteException;

    /**
     * Update the specified template on the server.
     *
     * @param templateId The id of the template to update.
     * @param delta The changes to apply.
     */
    void updateTemplate(ID templateId, TemplateDelta delta) throws CCCRemoteException;

    /**
     * Rename resource.
     */
    void rename(final ID resourceId, final String name) throws CCCRemoteException;

    /**
     * Update the tags for a resource.
     */
    void updateTags(ID resourceId, String tags) throws CCCRemoteException;

    /**
     * Changes a resource's parent.
     */
    void move(ID resourceId, ID newParentId) throws CCCRemoteException;

    /**
     * Update the specified resource's template on the server.
     */
    void updateResourceTemplate(ID resourceId, ID templateId) throws CCCRemoteException;

    /**
     * Lock the specified resource.
     * The resource will be locked by the currently logged in user.
     * If the resource is already locked a CCCException will be thrown.
     *
     * @param resourceId The uuid of the resource to lock.
     */
    void lock(ID resourceId) throws CCCRemoteException;

    /**
     * Unlock the specified Resource.
     * If the logged in user does not have privileges to unlock this resource a
     * CCCException will be thrown.
     * Unlocking an unlocked resource has no effect.
     *
     * @param resourceId The resource to unlock.
     */
    void unlock(ID resourceId) throws CCCRemoteException;

    /**
     * Publish the specified resource.
     *
     * @param resourceId The id of the resource to update.
     */
    void publish(ID resourceId) throws CCCRemoteException;

    /**
     * TODO: Add a description of this method.
     *
     * @param resourceId The id of the resource to update.
     * @param userId The id of the publishing user.
     * @param publishDate The date the resource was published.
     */
    void publish(ID resourceId, ID userId, Date publishDate) throws CCCRemoteException;

    /**
     * Unpublish the specified resource.
     *
     * @param resourceId The id of the resource to update.
     */
    void unpublish(ID resourceId) throws CCCRemoteException;

    /**
     * TODO: Add a description of this method.
     *
     * @param resourceId The id of the resource to update.
     * @param include
     */
    void includeInMainMenu(ID resourceId, boolean include) throws CCCRemoteException;

    /**
     * Update metadata of the resource.
     *
     * @param resourceId The id of the resource to update.
     * @param metadata The metadata to update.
     */
    void updateMetadata(ID resourceId, Map<String, String> metadata) throws CCCRemoteException;

    /**
     * Update the sort order for the specified folder.
     *
     * @param folderId The id of the folder to update.
     * @param sortOrder The new sort order.
     */
    void updateFolderSortOrder(ID folderId, String sortOrder) throws CCCRemoteException;

    /**
     * TODO: Add a description of this method.
     *
     * @param delta
     * @param definition
     * @return
     */
    public List<String> validateFields(final List<ParagraphDelta> delta,
                                       final String definition);

    /**
     * Delete the working copy for a page.
     *
     * @param pageId The id of the page with a working copy.
     */
    public void clearWorkingCopy(ID pageId) throws CCCRemoteException;




    /**
     * Create a new alias in CCC.
     */
    ResourceSummary createAlias(ID parentId, String name, ID targetId) throws CCCRemoteException;

    /**
     * Create a folder with the specified name.
     */
    ResourceSummary createFolder(ID parentId, String name) throws CCCRemoteException;

    /**
     * Create a folder with the specified name and title.
     */
    ResourceSummary createFolder(ID parentId, String name, String title) throws CCCRemoteException;

    /**
     * Create a root folder with the specified name.
     */
    ResourceSummary createRoot(String name) throws CCCRemoteException;

    /**
     * Create a new user in the system.
     */
    UserSummary createUser(UserDelta delta, String password) throws CCCRemoteException;

    /**
     * Creates a new page.
     */
    ResourceSummary createPage(ID parentId,
                               PageDelta delta,
                               String name,
                               final boolean publish,
                               ID templateId) throws CCCRemoteException;

    /**
     * Create a new template in CCC.
     */
    ResourceSummary createTemplate(ID parentId, TemplateDelta delta, String name) throws CCCRemoteException;

    /**
     * Creates a new search.
     *
     * @param parentId The parent folder where the search should be created.
     * @param title The title of the search.
     * @return A summary of the newly created search.
     */
    ResourceSummary createSearch(ID parentId, String title) throws CCCRemoteException;

    /**
     * Create a working copy for the specified resource, using the specified log
     * entry.
     *
     * @param resourceId The id of the resource.
     * @param index The index number of the log entry.
     */
    void createWorkingCopy(ID resourceId, long index) throws CCCRemoteException;

    /**
     * Cancel a scheduled action.
     *
     * @param actionId The id of the action to cancel.
     */
    void cancelAction(ID actionId) throws CCCRemoteException;

    /**
     * Create a new scheduled action.
     *
     * @param resourceId The id of the resource the action will operate on.
     * @param action The type of the action to be performed.
     * @param executeAfter The earliest date at which the action may be
     *  executed.
     * @param parameters Additional parameters for the action, as a JSON string.
     */
    void createAction(ID resourceId,
                      ActionType action,
                      Date executeAfter,
                      String parameters,
                      String comment,
                      boolean isMajorEdit) throws CCCRemoteException;

    /**
     * Change the order of resources in a folder.
     *
     * @param folderId The id of the folder to update.
     * @param order The new order of the resources.
     */
    void reorder(ID folderId, List<String> order) throws CCCRemoteException;

    /**
     * Change the security roles for a resource.
     *
     * @param resourceId The resource to update.
     * @param roles The new set of roles.
     */
    void changeRoles(ID resourceId, Collection<String> roles) throws CCCRemoteException;

    void applyWorkingCopyToFile(ID fileId) throws CCCRemoteException;

    /**
     * Update the period that a resource should be cached for.
     *
     * @param resourceId The resource to update.
     * @param duration The cache duration.
     */
    void updateCacheDuration(ID resourceId, Duration duration) throws CCCRemoteException;

    /**
     * Update the password for the specified user.
     *
     * @param userId The user's id.
     * @param password The new password to set.
     */
    void updateUserPassword(ID userId, String password) throws CCCRemoteException;
}

