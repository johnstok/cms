/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.api;

import java.util.List;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * This class allows a {@link RequestCallback} to operate as an
 * {@link AsyncCallback}, converting a {@link JSONValue} to a List of
 * {@link JsonModelData} objects.
 *
 * It is an implementation of the Adaptor pattern:
 * http://en.wikipedia.org/wiki/Adapter_pattern.
 *
 * TODO: Rename to JSONArrayCallbackAdaptor.
 * @author Civic Computing Ltd.
 */
public class AsyncCallbackBridge
    implements
        RequestCallback {

    private final AsyncCallback<List<JsonModelData>>  _callback;

    /**
     * Constructor.
     *
     * @param callback The callback to adapt.
     */
    public AsyncCallbackBridge(
                           final AsyncCallback<List<JsonModelData>> callback) {
        _callback = callback;
    }

    /** {@inheritDoc} */
    public void onError(final Request request, final Throwable exception) {
        _callback.onFailure(exception);
     }

     /** {@inheritDoc} */
    public void onResponseReceived(final Request request,
                                    final Response response) {
       try {
           if (Response.SC_OK == response.getStatusCode()) {
               final JSONValue jsonValue = JSONParser.parse(response.getText());
               _callback.onSuccess(JsonModelData.fromArray(jsonValue));
           } else {
               throw new RuntimeException("Invalid response");
           }
       } catch (final Exception e) {
           _callback.onFailure(e);
       }
     }
}
