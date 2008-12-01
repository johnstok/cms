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
package ccc.contentcreator.api;

import ccc.services.api.FolderSummary;
import ccc.services.api.PageDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.TemplateSummary;
import ccc.services.api.UserDelta;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface CommandServiceAsync {

    void updatePage(String pageId, long version, PageDelta delta, AsyncCallback<Void> callback);

    void rename(final String resourceId, long version, final String name, AsyncCallback<Void> callback);

    void updateTags(String resourceId, long version, String tags, AsyncCallback<Void> callback);

    void move(String resourceId, long version, String newParentId, AsyncCallback<Void> callback);

    void updateAlias(String aliasId, long version, String targetId, AsyncCallback<Void> callback);

    void updateUser(String userId, long version, UserDelta delta, AsyncCallback<Void> callback);

    void updateResourceTemplate(String resourceId, long version, String templateId, AsyncCallback<Void> callback);

    void updateTemplate(String templateId, long version, TemplateDelta delta, AsyncCallback<TemplateSummary> callback);

    void lock(String resourceId, AsyncCallback<ResourceSummary> callback);

    void unlock(String resourceId, AsyncCallback<ResourceSummary> callback);

    void publish(String resourceId, AsyncCallback<ResourceSummary> callback);

    void unpublish(String resourceId, AsyncCallback<ResourceSummary> callback);





    void createAlias(String parentId, String name, String targetId, AsyncCallback<Void> callback);

    void createFolder(String parentId, String name, AsyncCallback<FolderSummary> callback);

    void createUser(UserDelta delta, AsyncCallback<Void> callback);

    void createPage(String parentId, PageDelta delta, String templateId, AsyncCallback<Void> callback);

    void createTemplate(String parentId, TemplateDelta delta, AsyncCallback<Void> callback);






    void logout(AsyncCallback<Void> asyncCallback);
}
