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
package ccc.domain;

import ccc.types.DBC;


/**
 * An exception indicating that a user attempted to access a resource without
 * sufficient privileges.
 *
 * @author Civic Computing Ltd.
 */
public class AuthenticationRequiredException
    extends
        CCCException {

    private final String _resource;

    /**
     * Constructor.
     *
     * @param r The path to the resource that requires authentication.
     */
    public AuthenticationRequiredException(final String r) {
        DBC.require().notNull(r);
        _resource = r;
    }

    /**
     * Accessor.
     *
     * @return The path to the resource that requires authentication.
     */
    public String getTarget() {
        return _resource;
    }
}
