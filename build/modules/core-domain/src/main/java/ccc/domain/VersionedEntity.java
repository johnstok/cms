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
import ccc.commons.serialisation.CanSerialize;
import ccc.commons.serialisation.DeSerializer;
import ccc.commons.serialisation.JsonSerializer;
import ccc.commons.serialisation.Serializer;


/**
 * Abstract base class extended by entity classes that require persistence.
 * TODO: Factor out 'VersionedEntity' subclass?
 *
 * @author Civic Computing Ltd
 */
public abstract class VersionedEntity implements Serializable, CanSerialize {

    private UUID _id      = UUID.randomUUID();
    private int  _version = -1;

    /** Constructor: for persistence only. */
    protected VersionedEntity() { super(); }

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
     * @return This entity's version, as an integer.
     */
    public int version() {
        return _version;
    }


    /**
     * Mutator for the version property.
     *
     * @param version The new version to set.
     */
    public void version(final int version) {
        _version = version;
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
        final VersionedEntity other = (VersionedEntity) obj;
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
        return new JsonSerializer().dict(this).toString();
    }

    /** {@inheritDoc} */
    @Override
    public void deserialize(final DeSerializer ds) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void serialize(final Serializer s) {
        s.string("id", id().toString());
        s.integer("version", version());
    }
}
