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
public class ActionDto implements Jsonable {

    private final UUID _resourceId;
    private final CommandType _action;
    private final Date _executeAfter;
    private final Map<String, String> _parameters;


    /**
     * Constructor.
     *
     * @param resourceId The subject of the action.
     * @param action The command to perform.
     * @param executeAfter The earliest time the action may be performed.
     * @param parameters Additional parameters for the action.
     */
    public ActionDto(final UUID resourceId,
                     final CommandType action,
                     final Date executeAfter,
                     final Map<String, String> parameters) {
        _resourceId = resourceId;
        _action = action;
        _executeAfter = executeAfter;
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
    public final CommandType getAction() {
        return _action;
    }


    /**
     * Accessor.
     *
     * @return Returns the executeAfter.
     */
    public final Date getExecuteAfter() {
        return _executeAfter;
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
        json.set(JsonKeys.ACTION, _action.name());
        json.set(JsonKeys.EXECUTE_AFTER, _executeAfter);
        json.set(JsonKeys.PARAMETERS, _parameters);
    }
}
