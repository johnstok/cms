/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rendering;


/**
 * An exception indicating that a specified HTTP method is unsupported for the
 * a particular path.
 *
 * @author Civic Computing Ltd.
 */
public class UnsupportMethodException
    extends
        RuntimeException {

    private final String _httpMethod;


    /**
     * Constructor.
     *
     * @param httpMethod The HTTP method that can't be supported.
     */
    public UnsupportMethodException(final String httpMethod) {
        _httpMethod = httpMethod;
    }


    /**
     * Accessor.
     *
     * @return Returns the httpMethod.
     */
    public String getHttpMethod() {
        return _httpMethod;
    }
}
