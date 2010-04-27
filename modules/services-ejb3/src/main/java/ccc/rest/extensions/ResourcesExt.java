/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.rest.extensions;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ccc.api.types.Duration;


/**
 * Command API, used to update data in CCC.
 *
 * @author Civic Computing Ltd.
 */
@Deprecated // FIXME: Move any still used methods to the Migration API.
public interface ResourcesExt
    extends
        ActionExecutor {


    /**
     * Update the specified resource's template on the server.
     *
     * @param resourceId The id of the resource to update.
     * @param templateId The new template to set for the resource.
     */
    void updateResourceTemplate(UUID resourceId, UUID templateId);


    /**
     * Specify whether a resource should be included in a site's main menu.
     *
     * @param resourceId The id of the resource to update.
     * @param include True if the resource should be included, false otherwise.
     */
    void includeInMainMenu(UUID resourceId, boolean include);


    /**
     * Update metadata of the resource.
     *
     * @param resourceId The id of the resource to update.
     * @param title The new title to set.
     * @param description The new description to set.
     * @param tags The new tags to set.
     * @param metadata The metadata to update.
     */
    void updateMetadata(UUID resourceId,
                        String title,
                        String description,
                        Set<String> tags,
                        Map<String, String> metadata);


    /**
     * Create a working copy for the specified resource, using the specified log
     * entry.
     *
     * @param resourceId The id of the resource.
     * @param index The index number of the log entry.
     */
    void createWorkingCopy(UUID resourceId, long index);


    /**
     * Update the period that a resource should be cached for.
     *
     * @param resourceId The resource to update.
     * @param duration The cache duration.
     */
    void updateCacheDuration(UUID resourceId, Duration duration);
}
