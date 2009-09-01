/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.contentcreator.actions;

import static com.google.gwt.http.client.Response.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.GwtJson;
import ccc.contentcreator.client.RemoteException;
import ccc.contentcreator.client.SessionTimeoutException;
import ccc.contentcreator.overlays.FailureOverlay;
import ccc.rest.dto.ActionSummary;
import ccc.rest.dto.ResourceSummary;
import ccc.serialization.Json;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.http.client.RequestBuilder.Method;
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

    private final String _actionName;
    private final Method _method;
    private final boolean _isSecure;
    private static final int MS_IE6_1223 = 1223;
    private static final int SC_IM_A_TEAPOT = 418;

    /**
     * Constructor.
     *
     * @param actionName The name of this action.
     */
    public RemotingAction(final String actionName) {
        _actionName = actionName;
        _method = RequestBuilder.GET;
        _isSecure = true;
    }

    /**
     * Constructor.
     *
     * @param actionName The name of this action.
     * @param method The HTTP method to use.
     */
    public RemotingAction(final String actionName, final Method method) {
        _actionName = actionName;
        _method = method;
        _isSecure = true;
    }

    /**
     * Constructor.
     *
     * @param actionName The name of this action.
     * @param method The HTTP method to use.
     * @param isSecure Can this method only be called with a valid user session.
     */
    public RemotingAction(final String actionName,
                          final Method method,
                          final boolean isSecure) {
        _actionName = actionName;
        _method = method;
        _isSecure = isSecure;
    }

    /** {@inheritDoc} */
    @Override
    public void execute() {
        final String url = GLOBALS.apiURL(_isSecure) + getPath();
        final RequestBuilder builder = new RequestBuilder(_method, url);
        builder.setHeader("Accept", "application/json");
        if (RequestBuilder.POST.equals(_method)) {
            builder.setHeader("Content-Type", "application/json");
            final String body = getBody();
            builder.setRequestData(body);
        }
        builder.setCallback(new RequestCallback() {

            public void onError(final Request request,
                                final Throwable exception) {
                onFailure(exception);
            }

            public void onResponseReceived(final Request request,
                                           final Response response) {
                if (SessionTimeoutException
                                        .isTimeoutMessage(response.getText())) {
                    onFailure(
                        new SessionTimeoutException(response.getText()));
                } else if (SC_IM_A_TEAPOT == response.getStatusCode()) {
                    onCccException(response);
                } else if (SC_OK == response.getStatusCode()) {
                    onOK(response);
                } else if (SC_NO_CONTENT == response.getStatusCode()
                           || MS_IE6_1223 == response.getStatusCode()) {
                    onNoContent(response);
                } else {
                    onFailure(
                        new Exception(// TODO: Use a subclass of exception.
                            "Invalid response: "
                            + response.getStatusCode()+" "
                            + response.getStatusText()));
                }
            }

        });

        try {
            builder.send();
        } catch (final RequestException e) {
            GLOBALS.unexpectedError(e, getActionName());
        }
    }


    /**
     * Handle failure.
     *
     * @param throwable The throwable.
     */
    protected void onFailure(final Throwable throwable) {
        GLOBALS.unexpectedError(throwable, getActionName());
    }


    private void onUnsupported(final Response response) {
        onFailure(
            new RuntimeException(// TODO Add UnsupportedResponseException
                "Unsupported response: "
                + response.getStatusCode() + " "
                + response.getStatusText()));
    }


    private void onCccException(final Response response) {
        onFailure(
            new RemoteException(FailureOverlay.fromJson(response.getText())));
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
    protected abstract String getPath();

    /**
     * Handle a '204 NO CONTENT' response from the remote server.
     *
     * @param response The server response.
     */
    protected void onNoContent(final Response response) {
        onUnsupported(response);
    }

    /**
     * Handle a '200 OK' response from the remote server.
     *
     * @param response The server response.
     */
    protected void onOK(final Response response) {
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


    // 405 Method Not Allowed
    // protected abstract void onMethodNotAllowed(final Response response);


    // 400 Bad Request
    // protected abstract void onBadRequest(final Response response);


    /**
     * Parse the response as a resource summary.
     *
     * @param response The response to parse.
     *
     * @return The resource summary.
     */
    protected ResourceSummary parseResourceSummary(final Response response) {
        return new ResourceSummary(
            new GwtJson(JSONParser.parse(response.getText()).isObject()));
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
    protected boolean parseBoolean(final Response response) {
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
                new ActionSummary(new GwtJson(result.get(i).isObject())));
        }
        return actions;
    }

    /**
     * Parse the response as a map.
     *
     * @param response The response to parse.
     * @return A map.
     */
    protected Map<String, String> parseMapString(final Response response) {
        final JSONValue value = JSONParser.parse(response.getText());
        final JSONObject result = value.isObject();
        final Json json = new GwtJson(result);

        return json.getStringMap("properties");
    }
}
