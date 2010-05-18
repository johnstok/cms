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

package ccc.client.gwt.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import ccc.api.core.ActionSummary;
import ccc.api.core.ResourceSummary;
import ccc.api.types.DBC;
import ccc.client.gwt.remoting.TextParser;
import ccc.client.gwt.widgets.ContentCreator;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.json.ActionSummarySerializer;
import ccc.plugins.s11n.json.ResourceSummarySerializer;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;


/**
 * An action that makes a server-side call.
 *
 * @author Civic Computing Ltd.
 */
public abstract class RemotingAction
    implements
        Action {

    private String          _actionName;
    private HttpMethod      _method;
    private RequestExecutor _executor = new GwtRequestExecutor();
    private TextParser      _parser   = new GWTTextParser();


    /**
     * Constructor.
     */
    public RemotingAction() { super(); }


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
        if (!beforeExecute()) { return; }
        _executor.invokeRequest(getRequest());
    }


    /**
     * Mutator.
     *
     * @param executor The executor to set.
     *
     * @return A reference to 'this'.
     */
    public RemotingAction setExecutor(final RequestExecutor executor) {
        _executor = DBC.require().notNull(executor);
        return this;
    }


    /**
     * Accessor.
     *
     * @return Returns the parser.
     */
    public TextParser getParser() {
        return _parser;
    }


    /**
     * Mutator.
     *
     * @param parser The parser to set.
     *
     * @return A reference to 'this'.
     */
    public RemotingAction setParser(final TextParser parser) {
        _parser = DBC.require().notNull(parser);
        return this;
    }


    /**
     * Get the HTTP request for this action.
     *
     * @return The request for this remote action.
     */
    protected Request getRequest() {
        return
            new Request(
                _method,
                Globals.API_URL + getPath(),
                getBody(),
                new ResponseHandlerAdapter(_actionName) {


                    /** {@inheritDoc} */
                    @Override
                    public void onNoContent(final ccc.client.gwt.core.Response response) {
                        RemotingAction.this.onNoContent(response);
                    }


                    /** {@inheritDoc} */
                    @Override
                    public void onOK(final ccc.client.gwt.core.Response response) {
                        RemotingAction.this.onOK(response);
                    }
                });
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
    protected void onFailure(final Throwable throwable) {
        new GlobalsImpl().unexpectedError(throwable, getActionName());
    }


    private void onUnsupported(final ccc.client.gwt.core.Response response) {
        onFailure(
            new RuntimeException(// TODO Add UnsupportedResponseException
                "Unsupported response: "
                + response.getStatusCode() + " "
                + response.getStatusText()));
    }


    /**
     * Provide a body for this method.
     * <p><i>Only called for POST requests</i>.
     *
     * @return The request body, as a string.
     */
    protected String getBody() { return ""; }


    /**
     * Accessor.
     *
     * @return Returns the actionName.
     */
    public final String getActionName() { return _actionName; }


    /**
     * Determine the server path for this action.
     *
     * @return The server path for the resource.
     */
    protected String getPath() {
        throw new UnsupportedOperationException("You must override getPath().");
    }


    /**
     * Handle a '204 NO CONTENT' response from the remote server.
     *
     * @param response The server response.
     */
    protected void onNoContent(final ccc.client.gwt.core.Response response) {
        onUnsupported(response);
    }


    /**
     * Handle a '200 OK' response from the remote server.
     *
     * @param response The server response.
     */
    protected void onOK(final ccc.client.gwt.core.Response response) {
        onUnsupported(response);
    }


    /**
     * URL encode a string.
     *
     * @param string The string to encode.
     *
     * @return The encoded string.
     */
    protected String encode(final String string) {
        return URL.encodeComponent(string);
    }


    /**
     * Parse the response as a resource summary.
     *
     * @param response The response to parse.
     *
     * @return The resource summary.
     */
    protected ResourceSummary parseResourceSummary(final ccc.client.gwt.core.Response response) {
        return
            new ResourceSummarySerializer().read(
                new GwtJson(
                    JSONParser.parse(response.getText()).isObject()));
    }


    /**
     * Parse the response as a list of strings.
     *
     * @param response The response to parse.
     *
     * @return The list of strings.
     */
    protected List<String> parseListString(final Response response) {
        final List<String> strings = new ArrayList<String>();
        final JSONArray result = JSONParser.parse(response.getText()).isArray();
        for (int i=0; i<result.size(); i++) {
            strings.add(result.get(i).isString().stringValue());
        }
        return strings;
    }


    /**
     * Parse the response as a boolean.
     *
     * @param response The response to parse.
     *
     * @return A boolean.
     */
    protected boolean parseBoolean(final ccc.client.gwt.core.Response response) {
        final JSONBoolean b = JSONParser.parse(response.getText()).isBoolean();
        return b.booleanValue();
    }


    /**
     * Parse the response as a collection of action summaries.
     *
     * @param response The response to parse.
     *
     * @return A collection of action summaries.
     */
    protected Collection<ActionSummary> parseActionSummaryCollection(
                                                      final Response response) {
        final JSONArray result = JSONParser.parse(response.getText()).isArray();
        final Collection<ActionSummary> actions =
            new ArrayList<ActionSummary>();
        for (int i=0; i<result.size(); i++) {
            actions.add(
                new ActionSummarySerializer().read(
                    new GwtJson(result.get(i).isObject())));
        }
        return actions;
    }


    /**
     * Parse the response as a map.
     *
     * @param response The response to parse.
     * @return A map.
     */
    protected Map<String, String> parseMapString(final ccc.client.gwt.core.Response response) {
        final JSONValue value = JSONParser.parse(response.getText());
        final JSONObject result = value.isObject();
        final Json json = new GwtJson(result);

        return json.getStringMap("properties");
    }


    /**
     * Submit an event to the event bus.
     *
     * @param event The event to submit.
     */
    protected void fireEvent(final GwtEvent<?> event) {
        ContentCreator.EVENT_BUS.fireEvent(event);
    }
}
