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
import java.util.UUID;

import junit.framework.TestCase;

import org.h2.tools.SimpleResultSet;


/**
 * Tests for the {@link UUIDUserType} class.
 *
 * @author Civic Computing Ltd.
 */
public class UUIDUserTypeTest
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
        final UUID id = UUID.randomUUID();
        final UUIDUserType type = new UUIDUserType();

        final PreparedStatementAdaptor ps = new PreparedStatementAdaptor() {

            /** {@inheritDoc} */
            @Override
            public void setString(final int index, final String value) {
                addParameter(index, value);
            }
        };

        // ACT
        type.nullSafeSet(ps, id, 0);

        // ASSERT
        assertEquals(id.toString(), ps.getParameter(0));
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testNullSafeSetHandlesNull() throws Exception {

        // ARRANGE
        final UUIDUserType type = new UUIDUserType();

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
        final UUID id = UUID.randomUUID();
        final UUIDUserType type = new UUIDUserType();

        final SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("id", Types.VARCHAR, UUID_SIZE, 0);
        rs.addRow(id);
        rs.next();

        // ACT
        final UUID result = type.nullSafeGet(rs, new String[] {"id"}, null);

        // ASSERT
        assertEquals(id, result);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testNullSafeGetHandlesNull() throws Exception {

        // ARRANGE
        final UUIDUserType type = new UUIDUserType();

        final SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("id", Types.VARCHAR, UUID_SIZE, 0);
        rs.addRow(new Object[] {null});
        rs.next();

        // ACT
        final UUID result = type.nullSafeGet(rs, new String[] {"id"}, null);

        // ASSERT
        assertNull(result);
    }


    /**
     * Test.
     */
    public void testReplaceReturnsOriginal() {

        // ARRANGE
        final UUID id = UUID.randomUUID();
        final UUIDUserType type = new UUIDUserType();

        // ACT
        final Object replaced = type.replace(id, null, null);

        // ASSERT
        assertSame(id, replaced);
    }


    /**
     * Test.
     */
    public void testAssembleReturnsCachedObject() {

        // ARRANGE
        final UUID id = UUID.randomUUID();
        final UUIDUserType type = new UUIDUserType();

        // ACT
        final Object assembled = type.assemble(id, null);

        // ASSERT
        assertSame(id, assembled);
    }


    /**
     * Test.
     */
    public void testDisassembleReturnsCachedObject() {

        // ARRANGE
        final UUID id = UUID.randomUUID();
        final UUIDUserType type = new UUIDUserType();

        // ACT
        final Object disassembled = type.disassemble(id);

        // ASSERT
        assertSame(id, disassembled);
    }


    /**
     * Test.
     */
    public void testDeepCopyReturnsInput() {

        // ARRANGE
        final UUID id = UUID.randomUUID();
        final UUIDUserType type = new UUIDUserType();

        // ACT
        final UUID copy = (UUID) type.deepCopy(id);

        // ASSERT
        assertSame(id, copy);
    }


    /**
     * Test.
     */
    public void testEqualsTrueWhenBothNull() {

        // ARRANGE
        final UUIDUserType type = new UUIDUserType();

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
        final UUIDUserType type = new UUIDUserType();

        // ACT
        final boolean equal = type.equals(null, UUID.randomUUID());

        // ASSERT
        assertFalse(equal);
    }


    /**
     * Test.
     */
    public void testEqualsFalseWhenNotEqual() {

        // ARRANGE
        final UUIDUserType type = new UUIDUserType();

        // ACT
        final boolean equal = type.equals(UUID.randomUUID(), UUID.randomUUID());

        // ASSERT
        assertFalse(equal);
    }


    /**
     * Test.
     */
    public void testEqualsTrueWhenEqual() {

        // ARRANGE
        final UUIDUserType type = new UUIDUserType();
        final UUID id = UUID.randomUUID();

        // ACT
        final boolean equal = type.equals(id, id);

        // ASSERT
        assertTrue(equal);
    }


    /**
     * Test.
     */
    public void testHashcodeUsesSubjectHashcode() {

        // ARRANGE
        final UUIDUserType type = new UUIDUserType();
        final UUID id = UUID.randomUUID();

        // ACT
        final int hashcode = type.hashCode(id);

        // ASSERT
        assertEquals(id.hashCode(), hashcode);
    }


    /**
     * Test.
     */
    public void testMutableIsFalse() {

        // ARRANGE
        final UUIDUserType type = new UUIDUserType();

        // ACT

        // ASSERT
        assertFalse(type.isMutable());
    }


    /**
     * Test.
     */
    public void testReturnedClassIsUuid() {

        // ARRANGE
        final UUIDUserType type = new UUIDUserType();

        // ACT

        // ASSERT
        assertEquals(UUID.class, type.returnedClass());
    }


    /**
     * Test.
     */
    public void testSqlTypeIsVarchar() {

        // ARRANGE
        final UUIDUserType type = new UUIDUserType();

        // ACT
        final int[] sqlTypes = type.sqlTypes();

        // ASSERT
        assertEquals(1, sqlTypes.length);
        assertEquals(Types.VARCHAR, sqlTypes[0]);
    }
}
