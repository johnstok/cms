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
 * TODO: Add Description for this type.
 * FIXME: Convert all 'xxxId' parameters to use the {@link ID} type.
 *
 * @author Civic Computing Ltd.
 */
public interface Commands {

    /** NAME : String. */
    String NAME = "PublicCommands";


    /**
     * Update the specified page on the server.
     */
    void updatePage(PageDelta delta, String comment, boolean isMajorEdit)
        throws CCCRemoteException;

    /**
     * Update the working copy of the specified page.
     */
    void updateWorkingCopy(PageDelta delta);

    /**
     * Update an alias' target.
     */
    void updateAlias(AliasDelta delta);

    /**
     * Updates the user in the system.
     */
    UserSummary updateUser(UserDelta delta);

    /**
     * Update the specified template on the server.
     */
    ResourceSummary updateTemplate(TemplateDelta delta);

    /**
     * Rename resource.
     */
    void rename(final ID resourceId, final String name);

    /**
     * Update the tags for a resource.
     */
    void updateTags(ID resourceId, String tags);

    /**
     * Changes a resource's parent.
     */
    void move(ID resourceId, ID newParentId);

    /**
     * Update the specified resource's template on the server.
     */
    void updateResourceTemplate(ID resourceId, ID templateId);

    /**
     * Lock the specified resource.
     * The resource will be locked by the currently logged in user.
     * If the resource is already locked a CCCException will be thrown.
     *
     * @param resourceId The uuid of the resource to lock.
     * @return The current version of resource.
     */
    ResourceSummary lock(ID resourceId);

    /**
     * Unlock the specified Resource.
     * If the logged in user does not have privileges to unlock this resource a
     * CCCException will be thrown.
     * Unlocking an unlocked resource has no effect.
     *
     * @param resourceId The resource to unlock.
     * @return The current version of resource.
     */
    ResourceSummary unlock(ID resourceId);

    /**
     * TODO: Add a description of this method.
     *
     * @param resourceId The id of the resource to update.
     * @return The current version of resource.
     */
    ResourceSummary publish(ID resourceId);

    /**
     * TODO: Add a description of this method.
     *
     * @param resourceId The id of the resource to update.
     * @param resourceId The id of the publishing user.
     * @return The current version of resource.
     */
    ResourceSummary publish(ID resourceId, ID userId, Date publishDate);

    /**
     * TODO: Add a description of this method.
     *
     * @param resourceId The id of the resource to update.
     * @return
     */
    ResourceSummary unpublish(ID resourceId);

    /**
     * TODO: Add a description of this method.
     *
     * @param resourceId The id of the resource to update.
     * @param include
     */
    void includeInMainMenu(ID resourceId, boolean include);

    /**
     * Update metadata of the resource.
     *
     * @param resourceId The id of the resource to update.
     * @param metadata The metadata to update.
     */
    void updateMetadata(ID resourceId, Map<String, String> metadata);

    /**
     * Update the sort order for the specified folder.
     *
     * @param folderId The id of the folder to update.
     * @param sortOrder The new sort order.
     */
    void updateFolderSortOrder(ID folderId, String sortOrder);

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
    public void clearWorkingCopy(ID pageId);




    /**
     * Create a new alias in CCC.
     */
    ResourceSummary createAlias(ID parentId, String name, ID targetId);

    /**
     * Create a folder with the specified name.
     */
    ResourceSummary createFolder(ID parentId, String name);

    /**
     * Create a folder with the specified name and title.
     */
    ResourceSummary createFolder(ID parentId, String name, String title);

    /**
     * Create a root folder with the specified name.
     */
    ResourceSummary createRoot(String name);

    /**
     * Create a new user in the system.
     */
    UserSummary createUser(UserDelta delta, String password);

    /**
     * Creates a new page.
     */
    ResourceSummary createPage(ID parentId, PageDelta delta, ID templateId);

    /**
     * Create a new template in CCC.
     */
    ResourceSummary createTemplate(ID parentId, TemplateDelta delta);

    /**
     * Creates a new search.
     */
    ResourceSummary createSearch(ID parentId, String title);

    /**
     * Create a working copy for the specified resource, using the specified log
     * entry.
     *
     * @param resourceId The id of the resource.
     * @param index The index number of the log entry.
     */
    void createWorkingCopy(ID resourceId, long index);

    void cancelAction(ID actionId);

    void createAction(ID resourceId, Action action, Date executeAfter, String parameters);

    /**
     * TODO: Add a description of this method.
     *
     * @param folderId
     * @param order
     */
    void reorder(ID folderId, List<String> order);

    void changeRoles(ID resourceId, Collection<String> roles);

    void applyWorkingCopyToFile(ID fileId);

    void updateCacheDuration(ID resourceId, Duration duration);

    /**
     * Update the password for the specified user.
     *
     * @param userId The user's id.
     * @param password The new password to set.
     */
    void updateUserPassword(ID userId, String password);
}

