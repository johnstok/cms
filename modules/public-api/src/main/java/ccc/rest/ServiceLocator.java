/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
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
}
