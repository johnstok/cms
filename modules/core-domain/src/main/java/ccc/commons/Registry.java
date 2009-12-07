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
package ccc.commons;


/**
 * A public API for a registry. Registries are used look up other objects. See
 * See Martin Fowler's description
 * (http://martinfowler.com/eaaCatalog/registry.html) for more details.
 *
 * @author Civic Computing Ltd.
 */
public interface Registry {

    /**
     * Get an object from the registry.
     *
     * @param <T> The type of the object at the specified location.
     * @param location Location in the registry.
     * @return The object at the specified location or NULL if the location is
     *         not found.
     */
    <T> T get(final String location);

    /**
     * Put an object into JNDI.
     *
     * @param location Location in the in the registry.
     * @param object The object to put.
     * @return 'this' to allow a registry to be used in a 'fluently'.
     */
    Registry put(final String location, final Object object);
}
