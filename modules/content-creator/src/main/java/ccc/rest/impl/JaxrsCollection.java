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
import ccc.rest.Actions;
import ccc.rest.Aliases;
import ccc.rest.Files;
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
abstract class JaxrsCollection {

    private final Registry _reg = new JNDI();
    private final String _appName = CCCProperties.get("application.name");

    private Templates _templates;
    private ResourcesExt _resourcesExt;
    private PagesExt _pagesExt;
    private FoldersExt _foldersExt;
    private Users _userCommands;
    private Actions _actions;
    private Files _files;
    private Aliases _aliases;

    /**
     * Accessor.
     *
     * @return Returns the templates.
     */
    public final Templates getQueries() {
        return
            (null==_templates)
                ? (Templates) _reg.get(_appName+"/"+Templates.NAME+"/remote")
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

    /**
     * Accessor.
     *
     * @return Returns the commands.
     */
    public final ResourcesExt getCommands() {
        return
            (null==_resourcesExt)
                ? (ResourcesExt) _reg.get(_appName+"/"+ResourcesExt.NAME+"/remote")
                : _resourcesExt;
    }

    /**
     * Accessor.
     *
     * @return Returns the actions.
     */
    public final Actions getActions() {
        return
        (null==_actions)
        ? (Actions) _reg.get(_appName+"/"+Actions.NAME+"/local")
            : _actions;
    }

    /**
     * Accessor.
     *
     * @return Returns the commands.
     */
    public final PagesExt getPageCommands() {
        return
        (null==_pagesExt)
        ? (PagesExt) _reg.get(_appName+"/"+PagesExt.NAME+"/remote")
            : _pagesExt;
    }

    /**
     * Accessor.
     *
     * @return Returns the commands.
     */
    public final FoldersExt getFolderCommands() {
        return
        (null==_foldersExt)
        ? (FoldersExt) _reg.get(_appName+"/"+FoldersExt.NAME+"/remote")
            : _foldersExt;
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
     * Accessor.
     *
     * @return Returns the files.
     */
    public final Files getFiles() {
        return
        (null==_files)
        ? (Files) _reg.get(_appName+"/"+Files.NAME+"/local")
            : _files;
    }

    /**
     * Accessor.
     *
     * @return Returns the aliases.
     */
    public final Aliases getAliases() {
        return
        (null==_aliases)
        ? (Aliases) _reg.get(_appName+"/"+Aliases.NAME+"/remote")
            : _aliases;
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
}