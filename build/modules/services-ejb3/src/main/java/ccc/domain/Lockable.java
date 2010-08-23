/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.domain;

/**
 * API for objects that can be locked by a user.
 *
 * @author Civic Computing Ltd.
 */
public interface Lockable {

    /**
     * Lock a resource.
     *
     * @param u The user who is locking the resource.
     */
    void lock(final UserEntity u);

    /**
     * Unlock the resource.
     * Only the user who locked the resource, or an administrator may call this
     * method.
     *
     * @param user The user releasing the lock.
     */
    void unlock(final UserEntity user);

    /**
     * Confirm this resource is locked by the specified user.
     * If this resource is locked by the specified user this method does
     * nothing; otherwise an exception is thrown.
     *
     * @param user The user who should have the lock.
     */
    void confirmLock(final UserEntity user);

}