/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.http;

import ccc.api.core.ResourceSummary;


/**
 * API for browsing rendered resources.
 *
 * @author Civic Computing Ltd.
 */
public interface SiteBrowser {

    /**
     * Preview how the specified resource will be rendered.
     *
     * @param rs The resource to render.
     * @param wc True if the working copy should be rendered; false otherwise.
     *
     * @return The rendered resource as a String.
     */
    String previewContent(final ResourceSummary rs, final boolean wc);
    
    /**
     * Preview how the specified resource will be rendered with a template.
     *
     * @param rs The resource to render.
     * @param body Template body as a String.
     *
     * @return The rendered resource as a String.
     */
    String previewTemplate(final ResourceSummary rs, final String body);

    /**
     * Send an HTTP post to a resource.
     *
     * @param rs The resource to request.
     *
     * @return The rendered resource as a String.
     */
    String post(final ResourceSummary rs);

    /**
     * Send an HTTP GET for a path.
     *
     * @param path The path to GET.
     *
     * @return The response body, as a string.
     */
    String get(String absolutePath);
}
