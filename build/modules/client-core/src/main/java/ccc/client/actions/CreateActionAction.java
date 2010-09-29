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
package ccc.client.actions;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import ccc.api.core.Action;
import ccc.api.types.CommandType;
import ccc.api.types.Link;
import ccc.client.core.CallbackResponseHandler;
import ccc.client.core.DefaultCallback;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.Parser;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;
import ccc.client.core.S11nHelper;
import ccc.client.events.Event;


/**
 * Create a scheduled action.
 *
 * @author Civic Computing Ltd.
 */
public final class CreateActionAction
    extends
        RemotingAction<Action> {

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
        super(I18n.UI_CONSTANTS.createAction());
        _resourceId = resourceId;
        _command = command;
        _executeAfter = executeAfter;
        _actionParameters = actionParameters;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return createAction(
            new Action(
                _resourceId, _command, _executeAfter, _actionParameters));
    }


    /**
     * Create a new action.
     *
     * @param action The action to create.
     *
     * @return The HTTP request to create the action.
     */
    public Request createAction(final Action action) {

        final String path =
            Globals.API_URL
            + new Link(InternalServices.ACTIONS.getLink("self"))
                .build(InternalServices.ENCODER);

        return
            new Request(
                HttpMethod.POST,
                path,
                writeAction(action),
                new CallbackResponseHandler<Action>(
                    I18n.UI_CONSTANTS.createAction(),
                    new DefaultCallback<Action>(I18n.UI_CONSTANTS.createAction()) {
                        @Override public void onSuccess(final Action result) {
                            final Event<CommandType> event =
                                new Event<CommandType>(CommandType.ACTION_CREATE);
                            InternalServices.REMOTING_BUS.fireEvent(event);
                        }},
                    new Parser<Action>() {

                        @Override
                        public Action parse(final Response response) {
                            return new S11nHelper().readAction(response);
                        }}));
    }
}