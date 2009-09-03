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

package java.util;


/**
 * Simple value holder for a UUID.
 * <p>
 * The id is represented as a string, using the java.util.UUID
 * canonical representation described in java.util.UUID#toString().
 *
 * @author Civic Computing Ltd.
 */
public final class UUID
implements java.io.Serializable {

    private final String _uuid;

    /**
     * Constructor.
     *
     * @param uuid The UUID as a string.
     */
    public UUID(final String uuid) {
        _uuid = uuid;
    }

    /**
     * Creates a <tt>UUID</tt> from the string standard representation as
     * described in the {@link #toString} method.
     *
     * @param uuid The UUID as a string.
     * @return Created UUID.
     *
     */
    public static UUID fromString(final String uuid) {
        return new UUID(uuid);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
    return _uuid;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return _uuid.hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        return _uuid.equals(obj);
    }

}
