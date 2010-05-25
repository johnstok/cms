/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.core;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class Response {

    private String _text;
    private String _statusText;
    private int    _statusCode;


    /**
     * Constructor.
     *
     * @param text Text
     * @param statusText Status text
     * @param statusCode Status code
     */
    public Response(final String text,
                    final String statusText,
                    final int statusCode) {
        super();
        _text = text;
        _statusText = statusText;
        _statusCode = statusCode;
    }


    /**
     * Accessor.
     *
     * @return Returns the _text.
     */
    public String getText() {
        return _text;
    }


    /**
     * Accessor.
     *
     * @return Returns the _statusText.
     */
    public String getStatusText() {
        return _statusText;
    }


    /**
     * Accessor.
     *
     * @return Returns the _statusCode.
     */
    public int getStatusCode() {
        return _statusCode;
    }
}
