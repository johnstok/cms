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
 * Tests for the {@link ResourceSummaryProvider} class.
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
        final ResourceSummaryProvider rsp = new ResourceSummaryProvider();

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
        final ResourceSummaryProvider rsp = new ResourceSummaryProvider();

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
        final ResourceSummaryProvider rsp = new ResourceSummaryProvider();

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
        final ResourceSummaryProvider rsp = new ResourceSummaryProvider();
        final Type type = Object.class;

        // ACT
        final boolean condition = rsp.isCollectionOfType(Integer.class, type);

        // ASSERT
        assertFalse("Should be false.", condition);

    }

    public abstract class TypeReference<T> {

        private final Type type;

        protected TypeReference() {
            final Type superclass = getClass().getGenericSuperclass();
            this.type =
                ((ParameterizedType) superclass).getActualTypeArguments()[0];
        }

        /**
         * Gets the referenced type.
         */
        public Type getType() {
            return this.type;
        }
    }

    private Type _type = new TypeReference<ArrayList<Number>>(){/**/}.getType();
}
