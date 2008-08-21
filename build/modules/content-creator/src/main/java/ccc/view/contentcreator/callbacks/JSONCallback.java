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

import ccc.view.contentcreator.client.Application;

import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * A callback extension to simplify working with JSON.
 *
 * @author Civic Computing Ltd
 */
public abstract class JSONCallback implements AsyncCallback<String> {

    private final Application _app;

    /**
     * Constructor.
     *
     * @param application The application instance for use by this callback.
     */
    public JSONCallback(final Application application) {
        _app = application;
    }

    /**
     * {@inheritDoc}
     */
    public final void onFailure(final Throwable caught) {
        // TODO: should be using a message, not a constant.
        _app.alert(
            _app.constants().error()
            + ": " //$NON-NLS-1$
            + caught.getMessage());
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
