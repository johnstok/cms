/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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
