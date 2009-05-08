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
import java.util.Map;

import ccc.services.api.ActionSummary;
import ccc.services.api.AliasDelta;
import ccc.services.api.Duration;
import ccc.services.api.FileDelta;
import ccc.services.api.FileSummary;
import ccc.services.api.LogEntrySummary;
import ccc.services.api.PageDelta;
import ccc.services.api.ResourceDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface QueriesServiceAsync {

    void roots(AsyncCallback<Collection<ResourceSummary>> callback);

    void resource(String resourceId,
                  AsyncCallback<ResourceSummary> callback);

    void templates(AsyncCallback<Collection<TemplateDelta>> callback);

    void getFolderChildren(String folderId,
                           AsyncCallback<Collection<ResourceSummary>> callback);

    void getChildren(String resourceId,
                     AsyncCallback<Collection<ResourceSummary>> callback);

    void getAbsolutePath(String resourceId,
                         AsyncCallback<String> callback);

    void nameExistsInFolder(final String folderId,
                            final String name,
                            AsyncCallback<Boolean> callback);

    void listUsers(AsyncCallback<Collection<UserSummary>> callback);

    void listUsersWithRole(String role,
                           AsyncCallback<Collection<UserSummary>> callback);

    void listUsersWithUsername(String username,
                               AsyncCallback<Collection<UserSummary>> callback);

    void usernameExists(String username,
                        AsyncCallback<Boolean> callback);

    void listUsersWithEmail(String email,
                            AsyncCallback<Collection<UserSummary>> callback);

    void templateNameExists(final String templateName,
                            AsyncCallback<Boolean> callback);

    void getTemplateForResource(final String resourceId,
                                AsyncCallback<TemplateDelta> callback);

    void loggedInUser(AsyncCallback<UserSummary> callback);

    void lockedByCurrentUser(AsyncCallback<Collection<ResourceSummary>> callback);

    void locked(AsyncCallback<Collection<ResourceSummary>> callback);

    void history(String resourceId,
                 AsyncCallback<Collection<LogEntrySummary>> callback);

    void templateDelta(String templateId, AsyncCallback<TemplateDelta> asyncCallback);

    void userDelta(String userId, AsyncCallback<UserDelta> asyncCallback);

    void aliasDelta(String aliasId, AsyncCallback<AliasDelta> asyncCallback);

    void pageDelta(String pageId, AsyncCallback<PageDelta> asyncCallback);

    void folderDelta(String folderId, AsyncCallback<ResourceDelta> asyncCallback);

    void resourceDelta(String resourceId, AsyncCallback<ResourceDelta> asyncCallback);

    void fileDelta(String fileId, AsyncCallback<FileDelta> asyncCallback);

    void getAllContentImages(AsyncCallback<Collection<FileSummary>> callback);

    void metadata(String resourceId,
                  AsyncCallback<Map<String, String>> asyncCallback);

    void workingCopyDelta(String pageId, AsyncCallback<PageDelta> asyncCallback);

    void listPendingActions(AsyncCallback<Collection<ActionSummary>> asyncCallback);

    void listCompletedActions(AsyncCallback<Collection<ActionSummary>> asyncCallback);

    void roles(final String resourceId, AsyncCallback<Collection<String>> asyncCallback);

    void cacheDuration(String resourceId, AsyncCallback<Duration> asyncCallback);
}
