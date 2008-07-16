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

/**
 * A helper class to support design-by-contract (DBC).
 * 
 * @author Civic Computing Ltd
 */
public class DBC {
    
    /**
     * Constructor.
     */
    private DBC() { /* No-Op */ }

    /**
     * Factory method.
     * 
     * @return An instance of DBC.
     */
    public static DBC require() {

        return new DBC();
    }

    /**
     * Factory method.
     * 
     * @return An instance of DBC.
     */
    public static DBC ensure() {

        return new DBC();
    }

    /**
     * Assert that the specified value is not null.
     * 
     * @param object
     */
    public void notNull(Object object) {
        if (null==object) throw new IllegalArgumentException("Specified value may not be NULL.");
    }
    
    /**
     * Assert that the specified string is not empty.
     * 
     * @param string
     */
    public void notEmpty(String string) {
        if (null==string) throw new IllegalArgumentException("Specified string may not be NULL.");
        if (string.length() < 1) throw new IllegalArgumentException("Specified string must have length > 0.");
    }
}
