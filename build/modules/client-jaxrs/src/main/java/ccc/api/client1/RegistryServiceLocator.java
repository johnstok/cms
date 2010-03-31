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
package ccc.api.client1;

import ccc.commons.JNDI;
import ccc.commons.Registry;
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
import ccc.rest.extensions.FoldersExt;
import ccc.rest.extensions.ResourcesExt;


/**
 * A class to perform application name specific Registry lookups.
 *
 * @author Civic Computing Ltd.
 */
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
        _appName = appName;
        _registry = registry;
    }


    /**
     * Constructor.
     *
     * @param appName The name of the application.
     */
    public RegistryServiceLocator(final String appName) {
        this(appName, new JNDI());
    }


    /**
     * Constructor.
     *
     * @param appName The name of the application.
     * @param providerUrl The java naming provider URL
     */
    public RegistryServiceLocator(final String appName,
                                  final String providerUrl) {
        final Registry registry = new JNDI(providerUrl);
        _appName = appName;
        _registry = registry;
    }


    /** {@inheritDoc} */
    @Override
    public ResourcesExt getResources() {
        return _registry.<ResourcesExt>get(remotePath(Resources.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public Pages getPages() {
        return _registry.<Pages>get(localPath(Pages.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public FoldersExt getFolders() {
        return _registry.<FoldersExt>get(remotePath(Folders.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public Users getUsers() {
        return _registry.<Users>get(remotePath(Users.NAME));
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
        return _registry.<Templates>get(remotePath(Templates.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public Actions getActions() {
        return _registry.<Actions>get(remotePath(Actions.NAME));
    }


    /** {@inheritDoc} */
    @Override
    public Groups getGroups() {
        return _registry.<Groups>get(remotePath(Groups.NAME));
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


    private String remotePath(final String serviceName) {
        return _appName+"/"+serviceName+"/remote";
    }


    private String localPath(final String serviceName) {
        return _appName+"/"+serviceName+"/local";
    }
}
