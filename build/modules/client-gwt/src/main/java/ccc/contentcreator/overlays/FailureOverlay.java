/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.overlays;

import ccc.types.FailureCode;

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
    public final FailureCode getCode() {
        return FailureCode.valueOf(getCodeString());
    }


    private native String getCodeString() /*-{ return this.code; }-*/;


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
