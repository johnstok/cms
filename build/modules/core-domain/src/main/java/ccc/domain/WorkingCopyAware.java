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
package ccc.domain;



/**
 * API for working-copy support.
 *
 * @author Civic Computing Ltd.
 */
public interface WorkingCopyAware {

    /**
     * Apply a snapshot to this object.
     *
     * @param s The snapshot to apply.
     */
    void applySnapshot(final Snapshot s);

    /**
     * Accessor.
     *
     * @return The current working copy for this page, or null if there is no
     *      working copy.
     */
    Snapshot workingCopy();

    /**
     * Clear the current working copy.
     */
    void clearWorkingCopy();

    /**
     * Mutator.
     *
     * @param snapshot The new working copy for this page.
     */
    void workingCopy(final Snapshot snapshot);
}
