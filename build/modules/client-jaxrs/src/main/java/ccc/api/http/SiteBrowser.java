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

import java.util.Map;

import ccc.api.core.Resource;
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
    String previewContent(ResourceSummary rs, boolean wc);

    /**
     * Preview how the specified resource will be rendered.
     *
     * @param r The resource to render.
     * @param wc True if the working copy should be rendered; false otherwise.
     *
     * @return The rendered resource as a String.
     */
    String previewContent(Resource r, boolean wc);

    /**
     * Send an HTTP post to a resource.
     *
     * @param resource The resource to request.
     *
     * @return The rendered resource as a String.
     */
    String post(ResourceSummary resource);

    /**
     * Send an HTTP post to a resource.
     * <br>The request body will be encoded as
     * application/x-www-form-urlencoded.
     *
     * @param resource The resource to request.
     * @param params   The request parameters.
     *
     * @return The rendered resource as a String.
     */
    String postUrlEncoded(ResourceSummary resource,
                          Map<String, String[]> params);

    /**
     * Send an HTTP post to a resource.
     * <br>The request body will be encoded as multipart/form-data.
     *
     * @param resource The resource to request.
     * @param params   The request parameters.
     *
     * @return The rendered resource as a String.
     */
    String postMultipart(ResourceSummary resource,
                         Map<String, String> params);

    /**
     * Send an HTTP GET for a path.
     *
     * @param absolutePath The path to GET.
     *
     * @return The response body, as a string.
     */
    String get(String absolutePath);

    /**
     * Send an HTTP GET for a path.
     *
     * @param absolutePath The path to GET.
     * @param params       The request parameters.
     *
     * @return The response body, as a string.
     */
    String get(String absolutePath, Map<String, String[]> params);
}
