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

import java.util.UUID;


/**
 * Abstract base class extended by entity classes that require persistence.
 *
 * @author Civic Computing Ltd
 */
public abstract class Entity {

    /** id : UUID. */
    private final UUID id = UUID.randomUUID();
    private final int version = -1;

    /**
     * @see java.lang.Object#toString()
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
     * @return This entity's id as a Java {@link UUID}.
     */
    public final UUID id() {
        return id;
    }

}