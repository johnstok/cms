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
package ccc.api;

import java.io.Serializable;

import ccc.annotations.ValueObject;


/**
 * Simple value holder for a UUID.
 * <p>
 * The id is represented as a string, using the java.util.UUID
 * canonical representation described in java.util.UUID#toString().
 *
 * @author Civic Computing Ltd.
 */
@ValueObject
public final class ID implements Serializable {
    private String _value;

    @SuppressWarnings("unused") private ID() { super(); }

    /**
     * Constructor.
     *
     * @param value The UUID as a string.
     */
    public ID(final String value) {
        _value = value;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return _value;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_value == null) ? 0 : _value.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) { return true; }
        if (obj == null) { return false; }
        if (getClass() != obj.getClass()) { return false; }
        final ID other = (ID) obj;
        if (_value == null) {
            if (other._value != null) { return false; }
        } else if (!_value.equals(other._value)) { return false; }
        return true;
    }
}
