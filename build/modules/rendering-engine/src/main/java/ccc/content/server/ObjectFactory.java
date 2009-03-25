/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.content.server;

import java.io.Serializable;

import ccc.domain.User;
import ccc.services.StatefulReader;


/**
 * Factory for {@link Renderer} objects.
 *
 * @author Civic Computing Ltd.
 */
public interface ObjectFactory extends Serializable {

    /**
     * Create a new {@link Renderer} object.
     *
     * @return A {@link Renderer}.
     */
    Renderer createRenderer();

    /**
     * Accessor for the 'respect visibility' property.
     *
     * @return A boolean.
     */
    boolean getRespectVisibility();

    /**
     * Mutator for the 'respect visibility' property.
     *
     * @param newValue A string representing a boolean value. The string 'false'
     *  will be interpreted as false; all other values (including null) will be
     *  interpreted as true.
     */
    void setRespectVisibility(final String newValue);

    /**
     * Accessor.
     *
     * @return The factory's stateful reader.
     */
    StatefulReader getReader();

    User currentUser();
}
