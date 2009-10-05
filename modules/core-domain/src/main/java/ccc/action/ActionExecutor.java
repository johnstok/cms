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
package ccc.action;

import java.util.UUID;

import ccc.rest.RestException;


/**
 * API for executing CCC actions.
 *
 * @author Civic Computing Ltd.
 */
public interface ActionExecutor {

    /**
     * Execute the specified action.
     *
     * @param actionId The action to execute.
     *
     * @throws RestException If the action fails.
     */
    void executeAction(UUID actionId) throws RestException;
}
