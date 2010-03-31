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

package ccc.rest.impl;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import ccc.api.client1.RegistryServiceLocator;
import ccc.rest.ActionScheduler;
import ccc.rest.Actions;
import ccc.rest.Aliases;
import ccc.rest.Comments;
import ccc.rest.Files;
import ccc.rest.Folders;
import ccc.rest.Groups;
import ccc.rest.Pages;
import ccc.rest.Resources;
import ccc.rest.SearchEngine;
import ccc.rest.Security;
import ccc.rest.ServiceLocator;
import ccc.rest.Templates;
import ccc.rest.Users;


/**
 * Helper class for implementing JAX-RS collections.
 *
 * @author Civic Computing Ltd.
 */
abstract class JaxrsCollection {

    private          ServiceLocator _locator;
    @Context private ServletContext _sContext;


    /**
     * Mutator.
     *
     * @param sl The service locator to set.
     */
    void setServiceLocator(final ServiceLocator sl) {
        _locator = sl;
    }


    /**
     * Accessor.
     *
     * @return The current service locator.
     */
    ServiceLocator getServiceLocator() {
        return
            (null!=_locator)
                ? _locator
                : new RegistryServiceLocator(getAppName());
    }


    /**
     * Accessor.
     *
     * @return The name of the app, as a string.
     */
    protected String getAppName() {
        return _sContext.getInitParameter("ccc.application-name");
    }


    /**
     * Accessor.
     *
     * @return The context of the app, as a string.
     */
    protected String getContextName() {
        return _sContext.getInitParameter("ccc.context-name");
    }


    public Actions getActions() {
        return getServiceLocator().getActions();
    }


    public Aliases getAliases() {
        return getServiceLocator().getAliases();
    }


    public Comments getComments() {
        return getServiceLocator().getComments();
    }


    public Files getFiles() {
        return getServiceLocator().getFiles();
    }


    public Folders getFolders() {
        return getServiceLocator().getFolders();
    }


    public Groups getGroups() {
        return getServiceLocator().getGroups();
    }


    public Pages getPages() {
        return getServiceLocator().getPages();
    }


    public Resources getResources() {
        return getServiceLocator().getResources();
    }


    public SearchEngine getSearch() {
        return getServiceLocator().getSearch();
    }


    public Security getSecurity() {
        return getServiceLocator().getSecurity();
    }


    public Templates getTemplates() {
        return getServiceLocator().getTemplates();
    }


    public Users getUsers() {
        return getServiceLocator().getUsers();
    }


    public ActionScheduler lookupActionScheduler() {
        return getServiceLocator().lookupActionScheduler();
    }
}
