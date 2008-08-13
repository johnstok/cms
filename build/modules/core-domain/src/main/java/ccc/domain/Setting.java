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
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class Setting {

    private String _name;
    private String _value;

    /**
     * Constructor.
     *
     * @param string
     * @param value
     */
    public Setting(final String name, final String value) {
        DBC.require().notEmpty(name);
        _name = name;
        value(value);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String name() {
        return _name;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String value() {
        return _value;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param value
     */
    public void value(final String value) {
        DBC.require().notNull(value);
        _value = value;
    }

}
