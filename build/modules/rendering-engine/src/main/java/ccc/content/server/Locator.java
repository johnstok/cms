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

import java.util.UUID;

import ccc.domain.Resource;
import ccc.domain.ResourcePath;


/**
 * Locate a resource in CCC.
 *
 * @author Civic Computing Ltd.
 */
public interface Locator {

    /**
     * Lookup a resource by path.
     *
     * @param resourcePath The absolute path to the resource.
     * @return The corresponding resource.
     */
    Resource locate(ResourcePath resourcePath);

    /**
     * Lookup a resource by UUID.
     *
     * @param resourceId The UUID for the resource.
     * @return The corresponding resource.
     */
    Resource locate(UUID resourceId);
}
