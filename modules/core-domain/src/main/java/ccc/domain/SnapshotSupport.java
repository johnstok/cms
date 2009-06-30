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
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface SnapshotSupport<T> {

    T forWorkingCopy();

    T forCurrentRevision();

    T forSpecificRevision(final int revNo);

}