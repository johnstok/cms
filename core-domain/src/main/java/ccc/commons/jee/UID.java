/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.commons.jee;

import java.io.Serializable;



/**
 * A portable UID class.
 *
 * @author Civic Computing Ltd
 */
public class UID implements Serializable {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = 3642848275879243577L;

    private final long representation;

    /**
     * Constructor.
     *
     * @param representation The long representation for this UID.
     */
    public UID(final long representation) {
        this.representation = representation;
    }

    /**
     * Constructor.
     *
     */
    public UID() {
        this(
            new GeneralHashFunctionLibrary()
                .djbHash(
                    System.currentTimeMillis()
                    + ""
                    + Math.random()));
    }

    /**
     * Retrieve the underlying representation of this UID as a long.
     *
     * @return The internal representation of this UID, as a long.
     */
    public final long value() {
        return representation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result =
            prime
            * result
            + (int) (representation ^ (representation >>> 32));
        return result;
    }

    /**
     * {@inheritDoc}
     */
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
        final UID other = (UID) obj;
        if (representation != other.representation) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.valueOf(representation);
    }


}
