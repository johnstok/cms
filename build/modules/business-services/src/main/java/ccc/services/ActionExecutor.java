/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services;

import ccc.actions.Action;


/**
 * API for executing CCC actions.
 *
 * @author Civic Computing Ltd.
 */
public interface ActionExecutor {

    /** NAME : String. */
    String NAME = "ActionExecutor";


    /**
     * Execute the specified action.
     *
     * @param action The action to execute.
     */
    void executeAction(final Action action);
}
