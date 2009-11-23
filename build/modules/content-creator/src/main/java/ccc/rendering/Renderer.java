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
package ccc.rendering;

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
     * @param context The rendering context.
     *
     * @return The corresponding response.
     */
    Response render(Resource resource, Context context);


    /**
     * Renders the working copy of a resource.
     *
     * @param resource The resource to render.
     * @param context The rendering context.
     *
     * @return The response, ready to be written.
     */
    Response renderWorkingCopy(Resource resource, Context context);


    /**
     * Renders a historical version of a resource.
     *
     * @param resource The resource to render.
     * @param version The version of the resource to render.
     * @param context The rendering context.
     *
     * @return The response, ready to be written.
     */
    Response renderHistoricalVersion(Resource resource,
                                     String version,
                                     Context context);
}
