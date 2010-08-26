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

package ccc.plugins.s11n;


/**
 * Serializer factory.
 *
 * @author Civic Computing Ltd.
 */
public interface Serializers {


    /**
     * Create a serializer for a specified class.
     *
     * @param <T> The type of serializer to create.
     * @param clazz Class representing the type to serialize.
     *
     * @return The corresponding serializer or NULL if no serializer is
     *  available.
     */
    <T> Serializer<T> create(final Class<T> clazz);


    /**
     * Query if a serializer is available for a specified class.
     *
     * @param clazz The class to check.
     *
     * @return True if a serializer is available; false otherwise.
     */
    boolean canCreate(final Class<?> clazz);


    /**
     * Check if a class is supported by the serializer.
     *
     * @param name The name of the class to serialize.
     *
     * @return The corresponding class, or NULL if the name isn't supported.
     */
    Class<?> findClass(final String name);
}