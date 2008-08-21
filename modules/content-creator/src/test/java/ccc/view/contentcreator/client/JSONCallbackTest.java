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
import ccc.view.contentcreator.callbacks.JSONCallback;

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
        final Constants constants = createStrictMock(Constants.class);
        expect(constants.error()).andReturn("Error");

        final Application app = createStrictMock(Application.class);
        expect(app.constants()).andReturn(constants);
        app.alert("Error: foo"); //$NON-NLS-1$

        replay(app, constants);

        // ACT
        new TestCallback(app)
            .onFailure(new RuntimeException("foo")); //$NON-NLS-1$

        // ASSERT
        verify(app, constants);
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
        TestCallback(final Application application) {
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
