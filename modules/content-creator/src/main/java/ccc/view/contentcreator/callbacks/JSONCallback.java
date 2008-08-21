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
package ccc.view.contentcreator.callbacks;

import ccc.view.contentcreator.client.GwtApp;

import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * A callback extension to simplify working with JSON.
 *
 * @author Civic Computing Ltd
 */
public abstract class JSONCallback implements AsyncCallback<String> {

    private final GwtApp _app;

    /**
     * Constructor.
     *
     * @param application The application instance for use by this callback.
     */
    public JSONCallback(final GwtApp application) {
        _app = application;
    }

    /**
     * {@inheritDoc}
     */
    public final void onFailure(final Throwable caught) {
        _app.alert("Error: "+caught.getMessage());
    }

    /**
     * {@inheritDoc}
     */
    public final void onSuccess(final String result) {
        final JSONValue jsonResult = JSONParser.parse(result);
        onSuccess(jsonResult);
    }

    /**
     * {@inheritDoc}
     */
    public abstract void onSuccess(JSONValue result);
}
