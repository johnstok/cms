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


/**
 * Factory for {@link ResourceRenderer} objects.
 *
 * @author Civic Computing Ltd.
 */
public interface RendererFactory {

    /**
     * Create a new {@link ResourceRenderer} object.
     *
     * @return A {@link ResourceRenderer}.
     */
    ResourceRenderer newInstance();

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
     * Mutator for the 'root name' property.
     *
     * @param rootName The name of the root folder to look up resources from.
     */
    void setRootName(String rootName);
}
