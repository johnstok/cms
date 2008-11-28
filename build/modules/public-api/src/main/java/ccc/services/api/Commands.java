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




/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface Commands {

    /**
     * Update the specified page on the server.
     */
    void updatePage(String pageId, long version, PageDelta delta);

    /**
     * Rename resource.
     */
    void rename(final String resourceId, long version, final String name);

    /**
     * Update the tags for a resource.
     */
    void updateTags(String resourceId, long version, String tags);

    /**
     * Changes a resource's parent.
     */
    void move(String resourceId, long version, String newParentId);

    /**
     * Update an alias' target.
     */
    void updateAlias(String aliasId, long version, String targetId);

    /**
     * Updates the user in the system.
     */
    void updateUser(String userId, long version, UserDelta delta);

    /**
     * Update the specified resource's template on the server.
     */
    void updateResourceTemplate(String resourceId, long version, String templateId);

    /**
     * Update the specified template on the server.
     */
    void updateTemplate(String templateId, long version, TemplateDelta delta);

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
     * Create a new alias in CCC.
     */
    void createAlias(String parentId, String name, String targetId);

    /**
     * Create a folder with the specified name.
     */
    void createFolder(String parentId, String name);

    /**
     * Create a new user in the system.
     */
    void createUser(UserDelta delta);

    /**
     * Creates a new page.
     */
    void createPage(String parentId, PageDelta delta, String templateId);

    /**
     * Create a new template in CCC.
     */
    void createTemplate(String parentId, TemplateDelta delta);
}
