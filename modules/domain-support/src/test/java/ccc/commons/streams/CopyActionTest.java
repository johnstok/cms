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
package ccc.commons.streams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import junit.framework.TestCase;


/**
 * Tests for the {@link CopyAction} class.
 *
 * @author Civic Computing Ltd.
 */
public class CopyActionTest
    extends
        TestCase {

    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testExecuteCopy() throws Exception {

        // ARRANGE
        final byte[] bytes = {1, 2, 3, 4, 5};
        final ByteArrayOutputStream os =  new ByteArrayOutputStream();
        final ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        final CopyAction a = new CopyAction(os);

        // ACT
        a.execute(is);

        // ASSERT
        assertTrue(Arrays.equals(bytes, os.toByteArray()));
    }
}
