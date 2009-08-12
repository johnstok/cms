/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.ws;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;


/**
 * Abstract class providing helper methods for JAX-RS readers & writers.
 *
 * @author Civic Computing Ltd.
 */
public class AbstractProvider {

    /**
     * Determine if a type is a collection of the specified class.
     *
     * @param clazz The parameterized type of the collection.
     * @param type The type to check.
     * @return True if 'type' is a collection of type 'clazz', false otherwise.
     */
    protected boolean isCollectionOfType(final Class<?> clazz,
                                         final Type type) {
        if (type instanceof ParameterizedType) {
            final ParameterizedType pType = (ParameterizedType) type;
            if (Collection.class.isAssignableFrom((Class<?>) pType.getRawType())
                && pType.getActualTypeArguments()[0].equals(clazz)) {
                return true;
            }
            return false;

        }
        return false;
    }

}
