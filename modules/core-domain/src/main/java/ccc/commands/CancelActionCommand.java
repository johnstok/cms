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
package ccc.commands;

import java.util.UUID;

import ccc.domain.Action;
import ccc.services.Dao;


/**
 * Command: cancel an action.
 *
 * @author Civic Computing Ltd.
 */
public class CancelActionCommand {

    private final Dao _dao;

    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     */
    public CancelActionCommand(final Dao dao) {
        _dao = dao;
    }


    /**
     * Cancel an action.
     *
     * @param actionId The id of the action to cancel.
     */
    public void execute(final UUID actionId) {
        _dao.find(Action.class, actionId).cancel();

        // TODO: Audit this action.
    }
}
