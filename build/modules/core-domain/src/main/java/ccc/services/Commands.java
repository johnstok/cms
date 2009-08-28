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
package ccc.services;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ccc.api.AliasDelta;
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;
import ccc.domain.CommandFailedException;
import ccc.types.CommandType;
import ccc.types.Duration;
import ccc.types.ID;
import ccc.types.Paragraph;


/**
 * Command API, used to update data in CCC.
 *
 * @author Civic Computing Ltd.
 */
public interface Commands {

    /** NAME : String. */
    String NAME = "PublicCommands";

    /**
     * Update an alias.
     *
     * @param aliasId The id of the alias to update.
     * @param delta The changes to apply.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateAlias(ID aliasId, AliasDelta delta)
    throws CommandFailedException;

    /**
     * Update the specified template on the server.
     *
     * @param templateId The id of the template to update.
     * @param delta The changes to apply.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateTemplate(ID templateId, TemplateDelta delta)
    throws CommandFailedException;

    /**
     * Rename resource.
     *
     * @param resourceId The id of the resource to rename.
     * @param name The new name.
     *
     * @throws CommandFailedException If the method fails.
     */
    void rename(final ID resourceId, final String name)
    throws CommandFailedException;

    /**
     * Changes a resource's parent.
     *
     * @param resourceId The id of the resource to move.
     * @param newParentId The id of the folder to which the resource should be
     *  moved.
     *
     * @throws CommandFailedException If the method fails.
     */
    void move(ID resourceId, ID newParentId)
    throws CommandFailedException;

    /**
     * Update the specified resource's template on the server.
     *
     * @param resourceId The id of the resource to update.
     * @param templateId The new template to set for the resource.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateResourceTemplate(ID resourceId, ID templateId)
    throws CommandFailedException;

    /**
     * Update the specified resource's template on the server.
     *
     * @param resourceId The id of the resource to update.
     * @param templateId The new template to set for the resource.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateResourceTemplate(ID resourceId,
                                ID templateId,
                                ID actorId,
                                Date happenedOn)
    throws CommandFailedException;

    /**
     * Lock the specified resource.
     * The resource will be locked by the currently logged in user.
     * If the resource is already locked a CCCException will be thrown.
     *
     * @param resourceId The uuid of the resource to lock.
     *
     * @throws CommandFailedException If the method fails.
     */
    void lock(ID resourceId) throws CommandFailedException;

    /**
     * Lock the specified resource.
     * The resource will be locked by the currently logged in user.
     * If the resource is already locked a CCCException will be thrown.
     *
     * @param resourceId The uuid of the resource to lock.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     *
     * @throws CommandFailedException If the method fails.
     */
    void lock(ID resourceId, ID actorId, Date happenedOn)
    throws CommandFailedException;

    /**
     * Unlock the specified Resource.
     * If the logged in user does not have privileges to unlock this resource a
     * CCCException will be thrown.
     * Unlocking an unlocked resource has no effect.
     *
     * @param resourceId The resource to unlock.
     *
     * @throws CommandFailedException If the method fails.
     */
    void unlock(ID resourceId) throws CommandFailedException;

    /**
     * Unlock the specified Resource.
     * If the logged in user does not have privileges to unlock this resource a
     * CCCException will be thrown.
     * Unlocking an unlocked resource has no effect.
     *
     * @param resourceId The resource to unlock.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     *
     * @throws CommandFailedException If the method fails.
     */
    void unlock(ID resourceId, ID actorId, Date happenedOn)
    throws CommandFailedException;

    /**
     * Publish the specified resource.
     *
     * @param resourceId The id of the resource to update.
     *
     * @throws CommandFailedException If the method fails.
     */
    void publish(ID resourceId) throws CommandFailedException;

    /**
     * Publish the specified resource.
     *
     * @param resourceId The id of the resource to update.
     * @param userId The id of the publishing user.
     * @param publishDate The date the resource was published.
     *
     * @throws CommandFailedException If the method fails.
     */
    void publish(ID resourceId, ID userId, Date publishDate)
    throws CommandFailedException;

    /**
     * Unpublish the specified resource.
     *
     * @param resourceId The id of the resource to update.
     *
     * @throws CommandFailedException If the method fails.
     */
    void unpublish(ID resourceId) throws CommandFailedException;

    /**
     * Unpublish the specified resource.
     *
     * @param resourceId The id of the resource to update.
     * @param userId The id of the un-publishing user.
     * @param publishDate The date the resource was un-published.
     *
     * @throws CommandFailedException If the method fails.
     */
    void unpublish(ID resourceId, ID userId, Date publishDate)
                                                  throws CommandFailedException;

    /**
     * Specify whether a resource should be included in a site's main menu.
     *
     * @param resourceId The id of the resource to update.
     * @param include True if the resource should be included, false otherwise.
     *
     * @throws CommandFailedException If the method fails.
     */
    void includeInMainMenu(ID resourceId, boolean include)
    throws CommandFailedException;

    /**
     * Specify whether a resource should be included in a site's main menu.
     *
     * @param resourceId The id of the resource to update.
     * @param include True if the resource should be included, false otherwise.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     *
     * @throws CommandFailedException If the method fails.
     */
    void includeInMainMenu(ID resourceId,
                           boolean include,
                           ID actorId,
                           Date happenedOn)
    throws CommandFailedException;

    /**
     * Update metadata of the resource.
     *
     * @param resourceId The id of the resource to update.
     * @param title The new title to set.
     * @param description The new description to set.
     * @param tags The new tags to set.
     * @param metadata The metadata to update.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateMetadata(ID resourceId,
                        String title,
                        String description,
                        String tags,
                        Map<String, String> metadata,
                        ID actorId,
                        Date happenedOn)
    throws CommandFailedException;

    /**
     * Update metadata of the resource.
     *
     * @param resourceId The id of the resource to update.
     * @param title The new title to set.
     * @param description The new description to set.
     * @param tags The new tags to set.
     * @param metadata The metadata to update.
     * @throws  CommandFailedException If the method fails.
     */
    void updateMetadata(ID resourceId,
                        String title,
                        String description,
                        String tags,
                        Map<String, String> metadata)
    throws CommandFailedException;


    /**
     * Validate a set of paragraphs against a given definition.
     *
     * @param delta The paragraphs.
     * @param definition The xml definition, as a string.
     * @return A list of errors, as strings.
     */
    List<String> validateFields(final Set<Paragraph> delta,
                                final String definition);

    /**
     * Create a new alias in CCC.
     *
     * @param parentId The folder in which the alias should be created.
     * @param name The name of the alias.
     * @param targetId The target resource to which the alias should link.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A resource summary describing the new alias.
     */
    ResourceSummary createAlias(ID parentId, String name, ID targetId)
    throws CommandFailedException;


    /**
     * Create a new template in CCC.
     *
     * @param parentId The folder in which the template should be created.
     * @param delta The template's details.
     * @param title The template's title.
     * @param description The template's description.
     * @param name The template's name.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A resource summary describing the new template.
     */
    ResourceSummary createTemplate(ID parentId,
                                   TemplateDelta delta,
                                   String title,
                                   String description,
                                   String name) throws CommandFailedException;

    /**
     * Creates a new search.
     *
     * @param parentId The parent folder where the search should be created.
     * @param title The title of the search.
     *
     * @return A summary of the newly created search.
     *
     * @throws CommandFailedException If the method fails.
     */
    ResourceSummary createSearch(ID parentId, String title)
    throws CommandFailedException;

    /**
     * Create a working copy for the specified resource, using the specified log
     * entry.
     *
     * @param resourceId The id of the resource.
     * @param index The index number of the log entry.
     *
     * @throws CommandFailedException If the method fails.
     */
    void createWorkingCopy(ID resourceId, long index)
    throws CommandFailedException;

    /**
     * Cancel a scheduled action.
     *
     * @param actionId The id of the action to cancel.
     *
     * @throws CommandFailedException If the method fails.
     */
    void cancelAction(ID actionId) throws CommandFailedException;

    /**
     * Create a new scheduled action.
     *
     * @param resourceId The id of the resource the action will operate on.
     * @param action The type of the action to be performed.
     * @param executeAfter The earliest date at which the action may be
     *  executed.
     * @param parameters Additional parameters for the action.
     *
     * @throws CommandFailedException If the method fails.
     */
    void createAction(ID resourceId,
                      CommandType action,
                      Date executeAfter,
                      Map<String, String> parameters)
    throws CommandFailedException;

    /**
     * Change the security roles for a resource.
     *
     * @param resourceId The resource to update.
     * @param roles The new set of roles.
     *
     * @throws CommandFailedException If the method fails.
     */
    void changeRoles(ID resourceId, Collection<String> roles)
    throws CommandFailedException;

    /**
     * Change the security roles for a resource.
     *
     * @param resourceId The resource to update.
     * @param roles The new set of roles.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     *
     * @throws CommandFailedException If the method fails.
     */
    void changeRoles(ID resourceId,
                     Collection<String> roles,
                     ID actorId,
                     Date happenedOn)
    throws CommandFailedException;

    /**
     * Apply a resource's working copy.
     *
     * @param resourceId The id of the resource.
     *
     * @throws CommandFailedException If the method fails.
     */
    void applyWorkingCopy(ID resourceId) throws CommandFailedException;

    /**
     * Apply a resource's working copy.
     *
     * @param resourceId The id of the resource.
     * @param userId The user applying the working copy.
     * @param happenedOn When the command happened.
     * @param isMajorEdit Was this a major change.
     * @param comment A comment describing the change.
     *
     * @throws CommandFailedException If the method fails.
     */
    void applyWorkingCopy(ID resourceId,
                          ID userId,
                          Date happenedOn,
                          boolean isMajorEdit,
                          String comment) throws CommandFailedException;

    /**
     * Update the period that a resource should be cached for.
     *
     * @param resourceId The resource to update.
     * @param duration The cache duration.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateCacheDuration(ID resourceId, Duration duration)
    throws CommandFailedException;

}
