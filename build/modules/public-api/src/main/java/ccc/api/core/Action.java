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

import ccc.api.types.CommandType;
import ccc.api.types.SortOrder;
import ccc.api.types.URIBuilder;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Jsonable2;


/**
 * A new action.
 *
 * @author Civic Computing Ltd.
 */
public class Action
    implements
        Jsonable2,
        Serializable {

    static final String  COLLECTION  = "/secure/actions";
    static final String  ELEMENT     = COLLECTION + "/{id}";
    static final String  PENDING     = COLLECTION + "/pending";
    static final String  COMPLETED   = COLLECTION + "/completed";
    static final String  EXECUTE     = COLLECTION + "/all";
    static final String  CANCEL      = ELEMENT + "/cancel";

    private UUID                _resourceId;
    private CommandType         _command;
    private Date                _executeAfter;
    private Map<String, String> _parameters = new HashMap<String, String>();


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


    /** {@inheritDoc} */
    @Override
    public void fromJson(final Json json) {
        _resourceId = json.getId(JsonKeys.SUBJECT_ID);
        _command = CommandType.valueOf(json.getString(JsonKeys.COMMAND));
        _executeAfter = json.getDate(JsonKeys.EXECUTE_AFTER);
        _parameters = json.getStringMap(JsonKeys.PARAMETERS);
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public static String list() {
        return Action.COLLECTION;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param id
     * @return
     */
    public static String cancel(final UUID id) {
        return
            new URIBuilder(Action.CANCEL)
            .replace("id", id.toString())
            .toString();
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param page
     * @param count
     * @param order
     * @param sort
     * @return
     */
    public static String pending(final int page,
                                 final int count,
                                 final SortOrder order,
                                 final String sort) {
        return
            Action.PENDING
            + "?page="+page
            + "&count="+count
            + ((null==order) ? "" : "&order="+order.name())
            + ((null==sort) ? "" : "&sort="+sort);
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param page
     * @param count
     * @param order
     * @param sort
     * @return
     */
    public static String completed(final int page,
                                   final int count,
                                   final SortOrder order,
                                   final String sort) {
        return
            Action.COMPLETED
            + "?page="+page
            + "&count="+count
            + ((null==order) ? "" : "&order="+order.name())
            + ((null==sort) ? "" : "&sort="+sort);
    }
}
