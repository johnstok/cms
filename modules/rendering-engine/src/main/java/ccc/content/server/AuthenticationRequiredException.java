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
package ccc.content.server;

import ccc.commons.DBC;
import ccc.domain.Resource;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class AuthenticationRequiredException
    extends
        RuntimeException {

    private final Resource _resource;

    /**
     * Constructor.
     *
     * @param r
     */
    public AuthenticationRequiredException(final Resource r) {
        DBC.require().notNull(r);
        _resource = r;
    }

    public Resource getResource() {
        return _resource;
    }
}
