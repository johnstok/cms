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

import ccc.commons.DBC;


/**
 * Abstract base class extended by entity classes that require persistence.
 *
 * @author Civic Computing Ltd
 */
public abstract class Entity implements Serializable {

    private UUID _id      = UUID.randomUUID();
    private int  _version = -1;

    /**
     * Accessor for the id field.
     *
     * @return This entity's id as a {@link UUID}.
     */
    public UUID id() {
        return _id;
    }

    /**
     * Accessor for the version field.
     *
     * @return This entity's version, as an integer.
     */
    public int version() {
        return _version;
    }

    /**
     * Set this entoty's id.
     *
     * @param id The new id.
     */
    public void id(final UUID id) {
        DBC.require().notNull(id);
        _id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((_id == null) ? 0 : _id.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Entity other = (Entity) obj;
        if (_id == null) {
            if (other._id != null) {
                return false;
            }
        } else if (!_id.equals(other._id)) {
            return false;
        }
        return true;
    }


}
