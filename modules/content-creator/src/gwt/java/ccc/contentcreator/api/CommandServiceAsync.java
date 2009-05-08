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

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ccc.services.api.AliasDelta;
import ccc.services.api.Duration;
import ccc.services.api.PageDelta;
import ccc.services.api.ParagraphDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Async version of the {@link CommandService} interface.
 *
 * @author Civic Computing Ltd.
 */
public interface CommandServiceAsync {

    void updatePage(PageDelta delta, String comment, boolean isMajorEdit, AsyncCallback<Void> callback);

    void updateWorkingCopy(PageDelta delta, AsyncCallback<Void> callback);

    void rename(final String resourceId, final String name, AsyncCallback<Void> callback);

    void updateTags(String resourceId, String tags, AsyncCallback<Void> callback);

    void move(String resourceId, String newParentId, AsyncCallback<Void> callback);

    void updateAlias(AliasDelta delta, AsyncCallback<Void> callback);

    void updateUser(UserDelta delta, AsyncCallback<UserSummary> callback);

    void updateResourceTemplate(String resourceId, String templateId, AsyncCallback<Void> callback);

    void updateTemplate(TemplateDelta delta, AsyncCallback<ResourceSummary> callback);

    void lock(String resourceId, AsyncCallback<ResourceSummary> callback);

    void unlock(String resourceId, AsyncCallback<ResourceSummary> callback);

    void publish(String resourceId, AsyncCallback<ResourceSummary> callback);

    void publish(String resourceId, String userId, Date publishDate, AsyncCallback<ResourceSummary> callback);

    void unpublish(String resourceId, AsyncCallback<ResourceSummary> callback);

    void includeInMainMenu(String resourceId, boolean include, AsyncCallback<Void> callback);

    void validateFields(List<ParagraphDelta> delta, String definition, AsyncCallback<List <String>> callback);

    void updateMetadata(String resourceId, Map<String, String> metadata, AsyncCallback<Void> callback);

    void updateFolderSortOrder(String folderId, String sortOrder, AsyncCallback<Void> callback);

    public void clearWorkingCopy(String pageId, AsyncCallback<Void> callback);

    void createWorkingCopy(String resourceId, long index, AsyncCallback<Void> callback);

    void cancelAction(String actionId, AsyncCallback<Void> callback);

    void createAction(String resourceId, String action, Date executeAfter, String parameters, AsyncCallback<Void> callback);


    void createAlias(String parentId, String name, String targetId, AsyncCallback<ResourceSummary> callback);

    void createFolder(String parentId, String name, AsyncCallback<ResourceSummary> callback);

    void createFolder(String parentId, String name, String title, AsyncCallback<ResourceSummary> callback);

    void createUser(UserDelta delta, AsyncCallback<UserSummary> callback);

    void createPage(String parentId, PageDelta delta, String templateId, AsyncCallback<ResourceSummary> callback);

    void createTemplate(String parentId, TemplateDelta delta, AsyncCallback<ResourceSummary> callback);

    void createRoot(String name, AsyncCallback<ResourceSummary> callback);

    void createSearch(String parentId, String title, AsyncCallback<ResourceSummary> callback);

    void reorder(String folderId, List<String> order, AsyncCallback<Void> callback);

    void changeRoles(final String resourceId, final Collection<String> roles, AsyncCallback<Void> callback);

    void applyWorkingCopyToFile(String fileId, AsyncCallback<Void> callback);

    void updateCacheDuration(String resourceId, Duration duration, AsyncCallback<Void> callback);
}
