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
package ccc.api.jaxrs;

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
import ccc.api.types.DBC;
import ccc.commons.Registry;


/**
 * A class to perform application name specific Registry lookups.
 *
 * @author Civic Computing Ltd.
 */
// FIXME: This class is not a part of the HTTP client.
public class RegistryServiceLocator implements ServiceLocator {

    private final Registry _registry;
    private final String _appName;


    /**
     * Constructor.
     *
     * @param appName The name of the application.
     * @param registry The registry to use for look up.
     */
    public RegistryServiceLocator(final String appName,
                                  final Registry registry) {
        _appName =  DBC.require().notEmpty(appName);
        _registry = DBC.require().notNull(registry);
    }


    /** {@inheritDoc} */
    @Override
    public Resources getResources() {
        return _registry.<Resources>get(localPath(Resources.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public Pages getPages() {
        return _registry.<Pages>get(localPath(Pages.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public Folders getFolders() {
        return _registry.<Folders>get(localPath(Folders.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public Users getUsers() {
        return _registry.<Users>get(localPath(Users.NAME));
    }

    /** {@inheritDoc} */
    @Override
    public SearchEngine getSearch() {
        return _registry.<SearchEngine>get(localPath(SearchEngine.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public ActionScheduler lookupActionScheduler() {
        return _registry.<ActionScheduler>get(localPath(ActionScheduler.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public Templates getTemplates() {
        return _registry.<Templates>get(localPath(Templates.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public Actions getActions() {
        return _registry.<Actions>get(localPath(Actions.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public Groups getGroups() {
        return _registry.<Groups>get(localPath(Groups.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public Files getFiles() {
        return _registry.<Files>get(localPath(Files.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public Aliases getAliases() {
        return _registry.<Aliases>get(localPath(Aliases.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public Security getSecurity() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public Comments getComments() {
        return _registry.<Comments>get(localPath(Comments.NAME));
    }


    protected Registry getRegistry() {
        return _registry;
    }


    protected String remotePath(final String serviceName) {
        return _appName+"/"+serviceName+"/remote";
    }


    protected String localPath(final String serviceName) {
        return _appName+"/"+serviceName+"/local";
    }
}
