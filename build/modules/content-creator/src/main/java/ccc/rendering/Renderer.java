/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.rendering;

import ccc.commons.Context;
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
