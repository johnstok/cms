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

import ccc.api.types.Username;


/**
 * Messages for i18n.
 *
 * @author Civic Computing Ltd.
 */
public interface UIMessages {

    /**
     * "User with username ''{0}'' already exists.".
     *
     * @param username The param to display.
     * @return The constant, in the appropriate locale.
     */
    String userWithUsernameAlreadyExists(Username username);

    /**
     * "A resource with name ''{0}'' already exists in the parent folder.".
     *
     * @param name The param to display.
     * @return The constant, in the appropriate locale.
     */
    String nameExistsInParentFolder(String name);

    /**
     * "A resource with name ''{0}'' already exists in this folder.".
     *
     * @param name The param to display.
     * @return The constant, in the appropriate locale.
     */
    String nameExistsInFolder(String name);

    /**
     * "A template with name ''{0}'' already exists in this folder.".
     *
     * @param name The param to display.
     * @return The constant, in the appropriate locale.
     */
    String templateWithNameAlreadyExistsInThisFolder(String name);
}
