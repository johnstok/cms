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
 * Rendering API for CCC resources.
 *
 * @author Civic Computing Ltd.
 */
public interface ResourceRenderer {

    /**
     * Lookup a resource by path and generate an appropriate response.
     *
     * @param resourcePath The absolute path to the resource.
     * @return The corresponding response.
     */
    Response render(ResourcePath resourcePath);

    /**
     * Lookup a resource by UUID and generate an appropriate response.
     *
     * @param resourceId The UUID for the resource.
     * @return The corresponding response.
     */
    Response render(UUID resourceId);

    /**
     * Generate an appropriate response for the specified resource.
     *
     * @param resource The resource to render.
     * @return The corresponding response.
     */
    Response render(Resource resource);
}
