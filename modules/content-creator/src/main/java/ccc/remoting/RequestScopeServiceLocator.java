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
package ccc.remoting;

import javax.servlet.ServletRequest;

import ccc.remoting.actions.SessionKeys;
import ccc.rest.Actions;
import ccc.rest.Aliases;
import ccc.rest.Comments;
import ccc.rest.Files;
import ccc.rest.Folders;
import ccc.rest.Pages;
import ccc.rest.Resources;
import ccc.rest.SearchEngine;
import ccc.rest.Security;
import ccc.rest.ServiceLocator;
import ccc.rest.Templates;
import ccc.rest.Users;


/**
 * A service locator that retrieves services from a servlet request.
 *
 * @author Civic Computing Ltd.
 */
public class RequestScopeServiceLocator
    implements
        ServiceLocator {

    private final ServletRequest _request;


    /**
     * Constructor.
     *
     * @param request The servlet request to retrieve services from.
     */
    public RequestScopeServiceLocator(final ServletRequest request) {
        _request = request;
    }


    /** {@inheritDoc} */
    @Override
    public Actions getActions() {
        return (Actions) _request.getAttribute(SessionKeys.ACTIONS_KEY);
    }


    /** {@inheritDoc} */
    @Override
    public Files getFiles() {
        return (Files) _request.getAttribute(SessionKeys.FILES_KEY);
    }


    /** {@inheritDoc} */
    @Override
    public Folders getFolders() {
        return (Folders) _request.getAttribute(SessionKeys.FOLDERS_KEY);
    }


    /** {@inheritDoc} */
    @Override
    public Pages getPages() {
        return (Pages) _request.getAttribute(SessionKeys.PAGES_KEY);
    }


    /** {@inheritDoc} */
    @Override
    public Resources getResources() {
        return (Resources) _request.getAttribute(SessionKeys.RESOURCES_KEY);
    }


    /** {@inheritDoc} */
    @Override
    public Templates getTemplates() {
        return (Templates) _request.getAttribute(SessionKeys.TEMPLATES_KEY);
    }


    /** {@inheritDoc} */
    @Override
    public Users getUsers() {
        return (Users) _request.getAttribute(SessionKeys.USERS_KEY);
    }

    /** {@inheritDoc} */
    @Override
    public SearchEngine getSearch() {
        return (SearchEngine) _request.getAttribute(SessionKeys.SEARCH_KEY);
    }


    /** {@inheritDoc} */
    @Override
    public Aliases getAliases() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public Security getSecurity() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public Comments getComments() {
        return (Comments) _request.getAttribute(SessionKeys.COMMENTS_KEY);
    }
}
