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
}
