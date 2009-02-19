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

import java.util.List;
import java.util.Map;





/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface Commands {


    /**
     * Update the specified page on the server.
     */
    void updatePage(PageDelta delta) throws CCCRemoteException;

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
    void rename(final String resourceId, final String name);

    /**
     * Update the tags for a resource.
     */
    void updateTags(String resourceId, String tags);

    /**
     * Changes a resource's parent.
     */
    void move(String resourceId, String newParentId);

    /**
     * Update the specified resource's template on the server.
     */
    void updateResourceTemplate(String resourceId, String templateId);

    /**
     * Lock the specified resource.
     * The resource will be locked by the currently logged in user.
     * If the resource is already locked a CCCException will be thrown.
     *
     * @param resourceId The uuid of the resource to lock.
     * @return The current version of resource.
     */
    ResourceSummary lock(String resourceId);

    /**
     * Unlock the specified Resource.
     * If the logged in user does not have privileges to unlock this resource a
     * CCCException will be thrown.
     * Unlocking an unlocked resource has no effect.
     *
     * @param resourceId The resource to unlock.
     * @return The current version of resource.
     */
    ResourceSummary unlock(String resourceId);

    /**
     * TODO: Add a description of this method.
     *
     * @param resourceId The id of the resource to update.
     * @return The current version of resource.
     */
    ResourceSummary publish(String resourceId);

    /**
     * TODO: Add a description of this method.
     *
     * @param resourceId The id of the resource to update.
     * @param resourceId The id of the publishing user.
     * @return The current version of resource.
     */
    ResourceSummary publish(String resourceId, String userId);

    /**
     * TODO: Add a description of this method.
     *
     * @param resourceId The id of the resource to update.
     * @return
     */
    ResourceSummary unpublish(String resourceId);

    /**
     * TODO: Add a description of this method.
     *
     * @param resourceId The id of the resource to update.
     * @param include
     */
    void includeInMainMenu(String resourceId, boolean include);

    /**
     * Update metadata of the resource.
     *
     * @param resourceId The id of the resource to update.
     * @param metadata The metadata to update.
     */
    void updateMetadata(String resourceId, Map<String, String> metadata);

    /**
     * Update the sort order for the specified folder.
     *
     * @param folderId The id of the folder to update.
     * @param sortOrder The new sort order.
     */
    void updateFolderSortOrder(String folderId, String sortOrder);

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
    public void clearWorkingCopy(String pageId);




    /**
     * Create a new alias in CCC.
     */
    ResourceSummary createAlias(String parentId, String name, String targetId);

    /**
     * Create a folder with the specified name.
     */
    ResourceSummary createFolder(String parentId, String name);

    /**
     * Create a root folder with the specified name.
     */
    ResourceSummary createRoot(String name);

    /**
     * Create a new user in the system.
     */
    UserSummary createUser(UserDelta delta);

    /**
     * Creates a new page.
     */
    ResourceSummary createPage(String parentId, PageDelta delta, String templateId);

    /**
     * Create a new template in CCC.
     */
    ResourceSummary createTemplate(String parentId, TemplateDelta delta);
}
