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

import java.util.Map;
import java.util.UUID;

import ccc.rest.RestException;
import ccc.types.CommandType;


/**
 * API for executing CCC actions.
 *
 * @author Civic Computing Ltd.
 */
public interface ActionExecutor {

    /**
     * Execute the specified action.
     *
     * @param subjectId The resource the action will operate on.
     * @param actorId The actor to perform the action.
     * @param command The command to perform.
     * @param params Parameters for the action.
     *
     * @throws RestException If the action fails.
     */
    void executeAction(UUID subjectId,
                       UUID actorId,
                       CommandType command,
                       Map<String, String> params) throws RestException;
}
