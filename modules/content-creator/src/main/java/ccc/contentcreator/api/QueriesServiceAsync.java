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

import ccc.api.ActionSummary;
import ccc.api.AliasDelta;
import ccc.api.Duration;
import ccc.api.FileDelta;
import ccc.api.FileSummary;
import ccc.api.ID;
import ccc.api.LogEntrySummary;
import ccc.api.PageDelta;
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;
import ccc.api.TemplateSummary;
import ccc.api.UserDelta;
import ccc.api.UserSummary;
import ccc.api.Username;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Async version of the {@link QueriesService} interface.
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

    void loggedInUser(AsyncCallback<UserSummary> callback);

    void lockedByCurrentUser(AsyncCallback<Collection<ResourceSummary>> callback);

    void locked(AsyncCallback<Collection<ResourceSummary>> callback);

    void history(ID resourceId,
                 AsyncCallback<Collection<LogEntrySummary>> callback);

    void templateDelta(ID templateId, AsyncCallback<TemplateDelta> asyncCallback);

    void userDelta(ID userId, AsyncCallback<UserDelta> asyncCallback);

    void aliasDelta(ID aliasId, AsyncCallback<AliasDelta> asyncCallback);

    void pageDelta(ID pageId, AsyncCallback<PageDelta> asyncCallback);

    void fileDelta(ID fileId, AsyncCallback<FileDelta> asyncCallback);

    void getAllContentImages(AsyncCallback<Collection<FileSummary>> callback);

    void metadata(ID resourceId,
                  AsyncCallback<Map<String, String>> asyncCallback);

    void listPendingActions(AsyncCallback<Collection<ActionSummary>> asyncCallback);

    void listCompletedActions(AsyncCallback<Collection<ActionSummary>> asyncCallback);

    void roles(final ID resourceId, AsyncCallback<Collection<String>> asyncCallback);

    void cacheDuration(ID resourceId, AsyncCallback<Duration> asyncCallback);

    void computeTemplate(ID resourceId, AsyncCallback<TemplateSummary> callback);

    void resourceForPath(String rootPath, AsyncCallback<ccc.api.ResourceSummary> callback);

    void resourceForLegacyId(String legacyId, AsyncCallback<ccc.api.ResourceSummary> callback);
}
