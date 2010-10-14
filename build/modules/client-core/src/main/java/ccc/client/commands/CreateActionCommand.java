/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.client.commands;

import ccc.api.core.Action;
import ccc.api.core.ActionSummary;
import ccc.api.core.PagedCollection;
import ccc.api.types.DBC;
import ccc.api.types.Link;
import ccc.client.core.Callback;
import ccc.client.core.CallbackResponseHandler;
import ccc.client.core.Command;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.Parser;
import ccc.client.core.Request;
import ccc.client.core.Response;
import ccc.client.core.S11nHelper;


/**
 * Create an action.
 *
 * @author Civic Computing Ltd.
 */
public class CreateActionCommand
    extends
        Command<PagedCollection<ActionSummary>, Action> {

    private final Action _action;

    /**
     * Constructor.
     *
     * @param action The action to create.
     */
    public CreateActionCommand(final Action action) {
        _action = DBC.require().notNull(action);
    }

    /** {@inheritDoc} */
    @Override
    public void invoke(final PagedCollection<ActionSummary> subject,
                       final Callback<Action> callback) {

        final String path =
            getBaseUrl()
            + new Link(subject.getLink("self")).build(getEncoder());

        final Request r =
            new Request(
                HttpMethod.POST,
                path,
                writeAction(_action),
                new CallbackResponseHandler<Action>(
                    I18n.uiConstants.createAction(),
                    callback,
                    new Parser<Action>() {
                        @Override public Action parse(final Response response) {
                            return new S11nHelper().readAction(response);
                        }}));

        getExecutor().invokeRequest(r);
    }

}
