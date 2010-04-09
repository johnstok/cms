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

package ccc.api.jaxrs;

import javax.ejb.EJBException;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import ccc.api.ActionScheduler;
import ccc.api.Actions;
import ccc.api.Aliases;
import ccc.api.Comments;
import ccc.api.Files;
import ccc.api.Folders;
import ccc.api.Groups;
import ccc.api.Pages;
import ccc.api.Resources;
import ccc.api.SearchEngine;
import ccc.api.Security;
import ccc.api.ServiceLocator;
import ccc.api.Templates;
import ccc.api.Users;
import ccc.api.client1.RegistryServiceLocator;
import ccc.api.exceptions.RestException;
import ccc.commons.JNDI;


/**
 * Helper class for implementing JAX-RS collections.
 *
 * @author Civic Computing Ltd.
 */
abstract class JaxrsCollection {

    @Context private ServletContext _sContext;


    private ServiceLocator getServiceLocator() {
        return new RegistryServiceLocator(getAppName(), new JNDI());
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


    /**
     * Convert an EJB exception to a native CC exception.
     *
     * @param e The exception to convert.
     *
     * @return The corresponding CC exception.
     */
    protected RuntimeException convertToNative(final EJBException e) {
        final Exception cause = e.getCausedByException();
        if (cause instanceof RestException) {
            return (RestException) cause;
        }
        return e;
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


    protected Folders defaultFolders() {
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


    protected Users defaultUsers() {
        return getServiceLocator().getUsers();
    }


    public ActionScheduler lookupActionScheduler() {
        return getServiceLocator().lookupActionScheduler();
    }
}
