/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import ccc.commons.DBC;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class Alias extends Resource {

    private Resource _target;

    /**
     * Constructor.
     *
     * @param resourceName
     * @param target
     */
    public Alias(final ResourceName resourceName, final Resource target) {
        super(resourceName);
        DBC.require().notNull(target);
        _target = target;
    }

    /**
     * Constructor.
     * N.B. This constructor should only be used for persistence.
     */
    @SuppressWarnings("unused")
    protected Alias() { super(); }

    /**
     * @see ccc.domain.Resource#type()
     */
    @Override
    public ResourceType type() {
        return ResourceType.ALIAS;
    }

    public Resource target() {
        return _target;
    }
}
