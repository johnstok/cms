/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.core;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class Response {

    String _text;
    String _statusText;
    int    _statusCode;
    
    
    /**
     * Constructor.
     *
     * @param text
     * @param statusText
     * @param statusCode
     */
    public Response(String text, String statusText, int statusCode) {
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
