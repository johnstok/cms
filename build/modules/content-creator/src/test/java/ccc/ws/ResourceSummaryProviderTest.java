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
package ccc.ws;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import junit.framework.TestCase;


/**
 * Tests for the {@link ResourceSummaryCollectionProvider} class.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceSummaryProviderTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testSizeCantBeDetermined() {

        // ARRANGE
        final ResourceSummaryCollectionProvider rsp =
            new ResourceSummaryCollectionProvider();

        // ACT
        final long size = rsp.getSize(null, null, null, null, null);

        // ASSERT
        assertEquals(-1, size);

    }

    /**
     * Test.
     */
    public void testIsCollectionAcceptsExactMatch() {

        // ARRANGE
        final ResourceSummaryCollectionProvider rsp =
            new ResourceSummaryCollectionProvider();

        // ACT
        final boolean condition = rsp.isCollectionOfType(Number.class, _type);

        // ASSERT
        assertTrue("Should be true.", condition);

    }

    /**
     * Test.
     */
    public void testIsCollectionRejectsSubtypeMatch() {

        // ARRANGE
        final ResourceSummaryCollectionProvider rsp =
            new ResourceSummaryCollectionProvider();

        // ACT
        final boolean condition = rsp.isCollectionOfType(Integer.class, _type);

        // ASSERT
        assertFalse("Should be false.", condition);

    }

    /**
     * Test.
     */
    public void testIsCollectionRejectsRegularClasses() {

        // ARRANGE
        final ResourceSummaryCollectionProvider rsp =
            new ResourceSummaryCollectionProvider();
        final Type type = Object.class;

        // ACT
        final boolean condition = rsp.isCollectionOfType(Integer.class, type);

        // ASSERT
        assertFalse("Should be false.", condition);

    }

    /**
     * Minimal implementation of Josh Bloch's 'super type token'.
     *
     * @param <T> The type of the reference.
     */
    public abstract class TypeReference<T> {

        private final Type _t;

        /**
         * Constructor.
         */
        protected TypeReference() {
            final Type superclass = getClass().getGenericSuperclass();
            this._t =
                ((ParameterizedType) superclass).getActualTypeArguments()[0];
        }

        /**
         * Accessor.
         *
         * @return The referenced type.
         */
        public Type getType() {
            return this._t;
        }
    }

    private Type _type = new TypeReference<ArrayList<Number>>(){/**/}.getType();
}
