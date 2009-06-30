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
 * The API for working copy support.
 *
 * @author Civic Computing Ltd.
 * @param <T>
 */
public interface WCAware<T> {

    /**
     * Clear the current working copy.
     */
    void clearWorkingCopy();

    /**
     * Mutator.
     *
     * @param snapshot The new working copy for this page.
     */
    void workingCopy(final T snapshot);

    /**
     * Query method.
     *
     * @return True if this object has a working copy, false otherwise.
     */
    boolean hasWorkingCopy();

    /**
     * Apply a snapshot to this resource.
     */
    void applySnapshot(final RevisionMetadata metadata); // Should be applyWorkingCopy()

    /**
     * Accessor.
     *
     * @return The current working copy for this page, or a new working copy if
     *  none exists.
     */
    T workingCopy();
}
