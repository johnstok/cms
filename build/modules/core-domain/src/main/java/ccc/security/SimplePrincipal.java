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
package ccc.security;

import java.security.Principal;


/**
 * Simple principal.
 *
 * @author Civic Computing Ltd.
 */
public class SimplePrincipal
    implements
        Principal {

    private final String _name;

    /**
     * Constructor.
     *
     * @param name The principal's name.
     */
    public SimplePrincipal(final String name) {
        _name = name;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() { return _name; }

}
