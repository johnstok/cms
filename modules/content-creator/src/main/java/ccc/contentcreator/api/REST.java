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

import ccc.contentcreator.client.Globals;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.json.client.JSONValue;


/**
 * Helper class for implementing REST methods.
 *
 * @author Civic Computing Ltd.
 */
public final class REST {

    private REST() { super(); }

    /**
     * Execute a GET method, executing the specified action, on response.
     *
     * @param relativeURL The url to send the GET to.
     * @param action The action used to process the response.
     */
    public static void get(final String relativeURL,
                                 final Action<JSONValue> action) {
        invokeMethod(relativeURL, action, RequestBuilder.GET);
    }

    /**
     * Execute a POST method, executing the specified action, on response.
     *
     * @param relativeURL The url to send the POST to.
     * @param action The action used to process the response.
     */
    public static void post(final String relativeURL,
                                 final Action<JSONValue> action) {
        invokeMethod(relativeURL, action, RequestBuilder.POST);
    }

    private static void invokeMethod(final String relativeURL,
                                     final Action<JSONValue> action,
                                     final Method httpMethod) {
        try {
            final String url = Globals.appURL() + relativeURL;
            final RequestBuilder builder = new RequestBuilder(httpMethod, url);
            builder.sendRequest(null, new JSONRequestCallback(action));
        } catch (final RequestException e) {
            Globals.alert("Error invoking request: " + e.getMessage());
        }
    }

//    private static void invokeCallback(
//                          final String relativeURL,
//                          final AsyncCallback<List<JsonModelData>> callback,
//                          final Method httpMethod) {
//        try {
//            final String url = Globals.appURL() + relativeURL;
//            final RequestBuilder builder = new RequestBuilder(httpMethod, url);
//            builder.sendRequest(null, new AsyncCallbackBridge(callback));
//        } catch (final RequestException e) {
//            callback.onFailure(e);
//        }
//    }
}
