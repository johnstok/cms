/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.widgets;


/**
 * An option for a choice field.
 *
 * @author Civic Computing Ltd.
 */
public final class Option {

    private final String _title;
    private final String _value;
    private final Boolean _default;


    /**
     * Constructor.
     *
     * @param title The option's title.
     * @param value The option's value.
     * @param def   Is the option enabled by default.
     */
    public Option(final String title, final String value, final Boolean def) {
        _title = title;
        _value = value;
        _default = def;
    }


    /**
     * Accessor.
     *
     * @return Returns the title.
     */
    public String getTitle() {
        return _title;
    }


    /**
     * Accessor.
     *
     * @return Returns the value.
     */
    public String getValue() {
        return _value;
    }


    /**
     * Accessor.
     *
     * @return Returns the default state.
     */
    public Boolean isDefault() {
        return _default;
    }
}
