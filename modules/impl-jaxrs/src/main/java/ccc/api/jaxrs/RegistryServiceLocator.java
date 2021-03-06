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

import ccc.api.synchronous.Actions;
import ccc.api.synchronous.Actions2;
import ccc.api.synchronous.Aliases;
import ccc.api.synchronous.Comments;
import ccc.api.synchronous.Files;
import ccc.api.synchronous.Folders;
import ccc.api.synchronous.Groups;
import ccc.api.synchronous.Pages;
import ccc.api.synchronous.Resources;
import ccc.api.synchronous.Scheduler;
import ccc.api.synchronous.SearchEngine;
import ccc.api.synchronous.SearchEngine2;
import ccc.api.synchronous.Security;
import ccc.api.synchronous.ServiceLocator;
import ccc.api.synchronous.Templates;
import ccc.api.synchronous.Users;
import ccc.api.types.DBC;
import ccc.commons.Registry;


/**
 * A class to perform application name specific Registry lookups.
 *
 * @author Civic Computing Ltd.
 */
public class RegistryServiceLocator implements ServiceLocator {

    private final Registry  _registry;
    private final String    _appName;
    private final Scheduler _actionScheduler;
    private final Scheduler _searchScheduler;


    /**
     * Constructor.
     *
     * @param appName The name of the application.
     * @param registry The registry to use for look up.
     * @param actionScheduler The scheduler that drives the Actions API.
     * @param searchScheduler The scheduler that drives the Search API.
     */
    public RegistryServiceLocator(final String appName,
                                  final Registry registry,
                                  final Scheduler actionScheduler,
                                  final Scheduler searchScheduler) {
        _appName =  DBC.require().notEmpty(appName);
        _registry = DBC.require().notNull(registry);
        _actionScheduler = DBC.require().notNull(actionScheduler);
        _searchScheduler = DBC.require().notNull(searchScheduler);
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
        return
        new Search2Impl(
            _registry.<SearchEngine2>get(localPath(SearchEngine.NAME)),
            _searchScheduler);

    }


    /** {@inheritDoc} */
    @Override
    public Templates getTemplates() {
        return _registry.<Templates>get(localPath(Templates.NAME));
    }

    /** {@inheritDoc} */
    @Override
    public Actions getActions() {
        return
            new Actions2Impl(
                _registry.<Actions2>get(localPath(Actions.NAME)),
                _actionScheduler);
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


    protected String localPath(final String serviceName) {
        return _appName+"/"+serviceName+"/local";
    }
}
