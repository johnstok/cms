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

import ccc.services.api.Action;
import ccc.services.api.AliasDelta;
import ccc.services.api.Duration;
import ccc.services.api.ID;
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

    void rename(final ID resourceId, final String name, AsyncCallback<Void> callback);

    void updateTags(ID resourceId, String tags, AsyncCallback<Void> callback);

    void move(ID resourceId, ID newParentId, AsyncCallback<Void> callback);

    void updateAlias(AliasDelta delta, AsyncCallback<Void> callback);

    void updateUser(UserDelta delta, AsyncCallback<UserSummary> callback);

    void updateResourceTemplate(ID resourceId, ID templateId, AsyncCallback<Void> callback);

    void updateTemplate(TemplateDelta delta, AsyncCallback<ResourceSummary> callback);

    void lock(ID resourceId, AsyncCallback<ResourceSummary> callback);

    void unlock(ID resourceId, AsyncCallback<ResourceSummary> callback);

    void publish(ID resourceId, AsyncCallback<ResourceSummary> callback);

    void publish(ID resourceId, ID userId, Date publishDate, AsyncCallback<ResourceSummary> callback);

    void unpublish(ID resourceId, AsyncCallback<ResourceSummary> callback);

    void includeInMainMenu(ID resourceId, boolean include, AsyncCallback<Void> callback);

    void validateFields(List<ParagraphDelta> delta, String definition, AsyncCallback<List <String>> callback);

    void updateMetadata(ID resourceId, Map<String, String> metadata, AsyncCallback<Void> callback);

    void updateFolderSortOrder(ID folderId, String sortOrder, AsyncCallback<Void> callback);

    public void clearWorkingCopy(ID pageId, AsyncCallback<Void> callback);

    void createWorkingCopy(ID resourceId, long index, AsyncCallback<Void> callback);

    void cancelAction(ID actionId, AsyncCallback<Void> callback);

    void createAction(ID resourceId, Action action, Date executeAfter, String parameters, AsyncCallback<Void> callback);


    void createAlias(ID parentId, String name, ID targetId, AsyncCallback<ResourceSummary> callback);

    void createFolder(ID parentId, String name, AsyncCallback<ResourceSummary> callback);

    void createFolder(ID parentId, String name, String title, AsyncCallback<ResourceSummary> callback);

    void createUser(UserDelta delta, AsyncCallback<UserSummary> callback);

    void createPage(ID parentId, PageDelta delta, ID templateId, AsyncCallback<ResourceSummary> callback);

    void createTemplate(ID parentId, TemplateDelta delta, AsyncCallback<ResourceSummary> callback);

    void createRoot(String name, AsyncCallback<ResourceSummary> callback);

    void createSearch(ID parentId, String title, AsyncCallback<ResourceSummary> callback);

    void reorder(ID folderId, List<String> order, AsyncCallback<Void> callback);

    void changeRoles(final ID resourceId, final Collection<String> roles, AsyncCallback<Void> callback);

    void applyWorkingCopyToFile(ID fileId, AsyncCallback<Void> callback);

    void updateCacheDuration(ID resourceId, Duration duration, AsyncCallback<Void> callback);
}
