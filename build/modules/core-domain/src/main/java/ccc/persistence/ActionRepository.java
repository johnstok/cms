/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.persistence;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import ccc.domain.Action;
import ccc.domain.EntityNotFoundException;


/**
 * Repository for actions.
 *
 * @author Civic Computing Ltd.
 */
public interface ActionRepository {

    /**
     * Retrieve the latest actions.
     *
     * @param until Cut off date.
     *
     * @return All SCHEDULED actions with an execute_after date less than the
     *  specified date.
     */
    List<Action> latest(Date until);

    /**
     * Retrieve all actions in the status SCHEDULED.
     *
     * @return A list of actions.
     */
    List<Action> pending();

    /**
     * Retrieve all actions not in the status SCHEDULED.
     *
     * @return A list of actions.
     */
    List<Action> completed();

    /**
     * Find an action from its ID.
     *
     * @param actionId The action's ID.
     *
     * @throws EntityNotFoundException If no action exists with the specified
     *  ID.
     *
     * @return The corresponding action.
     */
    Action find(UUID actionId) throws EntityNotFoundException;

    /**
     * Persist a newly created action.
     *
     * @param action The action to persist.
     */
    void create(Action action);

}
