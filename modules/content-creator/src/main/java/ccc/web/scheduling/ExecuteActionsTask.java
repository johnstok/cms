/*-----------------------------------------------------------------------------
 * Copyright © 2011 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.web.scheduling;

import ccc.api.core.Actions2;
import ccc.api.types.DBC;


/**
 * A runnable that executes all outstanding actions.
 *
 * @author Civic Computing Ltd.
 */
public class ExecuteActionsTask
    implements
        Runnable {

    private final Actions2 _actions;


    /**
     * Constructor.
     *
     * @param actions The actions service to invoke.
     */
    public ExecuteActionsTask(final Actions2 actions) {
        _actions = DBC.require().notNull(actions);
    }


    /** {@inheritDoc} */
    @Override
    public void run() { _actions.executeAll(); }
}
