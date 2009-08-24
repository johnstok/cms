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
package ccc.types;

import java.io.Serializable;


/**
 * A username.
 *
 * @author Civic Computing Ltd.
 */
public final class Username implements Serializable {

    public static final String  VALID_CHARACTERS = "[\\w]*";
    public static final int     MIN_LENGTH = 4;

    private String _value;

    @SuppressWarnings("unused") private Username() { super(); }

    /**
     * Constructor.
     *
     * @param value The username, represented as a string.
     */
    public Username(final String value) {
        DBC.require().notEmpty(value);
//        DBC.require().minLength(value, MIN_LENGTH);
//        DBC.require().toMatch(VALID_CHARACTERS, value);
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
        final Username other = (Username) obj;
        if (_value == null) {
            if (other._value != null) { return false; }
        } else if (!_value.equals(other._value)) { return false; }
        return true;
    }
}
