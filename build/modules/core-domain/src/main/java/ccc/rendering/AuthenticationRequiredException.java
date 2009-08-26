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
package ccc.rendering;

import ccc.domain.CCCException;
import ccc.domain.Resource;
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

    private final Resource _resource;

    /**
     * Constructor.
     *
     * @param r The resource that requires authentication.
     */
    public AuthenticationRequiredException(final Resource r) {
        DBC.require().notNull(r);
        _resource = r;
    }

    /**
     * Accessor.
     *
     * @return The resource that requires authentication.
     */
    public Resource getResource() {
        return _resource;
    }
}
