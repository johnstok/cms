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
import ccc.rest.extensions.PagesExt;
import ccc.rest.extensions.ResourcesExt;


/**
 * Helper class for implementing JAX-RS collections.
 *
 * @author Civic Computing Ltd.
 */
abstract class JaxrsCollection
    implements
        ServiceLocator {

    private final Registry _reg = new JNDI();
    @Context private ServletContext _sContext;

    private Templates _templates;
    private ResourcesExt _resourcesExt;
    private PagesExt _pagesExt;
    private FoldersExt _foldersExt;
    private Users _userCommands;
    private Actions _actions;
    private ActionScheduler _actionscheduler;
    private Files _files;
    private SearchEngine _search;
    private Aliases _aliases;
    private Comments _comments;
    private Groups _groups;


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


    /** {@inheritDoc} */
    @Override
    public final Templates getTemplates() {
        return
            (null==_templates)
                ? (Templates)
                        _reg.get(getAppName()+"/"+Templates.NAME+"/remote")
                : _templates;
    }

    /**
     * Mutator.
     *
     * @param queries The templates to set.
     */
    public final void setQueries(final Templates queries) {
        _templates = queries;
    }

    /** {@inheritDoc} */
    @Override
    public final ResourcesExt getResources() {
        return
            (null==_resourcesExt)
                ? (ResourcesExt) _reg.get(
                    getAppName()+"/"+Resources.NAME+"/remote")
                : _resourcesExt;
    }

    /** {@inheritDoc} */
    @Override
    public final Actions getActions() {
        return
        (null==_actions)
            ? (Actions) _reg.get(getAppName()+"/"+Actions.NAME+"/remote")
            : _actions;
    }


    /** {@inheritDoc} */
    @Override
    public final ActionScheduler lookupActionScheduler() {
        return
            (null==_actionscheduler)
                ? (ActionScheduler)
                    _reg.get(getAppName()+"/"+ActionScheduler.NAME+"/local")
                : _actionscheduler;
    }

    /** {@inheritDoc} */
    @Override
    public final PagesExt getPages() {
        return
        (null==_pagesExt)
            ? (PagesExt) _reg.get(getAppName()+"/"+Pages.NAME+"/remote")
            : _pagesExt;
    }

    /** {@inheritDoc} */
    @Override
    public final FoldersExt getFolders() {
        return
        (null==_foldersExt)
            ? (FoldersExt) _reg.get(getAppName()+"/"+Folders.NAME+"/remote")
            : _foldersExt;
    }

    /** {@inheritDoc} */
    @Override
    public final Users getUsers() {
        return
        (null==_userCommands)
            ? (Users) _reg.get(getAppName()+"/"+Users.NAME+"/remote")
            : _userCommands;
    }

    /** {@inheritDoc} */
    @Override
    public final Files getFiles() {
        return
        (null==_files)
            ? (Files) _reg.get(getAppName()+"/"+Files.NAME+"/local")
            : _files;
    }

    /** {@inheritDoc} */
    @Override
    public final SearchEngine getSearch() {
        return
        (null==_search)
            ? (SearchEngine) _reg.get(getAppName()+"/"+SearchEngine.NAME+"/local")
            : _search;
    }

    /** {@inheritDoc} */
    @Override
    public final Aliases getAliases() {
        return
            (null==_aliases)
                ? (Aliases) _reg.get(getAppName()+"/"+Aliases.NAME+"/remote")
                : _aliases;
    }

    /** {@inheritDoc} */
    @Override
    public Comments getComments() {
        return
        (null==_comments)
            ? (Comments) _reg.get(getAppName()+"/"+Comments.NAME+"/local")
            : _comments;
    }

    /** {@inheritDoc} */
    @Override
    public Groups getGroups() {
        return
        (null==_groups)
            ? (Groups) _reg.get(getAppName()+"/"+Groups.NAME+"/remote")
            : _groups;
    }

    /**
     * Mutator.
     *
     * @param resourcesExt The commands to set.
     */
    public final void setCommands(final ResourcesExt resourcesExt) {
        _resourcesExt = resourcesExt;
    }

    /**
     * Mutator.
     *
     * @param pagesExt The commands to set.
     */
    public final void setPageCommands(final PagesExt pagesExt) {
        _pagesExt = pagesExt;
    }

    /**
     * Mutator.
     *
     * @param foldersExt The commands to set.
     */
    public final void setFolderCommands(final FoldersExt foldersExt) {
        _foldersExt = foldersExt;
    }

    /**
     * Mutator.
     *
     * @param userCommands The commands to set.
     */
    public final void setUserCommands(final Users userCommands) {
        _userCommands = userCommands;
    }

    /**
     * Mutator.
     *
     * @param files The files to set.
     */
    public final void setFiles(final Files files) {
        _files = files;
    }

    /**
     * Mutator.
     *
     * @param aliases The aliases to set.
     */
    public final void setAliases(final Aliases aliases) {
        _aliases = aliases;
    }


    /** {@inheritDoc} */
    @Override
    public Security getSecurity() {
        throw new UnsupportedOperationException("Method not implemented.");
    }
}
