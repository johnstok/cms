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
import ccc.services.api.ID;
import ccc.services.api.LogEntrySummary;
import ccc.services.api.PageDelta;
import ccc.services.api.ResourceDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.TemplateSummary;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;
import ccc.services.api.Username;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface QueriesServiceAsync {

    void roots(AsyncCallback<Collection<ResourceSummary>> callback);

    void resource(ID resourceId,
                  AsyncCallback<ResourceSummary> callback);

    void templates(AsyncCallback<Collection<TemplateSummary>> callback);

    void getFolderChildren(ID folderId,
                           AsyncCallback<Collection<ResourceSummary>> callback);

    void getChildren(ID resourceId,
                     AsyncCallback<Collection<ResourceSummary>> callback);

    void getAbsolutePath(ID resourceId,
                         AsyncCallback<String> callback);

    void nameExistsInFolder(final ID folderId,
                            final String name,
                            AsyncCallback<Boolean> callback);

    void listUsers(AsyncCallback<Collection<UserSummary>> callback);

    void listUsersWithRole(String role,
                           AsyncCallback<Collection<UserSummary>> callback);

    void listUsersWithUsername(String username,
                               AsyncCallback<Collection<UserSummary>> callback);

    void usernameExists(Username username,
                        AsyncCallback<Boolean> callback);

    void listUsersWithEmail(String email,
                            AsyncCallback<Collection<UserSummary>> callback);

    void templateNameExists(final String templateName,
                            AsyncCallback<Boolean> callback);

    void getTemplateForResource(final ID resourceId,
                                AsyncCallback<TemplateDelta> callback);

    void loggedInUser(AsyncCallback<UserSummary> callback);

    void lockedByCurrentUser(AsyncCallback<Collection<ResourceSummary>> callback);

    void locked(AsyncCallback<Collection<ResourceSummary>> callback);

    void history(ID resourceId,
                 AsyncCallback<Collection<LogEntrySummary>> callback);

    void templateDelta(ID templateId, AsyncCallback<TemplateDelta> asyncCallback);

    void userDelta(ID userId, AsyncCallback<UserDelta> asyncCallback);

    void aliasDelta(ID aliasId, AsyncCallback<AliasDelta> asyncCallback);

    void pageDelta(ID pageId, AsyncCallback<PageDelta> asyncCallback);

    void folderDelta(ID folderId, AsyncCallback<ResourceDelta> asyncCallback);

    void resourceDelta(ID resourceId, AsyncCallback<ResourceDelta> asyncCallback);

    void fileDelta(ID fileId, AsyncCallback<FileDelta> asyncCallback);

    void getAllContentImages(AsyncCallback<Collection<FileSummary>> callback);

    void metadata(ID resourceId,
                  AsyncCallback<Map<String, String>> asyncCallback);

    void workingCopyDelta(ID pageId, AsyncCallback<PageDelta> asyncCallback);

    void listPendingActions(AsyncCallback<Collection<ActionSummary>> asyncCallback);

    void listCompletedActions(AsyncCallback<Collection<ActionSummary>> asyncCallback);

    void roles(final ID resourceId, AsyncCallback<Collection<String>> asyncCallback);

    void cacheDuration(ID resourceId, AsyncCallback<Duration> asyncCallback);

    void computeTemplate(ID resourceId, AsyncCallback<TemplateSummary> callback);
}
