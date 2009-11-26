/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.security;

import java.security.Principal;
import java.security.acl.Group;

import junit.framework.TestCase;


/**
 * Tests for the {@link SimpleGroup} class.
 *
 * @author Civic Computing Ltd.
 */
public class SimpleGroupTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testAddNewMember() {

        // ARRANGE
        final Principal p = new SimplePrincipal("test principal");
        final Group g = new SimpleGroup("test group");

        // ACT
        final boolean added  = g.addMember(p);

        // ASSERT
        assertTrue(added);
    }

    /**
     * Test.
     */
    public void testAddExistingMember() {

        // ARRANGE
        final Principal p = new SimplePrincipal("test principal");
        final Group g = new SimpleGroup("test group");
        g.addMember(p);

        // ACT
        final boolean added  = g.addMember(p);

        // ASSERT
        assertFalse(added);
    }

    /**
     * Test.
     */
    public void testRemoveMemberReturnsTrue() {

        // ARRANGE
        final Principal p = new SimplePrincipal("test principal");
        final Group g = new SimpleGroup("test group");
        g.addMember(p);

        // ACT
        final boolean removed  = g.removeMember(p);

        // ASSERT
        assertTrue(removed);
    }

    /**
     * Test.
     */
    public void testRemoveNonmemberReturnsFalse() {

        // ARRANGE
        final Principal p = new SimplePrincipal("test principal");
        final Group g = new SimpleGroup("test group");

        // ACT
        final boolean removed  = g.removeMember(p);

        // ASSERT
        assertFalse(removed);
    }

    /**
     * Test.
     */
    public void testIsMemberReturnsFalseForNonmember() {

        // ARRANGE
        final Principal p = new SimplePrincipal("test principal");
        final Group g = new SimpleGroup("test group");

        // ACT
        final boolean isMember = g.isMember(p);

        // ASSERT
        assertFalse(isMember);
    }

    /**
     * Test.
     */
    public void testIsMemberReturnsTrueForMember() {

        // ARRANGE
        final Principal p = new SimplePrincipal("test principal");
        final Group g = new SimpleGroup("test group");
        g.addMember(p);

        // ACT
        final boolean isMember = g.isMember(p);

        // ASSERT
        assertTrue(isMember);
    }

    /**
     * Test.
     */
    public void testIsMemberReturnsFalseForNull() {

        // ARRANGE
        final Principal p = new SimplePrincipal("test principal");
        final Group g = new SimpleGroup("test group");
        g.addMember(p);

        // ACT
        final boolean isMember = g.isMember(null);

        // ASSERT
        assertFalse(isMember);
    }

    /**
     * Test.
     */
    public void testIsMemberReturnsTrueForNestedMember() {

        // ARRANGE
        final Principal p = new SimplePrincipal("test principal");
        final Group g = new SimpleGroup("test group");
        final Group n = new SimpleGroup("nested group");
        g.addMember(n);
        n.addMember(p);

        // ACT
        final boolean isMember = g.isMember(p);

        // ASSERT
        assertTrue(isMember);
    }
}
