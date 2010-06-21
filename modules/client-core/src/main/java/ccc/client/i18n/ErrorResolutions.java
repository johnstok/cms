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
public interface ErrorResolutions {

    /**
     * "Lock the selected resource and then try to perform the action again.".
     *
     * @return The message, in the appropriate locale.
     */
    String unlocked();

    /**
     * "Contact your system administrator.".
     *
     * @return The message, in the appropriate locale.
     */
    String contactSysAdmin();

    /**
     * "Try using a different name.".
     *
     * @return The message, in the appropriate locale.
     */
    String exists();

    /**
     * "Ask the user who locked the resource to unlock it.".
     *
     * @return The message, in the appropriate locale.
     */
    String lockMismatch();

    /**
     * "Choose a different resource.".
     *
     * @return The message, in the appropriate locale.
     */
    String cycle();

    /**
     * "Verify command parameters.".
     *
     * @return The message, in the appropriate locale.
     */
    String invalidCommand();
}
