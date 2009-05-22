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

import ccc.domain.Action;
import ccc.services.Dao;


/**
 * Command: schedule an action.
 *
 * @author Civic Computing Ltd.
 */
public class ScheduleActionCommand {

    private final Dao _dao;

    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     */
    public ScheduleActionCommand(final Dao dao) {
        _dao = dao;
    }


    /**
     * Schedule an action.
     *
     * @param action The action to schedule
     */
    public void execute(final Action action) {
        _dao.create(action);

        // TODO: Audit this action.
    }
}
