/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.actions.remote;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.RemotingAction;
import ccc.serialization.JsonKeys;
import ccc.types.CommandType;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Create a scheduled action.
 *
 * @author Civic Computing Ltd.
 */
public class CreateActionAction
    extends
        RemotingAction {

    private UUID _resourceId;
    private CommandType _command;
    private Date _executeAfter;
    private Map<String, String> _actionParameters;


    /**
     * Constructor.
     * @param actionParameters Additional parameters for the action.
     * @param executeAfter The date that the action will be performed.
     * @param command The command the action will invoke.
     * @param resourceId The resource the action will operate on.
     */
    public CreateActionAction(final UUID resourceId,
                               final CommandType command,
                               final Date executeAfter,
                               final Map<String, String> actionParameters) {
        super(UI_CONSTANTS.createAction(), RequestBuilder.POST);
        _resourceId = resourceId;
        _command = command;
        _executeAfter = executeAfter;
        _actionParameters = actionParameters;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/actions";
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        json.set(JsonKeys.SUBJECT_ID, _resourceId);
        json.set(JsonKeys.COMMAND, _command.name());
        json.set(JsonKeys.EXECUTE_AFTER, _executeAfter);
        json.set(JsonKeys.PARAMETERS, _actionParameters);
        return json.toString();
    }
}
