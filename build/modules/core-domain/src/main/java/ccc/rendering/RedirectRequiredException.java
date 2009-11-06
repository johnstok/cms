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
import ccc.types.DBC;


/**
 * An exception indicating that a redirect is required.
 *
 * @author Civic Computing Ltd.
 */
public class RedirectRequiredException
    extends
        CCCException {

    private final String _target;

    /**
     * Constructor.
     *
     * @param target The target path for the redirect.
     */
    public RedirectRequiredException(final String target) {
        DBC.require().notNull(target);
        _target = target;
    }

    /**
     * Accessor for the Resource which is the target of the redirect.
     *
     * @return The redirect's target, a Resource.
     */
    public String getTarget() {
        return _target;
    }

}
