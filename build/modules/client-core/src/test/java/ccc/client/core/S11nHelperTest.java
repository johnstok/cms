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
package ccc.client.core;

import junit.framework.TestCase;
import ccc.api.types.HttpStatusCode;
import ccc.plugins.s11n.S11nException;
import ccc.plugins.s11n.Serializer;
import ccc.plugins.s11n.Serializers;


/**
 * Tests for the {@link S11nHelper} class.
 *
 * @author Civic Computing Ltd.
 */
public class S11nHelperTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testReadException() {

        // ARRANGE
        final S11nHelper s11n = new S11nHelper(new SerializersAdapter());
        final Response response =
            new Response("body", "Error", HttpStatusCode.ERROR);

        // ACT
        final Throwable t = s11n.readException(response);

        // ASSERT
        assertEquals("500 Error\n\nbody", t.getMessage());
    }


    /**
     * A serializer that always fails.
     *
     * @param <T> The type of object to serialize.
     *
     * @author Civic Computing Ltd.
     */
    private static class BadSerializer<T> implements Serializer<T> {

        BadSerializer() { super(); }

        /** {@inheritDoc} */
        @Override
        public T read(final String data) {
            throw new S11nException("For testing.");
        }

        /** {@inheritDoc} */
        @Override
        public String write(final T instance) {
            throw new S11nException("For testing.");
        }
    }


    /**
     * Factory for {@link BadSerializer}..
     *
     * @author Civic Computing Ltd.
     */
    private static class SerializersAdapter implements Serializers {

        SerializersAdapter() { super(); }

        /** {@inheritDoc} */
        @Override
        public boolean canCreate(final Class<?> clazz) {
            return true;
        }

        /** {@inheritDoc} */
        @Override
        public <T> Serializer<T> create(final Class<? extends T> clazz) {
            return new BadSerializer<T>();
        }

        /** {@inheritDoc} */
        @Override
        public Class<?> findClass(final String name) {
            return Object.class;
        }
    }
}
