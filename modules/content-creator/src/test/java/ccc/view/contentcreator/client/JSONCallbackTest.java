/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.view.contentcreator.client;

import static org.easymock.EasyMock.*;
import junit.framework.TestCase;

import com.google.gwt.json.client.JSONValue;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class JSONCallbackTest extends TestCase {



    /**
     * Test.
     */
    public void testConstructor() {

        // ARRANGE
        final GwtApp app = createStrictMock(GwtApp.class);
        app.alert("Error: foo"); //$NON-NLS-1$
        replay(app);

        // ACT
        new TestCallback(app)
            .onFailure(new RuntimeException("foo")); //$NON-NLS-1$

        // ASSERT
        verify(app);
    }

    /**
     * TODO Add Description for this type.
     *
     * @author Civic Computing Ltd.
     */
    static final class TestCallback extends JSONCallback {

        /**
         * Constructor.
         *
         * @param application The application for this callback.
         */
        TestCallback(final GwtApp application) {
            super(application);
        }

        /** {@inheritDoc} */
        @Override
        public void onSuccess(final JSONValue result) {
            throw new UnsupportedOperationException(
                "Method not implemented."); //$NON-NLS-1$
        }
    }
}
