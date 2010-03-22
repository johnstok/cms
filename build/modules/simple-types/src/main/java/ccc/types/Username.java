/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.types;

import java.io.Serializable;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;


/**
 * A username.
 *
 * @author Civic Computing Ltd.
 */
public final class Username implements Serializable, Jsonable {

    /** VALID_CHARACTERS : String. */
    public static final String  VALID_CHARACTERS = "[\\w]*";
    /** MIN_LENGTH : int. */
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

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.USERNAME, _value);
    }
}
