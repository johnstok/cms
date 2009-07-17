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
import java.util.List;

import ccc.api.ResourceSummary;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.GwtJson;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class RemotingAction
    implements
        Action {

    private final String _actionName;
    private final Method _method;

    /**
     * Constructor.
     *
     * @param actionName The name of this action.
     */
    public RemotingAction(final String actionName) {
        _actionName = actionName;
        _method = RequestBuilder.GET;
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
    }

    /** {@inheritDoc} */
    @Override
    public void execute() {
    //        new ErrorReportingCallback<Collection<ResourceSummary>>(
    //            _globals.userActions().internalAction())

        final String url = GLOBALS.apiURL() + getPath();
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
                GLOBALS.unexpectedError(exception, _actionName);
            }

            public void onResponseReceived(final Request request,
                                           final Response response) {
//                GLOBALS.alert(
//                    response.getStatusCode()+" "+
//                    response.getStatusText());
                if (SC_OK == response.getStatusCode()) {
                    onOK(response);
                } else if (SC_NO_CONTENT == response.getStatusCode()) {
                    onNoContent(response);
                } else {
                    GLOBALS.unexpectedError(
                        new RuntimeException(
                            "Invalid response: "
                            + response.getStatusCode()+" "
                            + response.getStatusText()),
                        _actionName);
                }
            }

        });

        try {
            builder.send();
        } catch (final RequestException e) {
            GLOBALS.unexpectedError(e, "Foo");
        }
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
        throw new RuntimeException(// TODO Add UnsupportedResponseException
            "Unsupported response: "
            + response.getStatusCode() + " "
            + response.getStatusText());
    }

    /**
     * Handle a '200 OK' response from the remote server.
     *
     * @param response The server response.
     */
    protected void onOK(final Response response) {
        throw new RuntimeException(// TODO Add UnsupportedResponseException
            "Unsupported response: "
            + response.getStatusCode() + " "
            + response.getStatusText());
    }


    protected ResourceSummary parseResourceSummary(final Response response) {
        return new ResourceSummary(
            new GwtJson(JSONParser.parse(response.getText()).isObject()));
    }

    protected List<String> parseListString(final Response response) {
        final List<String> strings = new ArrayList<String>();
        final JSONArray result = JSONParser.parse(response.getText()).isArray();
        for (int i=0; i<result.size(); i++) {
            strings.add(result.get(i).isString().stringValue());
        }
        return strings;
    }

    // 405 Method Not Allowed
    // protected abstract void onMethodNotAllowed(final Response response);

    // 400 Bad Request
    // protected abstract void onBadRequest(final Response response);
}