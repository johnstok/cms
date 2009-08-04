/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.overlays;

import ccc.api.FailureCodes;

import com.google.gwt.core.client.JavaScriptObject;


/**
 * An CCC failure.
 *
 * @author Civic Computing Ltd.
 */
public class FailureOverlay
    extends
        JavaScriptObject {

    /**
     * Constructor.
     */
    protected FailureOverlay() { super(); }


    /**
     * Accessor for the failure's ID.
     *
     * @return The ID as a string.
     */
    public final native String getId() /*-{ return this.id; }-*/;


    /**
     * Accessor for the failure code.
     *
     * @return The code as an integer.
     */
    public final FailureCodes getCode() {
        return FailureCodes.valueOf(getCodeString());
    }


    private final native String getCodeString() /*-{ return this.code; }-*/;


    /**
     * Factory method for failure overlay objects.
     *
     * @param json The JSON string used to construct the object.
     * @return An overlay object representing the JSON.
     */
    public static native FailureOverlay fromJson(final String json) /*-{
        return eval('(' + json + ')');
    }-*/;
}
