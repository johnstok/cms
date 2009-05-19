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
package ccc.content.exceptions;

import ccc.api.DBC;
import ccc.domain.CCCException;
import ccc.domain.Resource;


/**
 * An exception indicating that a redirect is required.
 *
 * @author Civic Computing Ltd.
 */
public class RedirectRequiredException
    extends
        CCCException {

    private final Resource _target;

    /**
     * Constructor.
     *
     * @param target The target of the redirect.
     */
    public RedirectRequiredException(final Resource target) {
        DBC.require().notNull(target);
        _target = target;
    }

    /**
     * Accessor for the Resource which is the target of the redirect.
     *
     * @return The redirect's target, a Resource.
     */
    public Resource getResource() {
        return _target;
    }

}
