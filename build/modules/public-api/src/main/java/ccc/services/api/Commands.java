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
    void updatePage(PageDelta delta) throws CCCRemoteException;

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
    void updateAlias(AliasDelta delta);

    /**
     * Updates the user in the system.
     */
    UserSummary updateUser(UserDelta delta);

    /**
     * Update the specified resource's template on the server.
     */
    void updateResourceTemplate(String resourceId, long version, String templateId);

    /**
     * Update the specified template on the server.
     */
    ResourceSummary updateTemplate(TemplateDelta delta);

    /**
     * Lock the specified resource.
     * The resource will be locked by the currently logged in user.
     * If the resource is already locked a CCCException will be thrown.
     *
     * @param resourceId The uuid of the resource to lock.
     * @return The current version of resource.
     */
    ResourceSummary lock(String resourceId, long version);

    /**
     * Unlock the specified Resource.
     * If the logged in user does not have privileges to unlock this resource a
     * CCCException will be thrown.
     * Unlocking an unlocked resource has no effect.
     *
     * @param resourceId The resource to unlock.
     * @return The current version of resource.
     */
    ResourceSummary unlock(String resourceId, long version);

    /**
     * TODO: Add a description of this method.
     *
     * @param resourceId The id of the resource to update.
     * @return The current version of resource.
     */
    ResourceSummary publish(String resourceId, long version);

    ResourceSummary unpublish(String resourceId, long version);





    /**
     * Create a new alias in CCC.
     */
    void createAlias(String parentId, String name, String targetId);

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
