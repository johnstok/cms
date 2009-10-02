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
package ccc.remoting;

import javax.servlet.ServletRequest;

import ccc.remoting.actions.SessionKeys;
import ccc.rest.Actions;
import ccc.rest.Files;
import ccc.rest.Folders;
import ccc.rest.Pages;
import ccc.rest.Resources;
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
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public Files getFiles() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public Folders getFolders() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public Pages getPages() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public Resources getResources() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public Templates getTemplates() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public Users getUsers() {
        return (Users) _request.getAttribute(SessionKeys.USERS_KEY);
    }
}
