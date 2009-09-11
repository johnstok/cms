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
 * API implemented by resources that support snapshot creation.
 *
 * @param <T> The type of snapshot returned.
 *
 * @author Civic Computing Ltd.
 */
public interface SnapshotSupport<T> {


    /**
     * Create a snapshot for the working copy.
     *
     * @return A read-only snapshot of the resource.
     */
    T forWorkingCopy();


    /**
     * Create a snapshot for the current revision.
     *
     * @return A read-only snapshot of the resource.
     */
    T forCurrentRevision();


    /**
     * Create a snapshot for the specified revision.
     *
     * @param revNo The revision to create a snapshot for.
     *
     * @return A read-only snapshot of the resource.
     */
    T forSpecificRevision(final int revNo);

}
