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

import java.util.Date;

import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Password;
import ccc.domain.Resource;
import ccc.domain.User;


/**
 * Audit logging API.
 *
 * @author Civic Computing Ltd.
 */
public interface AuditLog {

    /** NAME : String. */
    String NAME = "AuditLog";


    /**
     * Record that a resource was locked.
     *
     * @param resource The resource that was locked.
     * @param actor The actor of the change.
     * @param happenedOn The date of the change.
     */
    void recordLock(final Resource resource, User actor, Date happenedOn);


    /**
     * Record that a resource was unlocked.
     *
     * @param resource The resource that was unlocked.
     * @param actor The actor of the change.
     * @param happenedOn The date of the change.
     */
    void recordUnlock(final Resource resource, User actor, Date happenedOn);


    /**
     * Record that a resource was created.
     *
     * @param resource The resource that was created.
     * @param actor The actor of the change.
     * @param happenedOn The date of the change.
     */
    void recordCreate(Resource resource, User actor, Date happenedOn);

    /**
     * Record that a resource was updated.
     *
     * @param resource The resource that was updated.
     * @param actor The actor of the change.
     * @param happenedOn The date of the change.
     * @param comment The comment for the update.
     * @param isMajorEdit The boolean for major edit.
     */
    void recordUpdate(Resource resource,
                      User actor, Date
                      happenedOn,
                      final String comment,
                      final boolean isMajorEdit);

    /**
     * Record that a resource was updated.
     *
     * @param resource The resource that was updated.
     * @param actor The actor of the change.
     * @param happenedOn The date of the change.
     */
    void recordUpdate(Resource resource, User actor, Date happenedOn);

    /**
     * Record that the template for a resource was changed.
     *
     * @param resource The resource that was changed.
     * @param actor The actor of the change.
     * @param happenedOn The date of the change.
     */
    void recordChangeTemplate(Resource resource, User actor, Date happenedOn);

    /**
     * Record that the resource was moved.
     *
     * @param resource The resource that was changed.
     * @param actor The actor of the change.
     * @param happenedOn The date of the change.
     */
    void recordMove(Resource resource, User actor, Date happenedOn);

    /**
     * Record that the resource was renamed.
     *
     * @param resource The resource that was changed.
     * @param actor The actor of the change.
     * @param happenedOn The date of the change.
     */
    void recordRename(Resource resource, User actor, Date happenedOn);

    /**
     * Record that a resource was published.
     *
     * @param resource The resource that was published.
     * @param actor The actor of the change.
     * @param happenedOn The date of the change.
     */
    void recordPublish(final Resource resource, User actor, Date happenedOn);

    /**
     * Record that a resource was unpublished.
     *
     * @param resource The resource that was unpublished.
     * @param actor The actor of the change.
     * @param happenedOn The date of the change.
     */
    void recordUnpublish(final Resource resource, User actor, Date happenedOn);


    /**
     * Find a log entry, given its index number.
     *
     * @param index The index number.
     * @return The log entry with the corresponding index number.
     */
    LogEntry findEntryForIndex(long index);


    /**
     * Record that a resource was included in main menu.
     *
     * @param resource The resource that was changed.
     * @param actor The actor of the change.
     * @param happenedOn The date of the change.
     */
    void recordIncludeInMainMenu(Resource resource,
                                 User actor,
                                 Date happenedOn);

    /**
     * Record that a resource was removed from main menu.
     *
     * @param resource The resource that was changed.
     * @param actor The actor of the change.
     * @param happenedOn The date of the change.
     */
    void recordRemoveFromMainMenu(Resource resource,
                                  User actor, Date
                                  happenedOn);


    /**
     * Record that a roles of the resource were changed.
     *
     * @param resource The resource that was changed.
     * @param actor The actor of the change.
     * @param happenedOn The date of the change.
     */
    void recordChangeRoles(Resource resource, User actor, Date happenedOn);


    /**
     * Record that the folder was updated.
     *
     * @param folder The folder that was changed.
     * @param actor The actor of the change.
     * @param happenedOn The date of the change.
     */
    void recordFolderUpdate(Folder folder, User actor, Date happenedOn);


    /**
     * Record that the folder's manual sorting was reordered.
     *
     * @param folder The folder that was changed.
     * @param actor The actor of the change.
     * @param happenedOn The date of the change.
     */
    void recordReorder(Folder folder, User actor, Date happenedOn);


    /**
     * Record that cache setting of the resource was changed.
     *
     * @param resource The resource that was changed.
     * @param actor The actor of the change.
     * @param happenedOn The date of the change.
     */
    void recordUpdateCache(Resource resource, User actor, Date happenedOn);

    /**
     * Record that a user's password was changed.
     *
     * @param pw The password that changed.
     * @param actor The user that performed the change.
     * @param happenedOn When the change occurred.
     */
    void recordUserChangePassword(Password pw, User actor, Date happenedOn);

    /**
     * Record a log entry.
     *
     * @param le The log entry to record.
     */
    void record(LogEntry le);


    /**
     * Record that a resource's metadata was updated.
     *
     * @param resource The resource that was changed.
     * @param actor The actor of the change.
     * @param happenedOn The date of the change.
     */
    void recordUpdateFullMetadata(Resource r, User actor, Date happenedOn);
}
