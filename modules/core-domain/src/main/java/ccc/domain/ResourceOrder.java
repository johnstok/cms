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
package ccc.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ccc.commons.DBC;


/**
 * Support sort orders for resource lists.
 *
 * @author Civic Computing Ltd.
 */
public enum ResourceOrder {

    /** MANUAL : ResourceOrder. */
    MANUAL {
        /** {@inheritDoc} */
        @Override public void sort(final List<Resource> resources) {
            return; // Leave ordering as-is.
        }
    },

    /** NAME_ALPHANUM_ASC : ResourceOrder. */
    NAME_ALPHANUM_ASC {
        /** {@inheritDoc} */
        @Override public void sort(final List<Resource> resources) {
            Collections.sort(
                resources, new AlphaNumericAscendingNameComparator());
        }
    };

    /**
     * Sort the supplied list.
     *
     * @param resources The list to sort.
     */
    public abstract void sort(List<Resource> resources);

    /**
     * Sort Resources in Ascending, Alphanumeric order, based on name.
     *
     * @author Civic Computing Ltd.
     */
    static final class AlphaNumericAscendingNameComparator
        implements
            Serializable, Comparator<Resource> {

        /** {@inheritDoc} */
        @Override
        public int compare(final Resource o1, final Resource o2) {
            DBC.require().notNull(o1);
            DBC.require().notNull(o2);
            return o1.name().toString().compareTo(o2.name().toString());
        }

    }
}
