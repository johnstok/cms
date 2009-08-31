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

package ccc.domain.sorting;

import java.io.Serializable;
import java.util.Comparator;

import ccc.domain.Resource;
import ccc.types.DBC;

/**
 * Sort Resources in Ascending based on date changed.
 *
 * @author Civic Computing Ltd.
 */
public final class DateChangedAscendingComparator
    implements
        Serializable,
        Comparator<Resource> {

    /** {@inheritDoc} */
    @Override
    public int compare(final Resource o1, final Resource o2) {
        DBC.require().notNull(o1);
        DBC.require().notNull(o2);
        return o1.dateChanged().compareTo(o2.dateChanged());
    }
}
