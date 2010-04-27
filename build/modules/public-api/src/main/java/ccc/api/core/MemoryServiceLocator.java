/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.api.core;


/**
 * Service locator that stores references to services in memory.
 *
 * @author Civic Computing Ltd.
 */
public class MemoryServiceLocator
    implements
        ServiceLocator {

    private Templates       _templates;
    private Resources       _resources;
    private Pages           _pages;
    private Folders         _folders;
    private Users           _userCommands;
    private Actions         _actions;
    private ActionScheduler _actionscheduler;
    private Files           _files;
    private SearchEngine    _search;
    private Aliases         _aliases;
    private Comments        _comments;
    private Groups          _groups;


    /** {@inheritDoc} */
    @Override
    public Actions getActions() { return _actions; }


    /** {@inheritDoc} */
    @Override
    public Files getFiles() { return _files; }


    /** {@inheritDoc} */
    @Override
    public Folders getFolders() { return _folders; }


    /** {@inheritDoc} */
    @Override
    public Pages getPages() { return _pages; }


    /** {@inheritDoc} */
    @Override
    public Resources getResources() { return _resources; }


    /** {@inheritDoc} */
    @Override
    public Templates getTemplates() { return _templates; }


    /** {@inheritDoc} */
    @Override
    public Users getUsers() { return _userCommands; }


    /** {@inheritDoc} */
    @Override
    public SearchEngine getSearch() { return _search; }


    /** {@inheritDoc} */
    @Override
    public Aliases getAliases() { return _aliases; }


    /** {@inheritDoc} */
    @Override
    public Security getSecurity() { throw new UnsupportedOperationException(); }


    /** {@inheritDoc} */
    @Override
    public Comments getComments() { return _comments; }


    /** {@inheritDoc} */
    @Override
    public Groups getGroups() { return _groups; }


    /** {@inheritDoc} */
    @Override
    public ActionScheduler lookupActionScheduler() { return _actionscheduler; }


    /**
     * Mutator.
     *
     * @param queries The templates to set.
     */
    public final void setQueries(final Templates queries) {
        _templates = queries;
    }


    /**
     * Mutator.
     *
     * @param resources The resources to set.
     */
    public final void setCommands(final Resources resources) {
        _resources = resources;
    }


    /**
     * Mutator.
     *
     * @param pages The pages implementation to set.
     */
    public final void setPageCommands(final Pages pages) {
        _pages = pages;
    }


    /**
     * Mutator.
     *
     * @param folders The folders to set.
     */
    public final void setFolderCommands(final Folders folders) {
        _folders = folders;
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


    /**
     * Mutator.
     *
     * @param actions The actions to set.
     */
    public void setActions(final Actions actions) {
        _actions = actions;
    }


    /**
     * Mutator.
     *
     * @param search The search to set.
     */
    public void setSearch(final SearchEngine search) {
        _search = search;
    }


    /**
     * Mutator.
     *
     * @param comments The comments to set.
     */
    public void setComments(final Comments comments) {
        _comments = comments;
    }


    /**
     * Mutator.
     *
     * @param groups The groups to set.
     */
    public void setGroups(final Groups groups) {
        _groups = groups;
    }
}
