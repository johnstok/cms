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
package ccc.api.core;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ccc.api.types.ActionStatus;
import ccc.api.types.CommandType;


/**
 * A new action.
 *
 * @author Civic Computing Ltd.
 */
public class Action
    implements
        Serializable {

    private UUID                _id;
    private UUID                _resourceId;
    private CommandType         _command;
    private Date                _executeAfter;
    private Map<String, String> _parameters = new HashMap<String, String>();
    private UUID                _actor;
    private ActionStatus        _status;
    private Failure             _failure;


    /**
     * Constructor.
     */
    public Action() { super(); }


    /**
     * Constructor.
     *
     * @param resourceId The subject of the action.
     * @param command The command to perform.
     * @param executeAfter The earliest time the action may be performed.
     * @param parameters Additional parameters for the action.
     */
    public Action(final UUID resourceId,
                  final CommandType command,
                  final Date executeAfter,
                  final Map<String, String> parameters) {
        _resourceId = resourceId;
        _command = command;
        _executeAfter = new Date(executeAfter.getTime());
        _parameters.putAll(parameters);
    }


    /**
     * Accessor.
     *
     * @return Returns the resourceId.
     */
    public final UUID getResourceId() {
        return _resourceId;
    }


    /**
     * Accessor.
     *
     * @return Returns the action.
     */
    public final CommandType getCommand() {
        return _command;
    }


    /**
     * Accessor.
     *
     * @return Returns the executeAfter.
     */
    public final Date getExecuteAfter() {
        return (null==_executeAfter) ? null : new Date(_executeAfter.getTime());
    }


    /**
     * Accessor.
     *
     * @return Returns the parameters.
     */
    public final Map<String, String> getParameters() {
        return _parameters;
    }


    /**
     * Accessor.
     *
     * @return Returns the actor.
     */
    public final UUID getActor() {
        return _actor;
    }


    /**
     * Mutator.
     *
     * @param actor The actor to set.
     */
    public final void setActor(final UUID actor) {
        _actor = actor;
    }


    /**
     * Accessor.
     *
     * @return Returns the status.
     */
    public final ActionStatus getStatus() {
        return _status;
    }


    /**
     * Mutator.
     *
     * @param status The status to set.
     */
    public final void setStatus(final ActionStatus status) {
        _status = status;
    }


    /**
     * Accessor.
     *
     * @return Returns the failure.
     */
    public final Failure getFailure() {
        return _failure;
    }


    /**
     * Mutator.
     *
     * @param failure The failure to set.
     */
    public final void setFailure(final Failure failure) {
        _failure = failure;
    }


    /**
     * Accessor.
     *
     * @return Returns the id.
     */
    public UUID getId() {
        return _id;
    }


    /**
     * Mutator.
     *
     * @param id The id to set.
     */
    public void setId(final UUID id) {
        _id = id;
    }
}
