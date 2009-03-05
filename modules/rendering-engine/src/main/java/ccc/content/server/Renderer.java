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

import java.util.Map;

import ccc.domain.Resource;


/**
 * Rendering API for CCC resources.
 *
 * @author Civic Computing Ltd.
 */
public interface Renderer {


    /**
     * Generate an appropriate response for the specified resource.
     *
     * @param resource The resource to render.
     * @return The corresponding response.
     */
    Response render(Resource resource,
                    Map<String, String[]> parameters);


    /**
     * Renders the working copy of a resource.
     * First applies a working copy (if one exists) and then passes the resource
     * to {@link #render(Resource)}. We can apply the working copy because this
     * method executes outside of a transaction.
     *
     * @param resource The resource to render.
     * @return The response, ready to be written.
     */
    Response renderWorkingCopy(Resource resource,
                               Map<String, String[]> parameters);
}
