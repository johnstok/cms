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

import ccc.domain.Resource;


/**
 * Audit logging API.
 *
 * @author Civic Computing Ltd.
 */
interface AuditLog {

    /**
     * Record that a resource was locked.
     *
     * @param resource The resource that was locked.
     */
    void recordLock(final Resource resource);


    /**
     * Record that a resource was unlocked.
     *
     * @param resource The resource that was unlocked.
     */
    void recordUnlock(final Resource resource);
}
