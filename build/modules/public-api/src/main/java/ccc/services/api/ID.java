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
package ccc.services.api;

import java.io.Serializable;


/**
 * Simple value holder for a UUID.
 * <p>
 * The id is represented as a string, using the java.util.UUID
 * canonical representation described in java.util.UUID#toString().
 *
 * @author Civic Computing Ltd.
 */
public final class ID implements Serializable {
    private String _value;

    @SuppressWarnings("unused") private ID() { super(); }

    /**
     * Constructor.
     *
     * @param value The UUID as a string.
     */
    public ID(final String value) {
        _value = value;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return _value;
    }
}
