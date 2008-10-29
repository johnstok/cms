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
package ccc.contentcreator.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class AsyncCallbackBridge
    implements
        RequestCallback {

    private final AsyncCallback<List<JsonModelData>>  _callback;

    AsyncCallbackBridge(final AsyncCallback<List<JsonModelData>> callback) {
        _callback = callback;
    }

    public void onError(final Request request, final Throwable exception) {
        _callback.onFailure(exception);
     }

     public void onResponseReceived(final Request request, final Response response) {
       try {
           if (200 == response.getStatusCode()) {
               final JSONValue jsonValue = JSONParser.parse(response.getText());
               _callback.onSuccess(asList(jsonValue));
           } else {
               throw new RuntimeException("Invalid response");
           }
       } catch (final Exception e) {
           _callback.onFailure(e);
       }
     }

    /**
     * TODO: Add a description of this method.
     *
     * @param jsonValue
     * @return
     */
    private List<JsonModelData> asList(final JSONValue jsonValue) {
        final List<JsonModelData> results = new ArrayList<JsonModelData>();
        final JSONArray array = jsonValue.isArray();
        for (int i=0; i<array.size(); i++) {
            results.add(new JsonModelData(array.get(i).isObject()));
        }
        return results;
    }
}
