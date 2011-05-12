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

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Types;

import junit.framework.TestCase;

import org.h2.tools.SimpleResultSet;


/**
 * Tests for the {@link URLUserType} class.
 *
 * @author Civic Computing Ltd.
 */
public class URLUserTypeTest
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
        final URL url = new URL("http://localhost");
        final URLUserType type = new URLUserType();

        final PreparedStatementAdaptor ps = new PreparedStatementAdaptor() {

            /** {@inheritDoc} */
            @Override
            public void setString(final int index, final String value) {
                addParameter(index, value);
            }
        };

        // ACT
        type.nullSafeSet(ps, url, 0);

        // ASSERT
        assertEquals(url.toString(), ps.getParameter(0));
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testNullSafeSetHandlesNull() throws Exception {

        // ARRANGE
        final URLUserType type = new URLUserType();

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
        final URL url = new URL("http://localhost");
        final URLUserType type = new URLUserType();

        final SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("url", Types.VARCHAR, UUID_SIZE, 0);
        rs.addRow(url);
        rs.next();

        // ACT
        final URL result = type.nullSafeGet(rs, new String[] {"url"}, null);

        // ASSERT
        assertEquals(url, result);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testNullSafeGetHandlesNull() throws Exception {

        // ARRANGE
        final URLUserType type = new URLUserType();

        final SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("url", Types.VARCHAR, UUID_SIZE, 0);
        rs.addRow(new Object[] {null});
        rs.next();

        // ACT
        final URL result = type.nullSafeGet(rs, new String[] {"url"}, null);

        // ASSERT
        assertNull(result);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testNullSafeGetHandlesMalformedURL() throws Exception {

        // ARRANGE
        final URLUserType type = new URLUserType();

        final SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("url", Types.VARCHAR, UUID_SIZE, 0);
        rs.addRow(new Object[] {"/\\\"!£$%^&*()-:::"});
        rs.next();

        // ACT
        try {
            type.nullSafeGet(rs, new String[] {"url"}, null);
            fail();

        // ASSERT
        } catch (final SQLException e) {
            assertEquals(MalformedURLException.class, e.getCause().getClass());
        }
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testReplaceReturnsOriginal() throws Exception {

        // ARRANGE
        final URL url = new URL("http://localhost");
        final URLUserType type = new URLUserType();

        // ACT
        final Object replaced = type.replace(url, null, null);

        // ASSERT
        assertSame(url, replaced);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testAssembleReturnsCachedObject() throws Exception {

        // ARRANGE
        final URL url = new URL("http://localhost");
        final URLUserType type = new URLUserType();

        // ACT
        final Object assembled = type.assemble(url, null);

        // ASSERT
        assertSame(url, assembled);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testDisassembleReturnsCachedObject() throws Exception {

        // ARRANGE
        final URL url = new URL("http://localhost");
        final URLUserType type = new URLUserType();

        // ACT
        final Object disassembled = type.disassemble(url);

        // ASSERT
        assertSame(url, disassembled);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testDeepCopyReturnsInput() throws Exception {

        // ARRANGE
        final URL url = new URL("http://localhost");
        final URLUserType type = new URLUserType();

        // ACT
        final URL copy = (URL) type.deepCopy(url);

        // ASSERT
        assertSame(url, copy);
    }


    /**
     * Test.
     */
    public void testEqualsTrueWhenBothNull() {

        // ARRANGE
        final URLUserType type = new URLUserType();

        // ACT
        final boolean equal = type.equals(null, null);

        // ASSERT
        assertTrue(equal);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testEqualsFalseWhenOneIsNull() throws Exception {

        // ARRANGE
        final URLUserType type = new URLUserType();

        // ACT
        final boolean equal = type.equals(null, new URL("http://localhost"));

        // ASSERT
        assertFalse(equal);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testEqualsFalseWhenNotEqual() throws Exception {

        // ARRANGE
        final URLUserType type = new URLUserType();

        // ACT
        final boolean equal =
            type.equals(
                new URL("http://localhost"),
                new URL("http://example.com"));

        // ASSERT
        assertFalse(equal);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testEqualsTrueWhenEqual() throws Exception {

        // ARRANGE
        final URLUserType type = new URLUserType();
        final URL url = new URL("http://localhost");

        // ACT
        final boolean equal = type.equals(url, url);

        // ASSERT
        assertTrue(equal);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testHashcodeUsesSubjectHashcode() throws Exception {

        // ARRANGE
        final URLUserType type = new URLUserType();
        final URL url = new URL("http://localhost");

        // ACT
        final int hashcode = type.hashCode(url);

        // ASSERT
        assertEquals(url.hashCode(), hashcode);
    }


    /**
     * Test.
     */
    public void testMutableIsFalse() {

        // ARRANGE
        final URLUserType type = new URLUserType();

        // ACT

        // ASSERT
        assertFalse(type.isMutable());
    }


    /**
     * Test.
     */
    public void testReturnedClassIsUrl() {

        // ARRANGE
        final URLUserType type = new URLUserType();

        // ACT

        // ASSERT
        assertEquals(URL.class, type.returnedClass());
    }


    /**
     * Test.
     */
    public void testSqlTypeIsVarchar() {

        // ARRANGE
        final URLUserType type = new URLUserType();

        // ACT
        final int[] sqlTypes = type.sqlTypes();

        // ASSERT
        assertEquals(1, sqlTypes.length);
        assertEquals(Types.VARCHAR, sqlTypes[0]);
    }
}
