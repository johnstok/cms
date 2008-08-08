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
package ccc.view.contentcreator.client;

import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public abstract class JSONCallback implements AsyncCallback<String> {

    /**
     * {@inheritDoc}
     */
    public final void onFailure(final Throwable caught) {
        Window.alert("Error: "+caught.getMessage());
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
