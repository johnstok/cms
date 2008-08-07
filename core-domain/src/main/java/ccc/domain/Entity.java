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

import java.io.Serializable;
import java.util.UUID;


/**
 * Abstract base class extended by entity classes that require persistence.
 *
 * @author Civic Computing Ltd
 */
public abstract class Entity implements Serializable {

    private UUID _id      = UUID.randomUUID();
    private int  _version = -1;

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return
            this.getClass().getName() + ": "
            + _id.toString()
            + " [version=" + _version + "]";
    }

    /**
     * Accessor for the id field.
     *
     * @return This entity's id as a {@link UUID}.
     */
    public final UUID id() {
        return _id;
    }

}
