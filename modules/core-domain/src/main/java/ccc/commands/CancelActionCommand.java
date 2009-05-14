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
 * TODO: Add Description for this type.
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
     * TODO: Add a description of this method.
     *
     * @param action
     */
    public void execute(final UUID actionId) {
        _dao.find(Action.class, actionId).cancel();

        // TODO: Audit this action.
    }
}
