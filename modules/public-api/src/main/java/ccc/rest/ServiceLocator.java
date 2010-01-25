/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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

package ccc.rest;



/**
 * Locator for CCC business services.
 *
 * @author Civic Computing Ltd.
 */
public interface ServiceLocator {

    /**
     * Accessor.
     *
     * @return Returns the comments service.
     */
    Comments getComments();

    /**
     * Accessor.
     *
     * @return Returns the templates service.
     */
    Templates getTemplates();

    /**
     * Accessor.
     *
     * @return Returns the resources service.
     */
    Resources getResources();

    /**
     * Accessor.
     *
     * @return Returns the actions service.
     */
    Actions getActions();

    /**
     * Accessor.
     *
     * @return Returns the pages service.
     */
    Pages getPages();

    /**
     * Accessor.
     *
     * @return Returns the folders service.
     */
    Folders getFolders();

    /**
     * Accessor.
     *
     * @return Returns the users service.
     */
    Users getUsers();

    /**
     * Accessor.
     *
     * @return Returns the files service.
     */
    Files getFiles();

    /**
     * Accessor.
     *
     * @return Returns the search index service.
     */
    SearchEngine getSearch();

    /**
     * Accessor.
     *
     * @return Returns the security service.
     */
    Security getSecurity();

    /**
     * Accessor.
     *
     * @return Returns the alias service.
     */
    Aliases getAliases();

    /**
     * Accessor.
     *
     * @return Returns the groups service.
     */
    Groups getGroups();
}
