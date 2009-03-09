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

import ccc.domain.Resource;
import ccc.domain.User;


/**
 * Audit logging API.
 *
 * @author Civic Computing Ltd.
 */
public interface AuditLog {

    /**
     * Record that a resource was locked.
     *
     * @param resource The resource that was locked.
     */
    void recordLock(final Resource resource, User actor, Date happenedOn);


    /**
     * Record that a resource was unlocked.
     *
     * @param resource The resource that was unlocked.
     */
    void recordUnlock(final Resource resource, User actor, Date happenedOn);


    /**
     * Record that a resource was created.
     *
     * @param resource The resource that was created.
     */
    void recordCreate(Resource resource, User actor, Date happenedOn);

    /**
     * Record that a resource was updated.
     *
     * @param resource The resource that was updated.
     */
    void recordUpdate(Resource resource, User actor, Date happenedOn, final String comment, final boolean isMajorEdit);

    /**
     * Record that a resource was updated.
     *
     * @param resource The resource that was updated.
     */
    void recordUpdate(Resource resource, User actor, Date happenedOn);

    /**
     * Record that the template for a resource was changed.
     *
     * @param resource The resource that was changed.
     */
    void recordChangeTemplate(Resource resource, User actor, Date happenedOn);

    /**
     * Record that the resource was moved.
     *
     * @param resource The resource that was changed.
     */
    void recordMove(Resource resource, User actor, Date happenedOn);

    /**
     * Record that the resource was renamed.
     *
     * @param resource The resource that was changed.
     */
    void recordRename(Resource resource, User actor, Date happenedOn);

    /**
     * Record that a resource was published.
     *
     * @param resource The resource that was published.
     */
    void recordPublish(final Resource resource, User actor, Date happenedOn);

    /**
     * Record that a resource was unpublished.
     *
     * @param resource The resource that was unpublished.
     */
    void recordUnpublish(final Resource resource, User actor, Date happenedOn);
}
