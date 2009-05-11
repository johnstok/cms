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
    void updateWorkingCopy(final User actor,
                           UUID id,
                           Snapshot workingCopy);

    /**
     * Delete a page's working copy.
     *
     * @param id The page's id.
     */
    void clearWorkingCopy(final User actor,
                          UUID id);
}
