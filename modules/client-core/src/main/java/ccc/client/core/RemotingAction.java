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

package ccc.client.core;

import ccc.api.types.CommandType;
import ccc.api.types.DBC;
import ccc.api.types.Link.Encoder;
import ccc.client.events.Bus;
import ccc.client.events.Event;


/**
 * An action that makes a server-side call.
 *
 * @param <T> The type of the return value.
 *
 * @author Civic Computing Ltd.
 */
public abstract class RemotingAction<T>
    extends
        S11nHelper
    implements
        Action {

    private String            _actionName;
    private HttpMethod        _method;

    private RequestExecutor   _executor    = InternalServices.executor;
    private Encoder           _encoder     = InternalServices.encoder;
    private Bus<CommandType>  _bus         = InternalServices.remotingBus;


    /**
     * Constructor.
     *
     * @param actionName The name of this action.
     */
    public RemotingAction(final String actionName) {
        _actionName = actionName;
        _method = HttpMethod.GET;
    }


    /**
     * Constructor.
     *
     * @param actionName The name of this action.
     * @param method The HTTP method to use.
     */
    public RemotingAction(final String actionName, final HttpMethod method) {
        _actionName = actionName;
        _method = method;
    }


    /** {@inheritDoc} */
    @Override
    public void execute() {
        execute(new Callback<T>() {
            @Override public void onSuccess(final T result) {
                RemotingAction.this.onSuccess(result);
            }

            @Override
            public void onFailure(final Throwable caught) {
                RemotingAction.this.onFailure(caught);
            }});
    }


    /**
     * Execute this action handling the response with the specified callback.
     *
     * @param callback The callback to handle the response.
     */
    public void execute(final Callback<T> callback) {
        if (!beforeExecute()) { return; }
        _executor.invokeRequest(getRequest(callback));
    }


    /**
     * Mutator.
     *
     * @param executor The executor to set.
     *
     * @return A reference to 'this'.
     */
    public RemotingAction<T> setExecutor(final RequestExecutor executor) {
        _executor = DBC.require().notNull(executor);
        return this;
    }


    /**
     * Get the HTTP request for this action.
     *
     * @param callback the callback the request will notify.
     *
     * @return The request for this remote action.
     */
    protected Request getRequest(final Callback<T> callback) {
        return
            new Request(
                _method,
                Globals.API_URL + getPath(),
                getBody(),
                new CallbackResponseHandler<T>(_actionName,
                                               callback,
                                               new Parser<T>() {
                    @Override public T parse(final Response response) {
                        return RemotingAction.this.parse(response);
                    }}));
    }


    /**
     * Parse a response.
     *
     * @param response The response to parse.
     *
     * @return The parsed result.
     */
    @Deprecated
    protected T parse(final Response response) {
        throw new UnsupportedOperationException("You must override parse()");
    }


    /**
     * Handler method called immediately before an action is executed.
     * <p>You can override this method to present confirmation dialogs, etc. to
     * users, when an action is invoked. If this method returns true the action
     * will continue; if false is returned the action will not be executed.
     *
     * @return True if the action should continue false otherwise.
     */
    protected boolean beforeExecute() {
        return true;
    }

    /**
     * Handle failure.
     *
     * @param throwable The throwable.
     */
    @Deprecated
    protected void onFailure(final Throwable throwable) {
        InternalServices.exHandler.unexpectedError(throwable, getActionName());
    }


    /**
     * Provide a body for this method.
     * <p><i>Only called for POST requests</i>.
     *
     * @return The request body, as a string.
     */
    @Deprecated
    protected String getBody() { return ""; }


    /**
     * Accessor.
     *
     * @return Returns the actionName.
     */
    @Deprecated
    public final String getActionName() { return _actionName; }


    /**
     * Determine the server path for this action.
     *
     * @return The server path for the resource.
     */
    @Deprecated
    protected String getPath() {
        throw new UnsupportedOperationException(
            "You must override getPath().");
    }


    /**
     * Handle the response from a successful invocation.
     *
     * @param response The server response.
     */
    @Deprecated
    protected void onSuccess(final T response) {
        throw new UnsupportedOperationException(
            "You must override onSuccess().");
    }


    /**
     * URL encode a string.
     *
     * @param string The string to encode.
     *
     * @return The encoded string.
     */
    protected String encode(final String string) {
        return _encoder.encode(string);
    }


    /**
     * Submit an event to the event bus.
     *
     * @param event The event to submit.
     */
    protected void fireEvent(final Event<CommandType> event) {
        _bus.fireEvent(event);
    }
}
