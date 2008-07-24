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

import ccc.commons.jee.UID;


/**
 * Abstract base class extended by entity classes that require persistence.
 *
 * @author Civic Computing Ltd
 */
public abstract class Entity {

    /** id : UID. */
    private final UID id = new UID();
    private final int version = -1;

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return
            this.getClass().getName() + ": "
            + id.toString()
            + " [version=" + version + "]";
    }

    /**
     * Accessor for the id field.
     *
     * @return This entity's id as a {@link UID}.
     */
    public final UID id() {
        return id;
    }

}