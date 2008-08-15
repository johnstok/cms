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
