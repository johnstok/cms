
package ccc.contentcreator.client;


import ccc.contentcreator.actions.IsLoggedInAction;

import com.google.gwt.core.client.EntryPoint;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public final class ContentCreator implements EntryPoint {

    private IGlobals _globals = new IGlobalsImpl();


    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        _globals.installUnexpectedExceptionHandler();
        new IsLoggedInAction().execute();
    }
}
