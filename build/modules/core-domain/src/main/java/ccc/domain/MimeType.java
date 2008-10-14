/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class MimeType {

    private final String _primary;
    private final String _sub;

    /**
     * Constructor.
     *
     * @param primary
     * @param sub
     */
    public MimeType(final String primary, final String sub) {
        this(new String[] {primary, sub});
    }

    /**
     * Constructor.
     *
     * @param string
     */
    public MimeType(final String string) {
        this(string.split("/"));
    }

    /**
     * Constructor.
     *
     * @param string
     */
    private MimeType(final String... parts) {
        if (parts.length!=2) {
            throw new CCCException("Two parts required.");
        }
        DBC.require().notEmpty(parts[0]);
        DBC.require().notEmpty(parts[1]);

        _primary = parts[0];
        _sub = parts[1];
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param actual
     * @return
     */
    public boolean match(final MimeType actual) {
        return equals(actual);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((_primary == null) ? 0 : _primary.hashCode());
        result = prime * result + ((_sub == null) ? 0 : _sub.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MimeType other = (MimeType) obj;
        if (_primary == null) {
            if (other._primary != null) {
                return false;
            }
        } else if (!_primary.equals(other._primary)) {
            return false;
        }
        if (_sub == null) {
            if (other._sub != null) {
                return false;
            }
        } else if (!_sub.equals(other._sub)) {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return _primary+"/"+_sub;
    }


}
