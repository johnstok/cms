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

package ccc.domain.sorting;

import java.io.Serializable;
import java.util.Comparator;

import ccc.domain.Resource;
import ccc.types.DBC;

/**
 * Sort Resources in Ascending, Alphanumeric order, based on name.
 *
 * @author Civic Computing Ltd.
 */
public final class AlphaNumericAscendingNameComparator
    implements
        Serializable,
        Comparator<Resource> {

    private final boolean _caseSensitive;


    /**
     * Constructor.
     */
    public AlphaNumericAscendingNameComparator() {
        this(true);
    }


    /**
     * Constructor.
     *
     * @param caseSensitive Should the sort be case sensitive.
     */
    public AlphaNumericAscendingNameComparator(final boolean caseSensitive) {
        _caseSensitive = caseSensitive;
    }


    /** {@inheritDoc} */
    @Override
    public int compare(final Resource o1, final Resource o2) {
        DBC.require().notNull(o1);
        DBC.require().notNull(o2);

        if (_caseSensitive) {
            return o1.getName().toString().compareTo(o2.getName().toString());
        }
        return String.CASE_INSENSITIVE_ORDER.compare(
            o1.getName().toString(), o2.getName().toString());
    }

}
