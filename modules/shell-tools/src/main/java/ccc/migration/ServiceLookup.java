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
package ccc.migration;

import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.domain.Scheduler;
import ccc.rest.ActionScheduler;
import ccc.rest.Actions;
import ccc.rest.Aliases;
import ccc.rest.Files;
import ccc.rest.Folders;
import ccc.rest.Pages;
import ccc.rest.Resources;
import ccc.rest.SearchEngine;
import ccc.rest.Security;
import ccc.rest.ServiceLocator;
import ccc.rest.Templates;
import ccc.rest.Users;
import ccc.rest.extensions.FoldersExt;
import ccc.rest.extensions.PagesExt;
import ccc.rest.extensions.ResourcesExt;


/**
 * A class to perform application name specific Registry lookups.
 *
 * @author Civic Computing Ltd.
 */
public class ServiceLookup implements ServiceLocator {

    private final Registry _registry;
    private final String _appName;


    /**
     * Constructor.
     *
     * @param appName The name of the application.
     * @param registry The registry to use for look up.
     */
    public ServiceLookup(final String appName, final Registry registry) {
        _appName = appName;
        _registry = registry;
    }


    /**
     * Constructor.
     *
     * @param appName The name of the application.
     */
    public ServiceLookup(final String appName) {
        this(appName, new JNDI());
    }


    /**
     * Constructor.
     *
     * @param appName The name of the application.
     * @param providerUrl The java naming provider URL
     */
    public ServiceLookup(final String appName, final String providerUrl) {
        final Registry registry = new JNDI(providerUrl);
        _appName = appName;
        _registry = registry;
    }


    /** {@inheritDoc} */
    @Override
    public ResourcesExt getResources() {
        return _registry.<ResourcesExt>get(jndiPath(Resources.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public PagesExt getPages() {
        return _registry.<PagesExt>get(jndiPath(Pages.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public FoldersExt getFolders() {
        return _registry.<FoldersExt>get(jndiPath(Folders.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public Users getUsers() {
        return _registry.<Users>get(jndiPath(Users.NAME));
    }

    /** {@inheritDoc} */
    @Override
    public SearchEngine getSearch() {
        return _registry.<SearchEngine>get(jndiPath(SearchEngine.NAME));
    }


    /**
     * Look up the action scheduler.
     *
     * @return An action scheduler.
     */
    public Scheduler lookupActionScheduler() {
        return _registry.<Scheduler>get(jndiPath(ActionScheduler.NAME));
    }


    /**
     * Look up the search scheduler.
     *
     * @return A search scheduler.
     */
    public Scheduler lookupSearchScheduler() {
        return _registry.<Scheduler>get(jndiPath(SearchEngine.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public Templates getTemplates() {
        return _registry.<Templates>get(jndiPath(Templates.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public Actions getActions() {
        return _registry.<Actions>get(jndiPath(Actions.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public Files getFiles() {
        throw new UnsupportedOperationException("Method not implemented.");
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


    private String jndiPath(final String serviceName) {
        return _appName+"/"+serviceName+"/remote";
    }
}
