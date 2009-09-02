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
import ccc.rest.Commands;
import ccc.rest.FolderCommands;
import ccc.rest.PageCommands;
import ccc.rest.Queries;
import ccc.rest.UserCommands;


/**
 * Helper class for implementing JAX-RS collections.
 *
 * @author Civic Computing Ltd.
 */
public abstract class JaxrsCollection {

    private final Registry _reg = new JNDI();
    private final String _appName = CCCProperties.get("application.name");

    private Queries _queries;
    private Commands _commands;
    private PageCommands _pageCommands;
    private FolderCommands _folderCommands;
    private UserCommands _userCommands;

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
     * @return Returns the commands.
     */
    public final PageCommands getPageCommands() {
        return
        (null==_pageCommands)
        ? (PageCommands) _reg.get(_appName+"/"+PageCommands.NAME+"/remote")
            : _pageCommands;
    }

    /**
     * Accessor.
     *
     * @return Returns the commands.
     */
    public final FolderCommands getFolderCommands() {
        return
        (null==_folderCommands)
        ? (FolderCommands) _reg.get(_appName+"/"+FolderCommands.NAME+"/remote")
            : _folderCommands;
    }

    /**
     * Accessor.
     *
     * @return Returns the commands.
     */
    public final UserCommands getUserCommands() {
        return
        (null==_userCommands)
        ? (UserCommands) _reg.get(_appName+"/"+UserCommands.NAME+"/remote")
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
     * @param pageCommands The commands to set.
     */
    public final void setPageCommands(final PageCommands pageCommands) {
        _pageCommands = pageCommands;
    }

    /**
     * Mutator.
     *
     * @param folderCommands The commands to set.
     */
    public final void setFolderCommands(final FolderCommands folderCommands) {
        _folderCommands = folderCommands;
    }

    /**
     * Mutator.
     *
     * @param userCommands The commands to set.
     */
    public final void setUserCommands(final UserCommands userCommands) {
        _userCommands = userCommands;
    }

}