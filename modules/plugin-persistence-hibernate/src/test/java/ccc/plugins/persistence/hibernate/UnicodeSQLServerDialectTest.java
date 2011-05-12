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

import static ccc.plugins.persistence.hibernate.UnicodeSQLServerDialect.*;

import java.sql.Types;

import junit.framework.TestCase;


/**
 * Tests for the {@link UnicodeSQLServerDialect} class.
 *
 * @author Civic Computing Ltd.
 */
public class UnicodeSQLServerDialectTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testColumnTypesResgiteredCorrectly() {

        // ARRANGE
        final UnicodeSQLServerDialect dialect = new UnicodeSQLServerDialect();

        // ACT

        // ASSERT
        assertEquals(
            COLUMN_TYPE_CHAR, dialect.getTypeName(Types.CHAR));
        assertEquals(
            COLUMN_TYPE_VARCHAR, dialect.getTypeName(Types.VARCHAR));
        assertEquals(
            COLUMN_TYPE_VARCHAR, dialect.getTypeName(Types.LONGVARCHAR));
        assertEquals(
            COLUMN_TYPE_CLOB, dialect.getTypeName(Types.CLOB));
    }
}
