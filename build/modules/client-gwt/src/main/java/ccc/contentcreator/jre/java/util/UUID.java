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

        final int prime = 31;
        int result = 1;
        result = prime * result + ((_uuid == null) ? 0 : _uuid.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {

        if (this == obj) { return true; }
        if (obj == null) { return false; }
        if (getClass() != obj.getClass()) { return false; }
        final UUID other = (UUID) obj;
        if (_uuid == null) {
            if (other._uuid != null) { return false; }
        } else if (!_uuid.equals(other._uuid)) { return false; }
        return true;
    }


}
