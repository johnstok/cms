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

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
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
        throw new RuntimeException("Unexpected error.", exception);
    }

    /** {@inheritDoc} */
    public void onResponseReceived(final Request request,
                                   final Response response) {
        if (Response.SC_OK == response.getStatusCode()) {
            try {
                _action.execute(JSONParser.parse(response.getText()));
            } catch (final JSONException e) {
                throw new RuntimeException("Failed to parse response.", e);
            }
        } else {
            throw new RuntimeException(
                "Unexpected error: "+response.getStatusText());
        }
    }
}
