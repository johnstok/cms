/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
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
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import ccc.types.DBC;


/**
 * A setting, represented as a key:value pair.
 *
 * @author Civic Computing Ltd
 */
public class Setting extends Entity {


    private Name _name;
    private String _value;

    /** Constructor: for persistence only. */
    protected Setting() { super(); }

    /**
     * Constructor.
     *
     * @param name The name of this setting.
     * @param value The value of this setting.
     */
    public Setting(final Name name, final String value) {
        DBC.require().notNull(name);
        _name = name;
        value(value);
    }

    /**
     * Accessor for name.
     *
     * @return The name, as a string.
     */
    public final Name name() {
        return _name;
    }

    /**
     * Accessor for value.
     *
     * @return The value, as a string.
     */
    public final String value() {
        return _value;
    }

    /**
     * The value as a string.
     *
     * @param value The new value for this setting.
     */
    public final void value(final String value) {
        DBC.require().notNull(value);
        _value = value;
    }

    /**
     * Valid setting names.
     *
     * @author Civic Computing Ltd.
     */
    public static enum Name {

        /** DATABASE_VERSION : Name. */
        DATABASE_VERSION,
        /** FILE_STORE_PATH : Name. */
        FILE_STORE_PATH,
        /** LUCENE_INDEX_PATH : Name. */
        LUCENE_INDEX_PATH;
    }
}
