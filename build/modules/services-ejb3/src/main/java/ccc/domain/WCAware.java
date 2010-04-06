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
    void setOrUpdateWorkingCopy(final T snapshot);

    /**
     * Query method.
     *
     * @return True if this object has a working copy, false otherwise.
     */
    boolean hasWorkingCopy();

    /**
     * Apply the current working copy.
     *
     * @param metadata The metadata describing the revision.
     */
    void applyWorkingCopy(final RevisionMetadata metadata);

    /**
     * Accessor.
     *
     * @return The current working copy for this page, or a new working copy if
     *  none exists.
     */
    T getOrCreateWorkingCopy();
}
