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
package ccc.types;

import java.io.Serializable;


/**
 * Simple value holder for a decimal.
 * The decimal is represented as a string, using the java.math.BigDecimal
 * canonical representation described in java.math.BigDecimal#toString().
 *
 * TODO: Validate string input by regex.
 * TODO: Add constructors for long & double (reject NaN & Infinity).
 * TODO: Add a hibernate user type.
 *
 * @author Civic Computing Ltd.
 */
public final class Decimal implements Serializable {
    private String _value;

    @SuppressWarnings("unused") private Decimal() { super(); }

    /**
     * Constructor.
     *
     * @param value The decimal as a string.
     */
    public Decimal(final String value) {
        _value = value;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return _value;
    }
}
