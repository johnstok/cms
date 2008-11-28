/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.api;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ServiceException
    extends
        RuntimeException {

    private final int _code;

    /**
     * Constructor.
     *
     * @param code The error code.
     */
    public ServiceException(final int code) {
        _code = code;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public int getCode() {
        return _code;
    }
}
