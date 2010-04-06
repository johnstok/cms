/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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

import ccc.serialization.Json;
import ccc.serialization.JsonImpl;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.DBC;


/**
 * Abstract base class extended by entity classes that require persistence.
 *
 * @author Civic Computing Ltd.
 */
public abstract class Entity implements Serializable, Jsonable {

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
    public UUID getId() {
        return _id;
    }


    /**
     * Set this entity's id.
     *
     * @param id The new id.
     */
    public void setId(final UUID id) {
        DBC.require().notNull(id);
        _id = id;
    }


    /**
     * Accessor for the version field.
     *
     * @return This entity's version as a long.
     */
    public long getVersion() {
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
        return new JsonImpl(this).getDetail();
    }


    /** {@inheritDoc} */
    @Override
    @Deprecated
    public void toJson(final Json json) {
        json.set(JsonKeys.ID, getId().toString());
        json.set(JsonKeys.VERSION, Long.valueOf(getVersion()));
    }
}
