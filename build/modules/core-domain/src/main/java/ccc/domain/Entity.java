/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.domain;

import java.io.Serializable;
import java.util.UUID;

import ccc.api.DBC;


/**
 * Abstract base class extended by entity classes that require persistence.
 *
 * @author Civic Computing Ltd.
 */
public abstract class Entity implements Serializable {

    private long  _version = -1;
    private UUID  _id      = UUID.randomUUID();

    /** Constructor: for persistence only. */
    protected Entity() { super(); }

    /**
     * Constructor.
     *
     * @param id The entity's ID.
     * @param version The entity's version.
     */
    protected Entity(final UUID id, final long version) {
        DBC.require().notNull(id);
        _id = id;
        _version = version;
    }

    /**
     * Accessor for the id field.
     *
     * @return This entity's id as a {@link UUID}.
     */
    public UUID id() {
        return _id;
    }

    /**
     * Set this entity's id.
     *
     * @param id The new id.
     */
    public void id(final UUID id) {
        DBC.require().notNull(id);
        _id = id;
    }

    /**
     * Accessor for the version field.
     *
     * @return This entity's version as a long.
     */
    public long version() {
        return _version;
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

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return _id.toString();
    }
}
