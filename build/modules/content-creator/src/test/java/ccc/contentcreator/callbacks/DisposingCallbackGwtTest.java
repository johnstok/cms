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
package ccc.contentcreator.callbacks;

import ccc.contentcreator.dialogs.Closeable;

import com.google.gwt.junit.client.GWTTestCase;


/**
 * Tests for the {@link DisposingCallback} class.
 *
 * @author Civic Computing Ltd.
 */
public class DisposingCallbackGwtTest
    extends
        GWTTestCase {

    private static final class SimpleCloseable
        implements
            Closeable {

        private boolean _closed = false;

        @Override public void close() {
            _closed = true;
        }
    }

    /**
     * Test.
     */
    public void testCallbackCanCloseCloseable() {

        // Arrange
        final SimpleCloseable c = new SimpleCloseable();

        // ACT
        new DisposingCallback(c, "").onSuccess(null);

        // ASSERT
        assertTrue(c._closed);
    }

    /** {@inheritDoc} */
    @Override
    public String getModuleName() {
        return "ccc.contentcreator.ContentCreator";
    }
}
