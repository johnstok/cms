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

import junit.framework.TestCase;
import static ccc.commons.jee.DBC.*;


/**
 * TODO Add Description for this type.
 * 
 * @author Civic Computing Ltd
 */
public class DBCTest extends TestCase {

    public void testRequireNotNull() {
        
        // ACT
        require().notNull(new Object());
        
        try {
            require().notNull(null);
            fail("NULL should be rejected.");
        } catch (IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }
    
    public void testRequireNotEmpty() {
        
        // ACT
        require().notEmpty("foo");
        
        try {
            require().notEmpty(null);
            fail("NULL should be rejected.");
        } catch (IllegalArgumentException e) {
            assertEquals("Specified string may not be NULL.", e.getMessage());
        }
        
        try {
            require().notEmpty("");
            fail("Zero length string should be rejected.");
        } catch (IllegalArgumentException e) {
            assertEquals("Specified string must have length > 0.", e.getMessage());
        }
    }
}
