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

import ccc.services.api.LogEntrySummary;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
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

    void nameExistsInParentFolder(final String id,
                                  final String name,
                                  AsyncCallback<Boolean> callback);

    void lockedByCurrentUser(AsyncCallback<Collection<ResourceSummary>> callback);

    void locked(AsyncCallback<Collection<ResourceSummary>> callback);

    void history(String resourceId,
                 AsyncCallback<Collection<LogEntrySummary>> callback);
}
