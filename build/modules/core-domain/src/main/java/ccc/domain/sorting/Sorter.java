/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.domain.sorting;

import java.util.Collections;
import java.util.List;

import ccc.domain.Resource;
import ccc.types.ResourceOrder;


/**
 * Sorts a list of resources.
 *
 * @author Civic Computing Ltd.
 */
public final class Sorter {

    private Sorter() { super(); }


    /**
     * Sort the supplied list.
     *
     * @param resources The list to sort.
     * @param order     The order for the list's entries.
     */
    public static void sort(final List<Resource> resources,
                            final ResourceOrder  order) {
        switch (order) {

            case MANUAL:
                break;

            case NAME_ALPHANUM_ASC:
                Collections.sort(
                    resources, new AlphaNumericAscendingNameComparator());
                break;

            case DATE_CREATED_ASC:
                Collections.sort(
                    resources, new DateCreatedAscendingComparator());
                break;

            case DATE_CREATED_DESC:
                Collections.sort(
                    resources, new DateCreatedAscendingComparator());
                Collections.reverse(resources);
                break;

            case DATE_CHANGED_ASC:
                Collections.sort(
                    resources, new DateChangedAscendingComparator());
                break;

            case DATE_CHANGED_DESC:
                Collections.sort(
                    resources, new DateChangedAscendingComparator());
                Collections.reverse(resources);
                break;

            default:
                throw new IllegalArgumentException("Unsupported order: "+order);
        }
    }
}
