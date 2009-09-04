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

package ccc.rest.impl;

import ccc.commons.CCCProperties;
import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.rest.ActionDao;
import ccc.rest.Commands;
import ccc.rest.Folders;
import ccc.rest.Pages;
import ccc.rest.Queries;
import ccc.rest.Users;


/**
 * Helper class for implementing JAX-RS collections.
 *
 * @author Civic Computing Ltd.
 */
abstract class JaxrsCollection {

    private final Registry _reg = new JNDI();
    private final String _appName = CCCProperties.get("application.name");

    private Queries _queries;
    private Commands _commands;
    private Pages _pages;
    private Folders _folders;
    private Users _userCommands;
    private ActionDao _actions;

    /**
     * Accessor.
     *
     * @return Returns the queries.
     */
    public final Queries getQueries() {
        return
            (null==_queries)
                ? (Queries) _reg.get(_appName+"/"+Queries.NAME+"/remote")
                : _queries;
    }

    /**
     * Mutator.
     *
     * @param queries The queries to set.
     */
    public final void setQueries(final Queries queries) {
        _queries = queries;
    }

    /**
     * Accessor.
     *
     * @return Returns the commands.
     */
    public final Commands getCommands() {
        return
            (null==_commands)
                ? (Commands) _reg.get(_appName+"/"+Commands.NAME+"/remote")
                : _commands;
    }

    /**
     * Accessor.
     *
     * @return Returns the actions.
     */
    public final ActionDao getActions() {
        return
        (null==_actions)
        ? (ActionDao) _reg.get(_appName+"/"+ActionDao.NAME+"/local")
            : _actions;
    }

    /**
     * Accessor.
     *
     * @return Returns the commands.
     */
    public final Pages getPageCommands() {
        return
        (null==_pages)
        ? (Pages) _reg.get(_appName+"/"+Pages.NAME+"/remote")
            : _pages;
    }

    /**
     * Accessor.
     *
     * @return Returns the commands.
     */
    public final Folders getFolderCommands() {
        return
        (null==_folders)
        ? (Folders) _reg.get(_appName+"/"+Folders.NAME+"/remote")
            : _folders;
    }

    /**
     * Accessor.
     *
     * @return Returns the commands.
     */
    public final Users getUserCommands() {
        return
        (null==_userCommands)
        ? (Users) _reg.get(_appName+"/"+Users.NAME+"/remote")
            : _userCommands;
    }

    /**
     * Mutator.
     *
     * @param commands The commands to set.
     */
    public final void setCommands(final Commands commands) {
        _commands = commands;
    }

    /**
     * Mutator.
     *
     * @param pages The commands to set.
     */
    public final void setPageCommands(final Pages pages) {
        _pages = pages;
    }

    /**
     * Mutator.
     *
     * @param folders The commands to set.
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

}