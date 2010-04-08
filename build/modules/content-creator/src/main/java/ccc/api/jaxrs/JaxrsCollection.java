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

import ccc.api.client1.RegistryServiceLocator;
import ccc.commons.JNDI;
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
import ccc.rest.exceptions.RestException;


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
