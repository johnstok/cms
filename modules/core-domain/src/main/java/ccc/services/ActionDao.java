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

import java.util.Collection;
import java.util.UUID;

import ccc.actions.Action;


/**
 * API for managing actions.
 *
 * @author Civic Computing Ltd.
 */
public interface ActionDao {

    /** NAME : String. */
    String NAME = "ActionDao";

    /**
     * Schedule a new action..
     *
     * @param action The new action.
     */
    void schedule(Action action);

    /**
     * Retrieve a list of actions that are waiting to be executed.
     *
     * @return The list of actions.
     */
    Collection<Action> pending();

    /**
     * Retrieve a list of actions that have been executed.
     *
     * @return The list of actions.
     */
    Collection<Action> executed();

    /**
     * Cancel an action.
     *
     * @param actionId The id of the action to cancel.
     */
    void cancel(UUID actionId);

    /**
     * Execute the next available action.
     */
    void executeAction();
}
