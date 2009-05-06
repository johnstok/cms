/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
import java.util.UUID;

import ccc.domain.Snapshot;
import ccc.domain.User;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface IWorkingCopyManager {

    /**
     * Updates the working copy.
     *
     * @param id The identifier for the page.
     * @param workingCopy The snapshot to use as a working copy.
     */
    void updateWorkingCopy(UUID id, Snapshot workingCopy);

    /**
     * Delete a page's working copy.
     *
     * @param id The page's id.
     */
    void clearWorkingCopy(UUID id);

    /**
     * Applies the current working copy to update a page.
     *
     * @param id The page's id.
     * @param comment The comment for the page edit.
     * @param isMajorEdit A boolean for major edit.
     * @param actor The actor that performed the update.
     * @param happenedOn The date the update took place.
     */
    void applyWorkingCopy(UUID id,
                          String comment,
                          boolean isMajorEdit,
                          User actor,
                          Date happenedOn);

    /**
     * Applies the current working copy to update a resource.
     *
     * @param id The resource's id.
     * @param actor The actor that performed the update.
     */
    void applyWorkingCopy(final UUID id, User actor);
}
