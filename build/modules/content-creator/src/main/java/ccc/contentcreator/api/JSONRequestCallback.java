/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.api;

import ccc.contentcreator.client.Globals;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
final class JSONRequestCallback
    implements
        RequestCallback {

    /** _action : Action. */
    private final Action<JSONValue> _action;

    /**
     * Constructor.
     *
     * @param action The action to execute when a response is received.
     */
    public JSONRequestCallback(final Action<JSONValue> action) {
        _action = action;
    }

    /** {@inheritDoc} */
    public void onError(final Request request, final Throwable exception) {
        Globals.unexpectedError(exception);
    }

    /** {@inheritDoc} */
    public void onResponseReceived(final Request request,
                                   final Response response) {
        final int responseCode = response.getStatusCode();
        if (Response.SC_OK == responseCode) {
            try {
                final String responseText = response.getText();
                _action.execute(JSONParser.parse(responseText));
            } catch (final JSONException e) {
                Globals.unexpectedError(e);
            }
        } else if (Response.SC_NO_CONTENT == responseCode
            || 1223 == responseCode){
            _action.execute(new JSONObject());
        }else {
            Globals.unexpectedError(new RuntimeException(
                "Unexpected error: ["
                + responseCode
                + "] "
                + response.getStatusText()));
        }
    }
}
