/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.client;


import ccc.contentcreator.actions.GetPropertyAction;
import ccc.contentcreator.actions.IsLoggedInAction;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.http.client.Response;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public final class ContentCreator implements EntryPoint {

    private IGlobalsImpl _globals = new IGlobalsImpl();


    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        _globals.installUnexpectedExceptionHandler();
        loadSettings();
        new IsLoggedInAction().execute();
    }


    private void loadSettings() {
        new GetPropertyAction() {
            /** {@inheritDoc} */
            @Override protected void onOK(final Response response) {
                _globals.setSettings(parseMapString(response));
            }
        }.execute();
    }
}
