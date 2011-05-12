/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.plugins.persistence.hibernate;

import java.sql.Types;
import java.util.Properties;

import junit.framework.TestCase;

import org.h2.tools.SimpleResultSet;
import org.hibernate.HibernateException;


/**
 * Tests for the {@link EnumUserType} class.
 *
 * @author Civic Computing Ltd.
 */
public class EnumUserTypeTest
    extends
        TestCase {

    private static final int UUID_SIZE = 36;


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testNullSafeSetHandlesUuid() throws Exception {

        // ARRANGE
        final EnumUserType<Suit> type = new EnumUserType<Suit>(Suit.class);

        final PreparedStatementAdaptor ps = new PreparedStatementAdaptor() {

            /** {@inheritDoc} */
            @Override
            public void setString(final int index, final String value) {
                addParameter(index, value);
            }
        };

        // ACT
        type.nullSafeSet(ps, Suit.CLUBS, 0);

        // ASSERT
        assertEquals(Suit.CLUBS.name(), ps.getParameter(0));
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testNullSafeSetHandlesNull() throws Exception {

        // ARRANGE
        final EnumUserType<Suit> type = new EnumUserType<Suit>(Suit.class);

        final PreparedStatementAdaptor ps = new PreparedStatementAdaptor() {

            /** {@inheritDoc} */
            @Override
            public void setNull(final int index, final int sqlType) {
                addParameter(index, null);
            }
        };

        // ACT
        type.nullSafeSet(ps, null, 0);

        // ASSERT
        assertTrue(ps.isParameterNull(0));
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testNullSafeGetHandlesUuid() throws Exception {

        // ARRANGE
        final EnumUserType<Suit> type = new EnumUserType<Suit>(Suit.class);

        final SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("suit", Types.VARCHAR, UUID_SIZE, 0);
        rs.addRow(Suit.CLUBS);
        rs.next();

        // ACT
        final Suit result = type.nullSafeGet(rs, new String[] {"suit"}, null);

        // ASSERT
        assertEquals(Suit.CLUBS, result);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testNullSafeGetHandlesNull() throws Exception {

        // ARRANGE
        final EnumUserType<Suit> type = new EnumUserType<Suit>(Suit.class);

        final SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("id", Types.VARCHAR, UUID_SIZE, 0);
        rs.addRow(new Object[] {null});
        rs.next();

        // ACT
        final Suit result = type.nullSafeGet(rs, new String[] {"id"}, null);

        // ASSERT
        assertNull(result);
    }


    /**
     * Test.
     */
    public void testConstructWithPropertiesHandlesMissingType() {

        // ARRANGE
        final Properties props = new Properties();

        // ACT
        EnumUserType<Suit> type;
        try {
            type = new EnumUserType<Suit>();
            type.setParameterValues(props);
            fail();

        // ASSERT
        } catch (final HibernateException e) {
            assertEquals("Enum class not found: null", e.getMessage());
        }
    }


    /**
     * Test.
     */
    public void testConstructWithProperties() {

        // ARRANGE
        final Properties props = new Properties();
        props.setProperty(EnumUserType.PROPERTY_TYPE, Suit.class.getName());

        // ACT
        final EnumUserType<Suit> type = new EnumUserType<Suit>(Suit.class);
        type.setParameterValues(props);

        // ASSERT
        assertEquals(Suit.class, type.returnedClass());
    }


    /**
     * Test.
     */
    public void testReplaceReturnsOriginal() {

        // ARRANGE
        final EnumUserType<Suit> type = new EnumUserType<Suit>(Suit.class);

        // ACT
        final Object replaced = type.replace(Suit.CLUBS, null, null);

        // ASSERT
        assertSame(Suit.CLUBS, replaced);
    }


    /**
     * Test.
     */
    public void testAssembleReturnsCachedObject() {

        // ARRANGE
        final EnumUserType<Suit> type = new EnumUserType<Suit>(Suit.class);

        // ACT
        final Object assembled = type.assemble(Suit.CLUBS, null);

        // ASSERT
        assertSame(Suit.CLUBS, assembled);
    }


    /**
     * Test.
     */
    public void testDisassembleReturnsCachedObject() {

        // ARRANGE
        final EnumUserType<Suit> type = new EnumUserType<Suit>(Suit.class);

        // ACT
        final Object disassembled = type.disassemble(Suit.CLUBS);

        // ASSERT
        assertSame(Suit.CLUBS, disassembled);
    }


    /**
     * Test.
     */
    public void testDeepCopyReturnsInput() {

        // ARRANGE
        final EnumUserType<Suit> type = new EnumUserType<Suit>(Suit.class);

        // ACT
        final Suit copy = (Suit) type.deepCopy(Suit.CLUBS);

        // ASSERT
        assertSame(Suit.CLUBS, copy);
    }


    /**
     * Test.
     */
    public void testEqualsTrueWhenBothNull() {

        // ARRANGE
        final EnumUserType<Suit> type = new EnumUserType<Suit>(Suit.class);

        // ACT
        final boolean equal = type.equals(null, null);

        // ASSERT
        assertTrue(equal);
    }


    /**
     * Test.
     */
    public void testEqualsFalseWhenOneIsNull() {

        // ARRANGE
        final EnumUserType<Suit> type = new EnumUserType<Suit>(Suit.class);

        // ACT
        final boolean equal = type.equals(null, Suit.CLUBS);

        // ASSERT
        assertFalse(equal);
    }


    /**
     * Test.
     */
    public void testEqualsFalseWhenNotEqual() {

        // ARRANGE
        final EnumUserType<Suit> type = new EnumUserType<Suit>(Suit.class);

        // ACT
        final boolean equal = type.equals(Suit.CLUBS, Suit.HEARTS);

        // ASSERT
        assertFalse(equal);
    }


    /**
     * Test.
     */
    public void testEqualsTrueWhenEqual() {

        // ARRANGE
        final EnumUserType<Suit> type = new EnumUserType<Suit>(Suit.class);

        // ACT
        final boolean equal = type.equals(Suit.CLUBS, Suit.CLUBS);

        // ASSERT
        assertTrue(equal);
    }


    /**
     * Test.
     */
    public void testHashcodeUsesSubjectHashcode() {

        // ARRANGE
        final EnumUserType<Suit> type = new EnumUserType<Suit>(Suit.class);

        // ACT
        final int hashcode = type.hashCode(Suit.CLUBS);

        // ASSERT
        assertEquals(Suit.CLUBS.hashCode(), hashcode);
    }


    /**
     * Test.
     */
    public void testMutableIsFalse() {

        // ARRANGE
        final EnumUserType<Suit> type = new EnumUserType<Suit>(Suit.class);

        // ACT

        // ASSERT
        assertFalse(type.isMutable());
    }


    /**
     * Test.
     */
    public void testReturnedClassIsEnumSubclass() {

        // ARRANGE
        final EnumUserType<Suit> type = new EnumUserType<Suit>(Suit.class);

        // ACT

        // ASSERT
        assertEquals(Suit.class, type.returnedClass());
    }


    /**
     * Test.
     */
    public void testSqlTypeIsVarchar() {

        // ARRANGE
        final EnumUserType<Suit> type = new EnumUserType<Suit>(Suit.class);

        // ACT
        final int[] sqlTypes = type.sqlTypes();

        // ASSERT
        assertEquals(1, sqlTypes.length);
        assertEquals(Types.VARCHAR, sqlTypes[0]);
    }


    /**
     * Example enum for testing.
     *
     * @author Civic Computing Ltd.
     */
    private enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }
}
