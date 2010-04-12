/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.jaxrs.providers;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import ccc.api.jaxrs.providers.JsonableCollectionWriter;

import junit.framework.TestCase;


/**
 * Tests for the {@link JsonableCollectionWriter} class.
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
        final JsonableCollectionWriter rsp =
            new JsonableCollectionWriter();

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
        final JsonableCollectionWriter rsp =
            new JsonableCollectionWriter();

        // ACT
        final boolean condition = rsp.isCollectionOfType(Integer.class, _type);

        // ASSERT
        assertTrue("Should be true.", condition);

    }

    /**
     * Test.
     */
    public void testIsCollectionAcceptsSupertypeMatch() {

        // ARRANGE
        final JsonableCollectionWriter rsp =
            new JsonableCollectionWriter();

        // ACT
        final boolean condition = rsp.isCollectionOfType(Number.class, _type);

        // ASSERT
        assertTrue("Should be true.", condition);

    }

    /**
     * Test.
     */
    public void testIsCollectionRejectsRegularClasses() {

        // ARRANGE
        final JsonableCollectionWriter rsp =
            new JsonableCollectionWriter();
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

    private Type _type =
        new TypeReference<ArrayList<Integer>>(){/**/}.getType();
}
