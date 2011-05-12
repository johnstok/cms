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

import ccc.api.types.DBC;
import ccc.domain.ResourceEntity;

/**
 * Sort Resources in Ascending based on date changed.
 *
 * @author Civic Computing Ltd.
 */
public final class DateChangedAscendingComparator
    implements
        Serializable,
        Comparator<ResourceEntity> {

    /** {@inheritDoc} */
    @Override
    public int compare(final ResourceEntity o1, final ResourceEntity o2) {
        DBC.require().notNull(o1);
        DBC.require().notNull(o2);
        return o1.getDateChanged().compareTo(o2.getDateChanged());
    }
}
