/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rest.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.CommandType;


/**
 * A new action.
 *
 * @author Civic Computing Ltd.
 */
public class ActionDto implements Jsonable, Serializable {

    private final UUID _resourceId;
    private final CommandType _command;
    private final Date _executeAfter;
    private final Map<String, String> _parameters;


    /**
     * Constructor.
     *
     * @param resourceId The subject of the action.
     * @param command The command to perform.
     * @param executeAfter The earliest time the action may be performed.
     * @param parameters Additional parameters for the action.
     */
    public ActionDto(final UUID resourceId,
                     final CommandType command,
                     final Date executeAfter,
                     final Map<String, String> parameters) {
        _resourceId = resourceId;
        _command = command;
        _executeAfter = new Date(executeAfter.getTime());
        _parameters = parameters;
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
        return new Date(_executeAfter.getTime());
    }


    /**
     * Accessor.
     *
     * @return Returns the parameters.
     */
    public final Map<String, String> getParameters() {
        return _parameters;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.SUBJECT_ID, _resourceId);
        json.set(JsonKeys.COMMAND, _command.name());
        json.set(JsonKeys.EXECUTE_AFTER, _executeAfter);
        json.set(JsonKeys.PARAMETERS, _parameters);
    }
}
