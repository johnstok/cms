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
import java.util.Set;

import ccc.api.AliasDelta;
import ccc.api.CommandType;
import ccc.api.Duration;
import ccc.api.ID;
import ccc.api.PageDelta;
import ccc.api.Paragraph;
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;
import ccc.api.UserDelta;
import ccc.api.UserSummary;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Async version of the {@link CommandService} interface.
 *
 * @author Civic Computing Ltd.
 */
public interface CommandServiceAsync {

    /*
     * Update methods.
     */
    void updatePage(ID pageId,
                    PageDelta delta,
                    String comment,
                    boolean isMajorEdit,
                    AsyncCallback<Void> callback);

    void updateWorkingCopy(ID pageId,
                           PageDelta delta,
                           AsyncCallback<Void> callback);

    void rename(final ID resourceId,
                final String name,
                AsyncCallback<Void> callback);

    void move(ID resourceId,
              ID newParentId,
              AsyncCallback<Void> callback);

    void updateAlias(ID aliasId,
                     AliasDelta delta,
                     AsyncCallback<Void> callback);

    void updateUser(ID userId,
                    UserDelta delta,
                    AsyncCallback<Void> callback);

    void updateResourceTemplate(ID resourceId,
                                ID templateId,
                                AsyncCallback<Void> callback);

    void updateTemplate(ID templateId,
                        TemplateDelta delta,
                        AsyncCallback<Void> callback);

    void lock(ID resourceId,
              AsyncCallback<Void> callback);

    void unlock(ID resourceId,
                AsyncCallback<Void> callback);

    void publish(ID resourceId,
                 AsyncCallback<Void> callback);

    void publish(ID resourceId,
                 ID userId,
                 Date publishDate,
                 AsyncCallback<Void> callback);

    void unpublish(ID resourceId,
                   AsyncCallback<Void> callback);

    void unpublish(ID resourceId,
                   final ID userId,
                   final Date publishDate,
                   AsyncCallback<Void> callback);

    void includeInMainMenu(ID resourceId,
                           boolean include,
                           AsyncCallback<Void> callback);

    void validateFields(Set<Paragraph> delta,
                        String definition,
                        AsyncCallback<List <String>> callback);

    void updateMetadata(ID resourceId,
                        String title,
                        String description,
                        String tags,
                        Map<String, String> metadata,
                        AsyncCallback<Void> callback);

    void updateFolder(ID folderId,
                      String sortOrder,
                      ID indexPageId,
                      AsyncCallback<Void> callback);

    public void clearWorkingCopy(ID pageId,
                                 AsyncCallback<Void> callback);

    void createWorkingCopy(ID resourceId,
                           long index,
                           AsyncCallback<Void> callback);

    void cancelAction(ID actionId,
                      AsyncCallback<Void> callback);

    void createAction(ID resourceId,
                      CommandType action,
                      Date executeAfter,
                      final Map<String, String> parameters,
                      AsyncCallback<Void> callback);

    void createAlias(ID parentId,
                     String name,
                     ID targetId,
                     AsyncCallback<ResourceSummary> callback);

    void reorder(ID folderId,
                 List<String> order,
                 AsyncCallback<Void> callback);

    void changeRoles(final ID resourceId,
                     final Collection<String> roles,
                     AsyncCallback<Void> callback);

    void applyWorkingCopy(ID resourceId, AsyncCallback<Void> callback);

    void applyWorkingCopy(ID resourceId,
                          ID userId,
                          Date happenedOn,
                          boolean isMajorEdit,
                          String comment,
                          AsyncCallback<Void> callback);

    void updateCacheDuration(ID resourceId,
                             Duration duration,
                             AsyncCallback<Void> callback);

    void updateUserPassword(ID userId,
                            String password,
                            AsyncCallback<Void> callback);



    /*
     * Create methods.
     */
    void createFolder(ID parentId,
                      String name,
                      AsyncCallback<ResourceSummary> callback);

    void createFolder(ID parentId,
                      String name,
                      String title,
                      boolean publish,
                      AsyncCallback<ResourceSummary> callback);

    void createUser(UserDelta delta,
                    String password,
                    AsyncCallback<UserSummary> callback);

    void createPage(ID parentId,
                    PageDelta delta,
                    String pageName,
                    final boolean publish,
                    ID templateId,
                    String title,
                    AsyncCallback<ResourceSummary> callback);

    void createTemplate(ID parentId,
                        TemplateDelta delta,
                        String title,
                        String description,
                        String name,
                        AsyncCallback<ResourceSummary> callback);

    void createRoot(String name,
                    AsyncCallback<ResourceSummary> callback);

    void createSearch(ID parentId,
                      String title,
                      AsyncCallback<ResourceSummary> callback);

    void changeRoles(ID resourceId,
                     java.util.Collection<java.lang.String> roles,
                     ID actorId,
                     Date happenedOn,
                     AsyncCallback<Void> callback);

    void createFolder(ID parentId, java.lang.String name,
                      java.lang.String title,
                      boolean publish,
                      ID actorId,
                      Date happenedOn,
                      AsyncCallback<ResourceSummary> callback);

    void createPage(
                    ID parentId,
                    PageDelta delta,
                    String name,
                    boolean publish,
                    ID templateId,
                    String title,
                    ID actorId,
                    Date happenedOn,
                    AsyncCallback<ResourceSummary> callback);

    void includeInMainMenu(ccc.api.ID resourceId,
                           boolean include,
                           ID actorId,
                           Date happenedOn,
                           AsyncCallback<Void> callback);

    void lock(ID resourceId,
              ID actorId,
              Date happenedOn,
              AsyncCallback<Void> callback);

    void unlock(ID resourceId,
                ID actorId,
                Date happenedOn,
                AsyncCallback<Void> callback);

    void updateMetadata(ID resourceId,
                        String title,
                        String description,
                        String tags,
                        Map<java.lang.String,
                        String> metadata,
                        ID actorId,
                        Date happenedOn,
                        AsyncCallback<Void> callback);

    void updatePage(ID pageId,
                    PageDelta delta,
                    String comment,
                    boolean isMajorEdit,
                    ID actorId,
                    Date happenedOn,
                    AsyncCallback<Void> callback);

    void updateResourceTemplate(ID resourceId,
                                ID templateId,
                                ID actorId,
                                Date happenedOn,
                                AsyncCallback<java.lang.Void> callback);
}
