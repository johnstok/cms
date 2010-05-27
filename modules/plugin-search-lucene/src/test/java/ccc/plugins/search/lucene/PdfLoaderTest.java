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
package ccc.plugins.search.lucene;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;
import ccc.commons.Resources;


/**
 * Tests for the {@link PdfLoader} class.
 *
 * @author Civic Computing Ltd.
 */
public class PdfLoaderTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testHandlesNullInputStream() {

        // ARRANGE
        final PdfLoader pl = new PdfLoader();

        // ACT
        pl.execute(null);

        // ASSERT
        assertEquals("", pl.getText());
    }


    /**
     * Test.
     */
    public void testHandlesZeroLengthInput() {

        // ARRANGE
        final PdfLoader pl = new PdfLoader();

        // ACT
        pl.execute(new ByteArrayInputStream(new byte[] {}));

        // ASSERT
        assertEquals("", pl.getText());
    }


    /**
     * Test.
     */
    public void testHandlesBadInput() {

        // ARRANGE
        final PdfLoader pl = new PdfLoader();

        // ACT
        pl.execute(Resources.open("foo.doc"));

        // ASSERT
        assertEquals("", pl.getText());
    }


    /**
     * Test.
     */
    public void testHandlesGoodInput() {

        // ARRANGE
        final PdfLoader pl = new PdfLoader();

        // ACT
        pl.execute(Resources.open("foo.pdf"));

        // ASSERT
        assertEquals("foo\n", pl.getText());
    }
}
