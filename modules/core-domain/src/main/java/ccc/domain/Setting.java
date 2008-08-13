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

import ccc.commons.DBC;


/**
 * A setting, represented as a key:value pair.
 *
 * @author Civic Computing Ltd
 */
public class Setting extends Entity {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = 1669421605591024452L;

    private String _name;
    private String _value;

    /**
     * Constructor.
     *
     * @param name The name of this setting.
     * @param value The value of this setting.
     */
    public Setting(final String name, final String value) {
        DBC.require().notEmpty(name);
        _name = name;
        value(value);
    }

    /**
     * Accessor for name.
     *
     * @return The name, as a string.
     */
    public final String name() {
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

}
