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
package ccc.client.i18n;


/**
 * Messages for i18n.
 *
 * @author Civic Computing Ltd.
 */
public interface ErrorDescriptions {

    /**
     * "The resource that you tried to update is not locked.".
     *
     * @return The message, in the appropriate locale.
     */
    String unlocked();

    /**
     * "{0}Your action could not be completed.{1}".
     *
     *
     * @param htmlPrefix HTML appended before the message.
     * @param htmlSuffix HTML appended after the message.
     * @return The message, in the appropriate locale.
     */
    String couldNotComplete(String htmlPrefix, String htmlSuffix);

    /**
     * "Unknown error.".
     *
     * @return The message, in the appropriate locale.
     */
    String unknown();

    /**
     * "A resource already exists with the specified name.".
     *
     * @return The message, in the appropriate locale.
     */
    String exists();

    /**
     * "The resource you tried to update is locked by another user.".
     *
     * @return The message, in the appropriate locale.
     */
    String lockMismatch();

    /**
     * "Creating this relationship would cause a circular dependency.".
     *
     * @return The message, in the appropriate locale.
     */
    String cycle();

    /**
     * "Command parameters were not valid.".
     *
     * @return The message, in the appropriate locale.
     */
    String invalidCommand();
}
