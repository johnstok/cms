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
package ccc.persistence;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.Revision;
import ccc.domain.Template;
import ccc.rest.EntityNotFoundException;
import ccc.types.ResourcePath;
import ccc.types.SortOrder;


/**
 * API for resource repositories.
 *
 * @author Civic Computing Ltd.
 */
public interface ResourceRepository {

    /**
     * List all locked resources.
     *
     * @return The list of resources.
     */
    List<Resource> locked();

    /**
     * Retrieve the history of a resource.
     *
     * @param resourceId The id of the resource whose history we will look up.
     *
     * @throws EntityNotFoundException If no entity exists with the specified
     *  uuid.
     *
     * @return The revisions for the resource.
     */
    Map<Integer, ? extends Revision<?>> history(UUID resourceId)
    throws EntityNotFoundException;

    /**
     * Find a resource using its unique id.
     *
     * @param <T> The type of the resource to return.
     * @param type A class representing the type of the resource to return.
     * @param id The id of the resource to find.
     *
     * @throws EntityNotFoundException If no entity exists with the specified
     *  uuid.
     *
     * @return The resource for the specified id.
     */
    <T extends Resource> T find(final Class<T> type, final UUID id)
    throws EntityNotFoundException;

    /**
     * Look up a resource.
     *
     * @param contentPath ResourcePath The path to the resource.
     *
     * @throws EntityNotFoundException If no entity exists with the specified
     *  path.
     *
     * @return Resource The resource at the specified path, or NULL if it
     *  doesn't exist.
     */
    Resource lookup(ResourcePath contentPath)
    throws EntityNotFoundException;

    /**
     * Look up a resource, given its CCC6 id.
     *
     * @param legacyId The CCC6 id.
     *
     * @throws EntityNotFoundException If no entity exists with the specified
     *  id.
     *
     * @return The corresponding resource in CCC7.
     */
    Resource lookupWithLegacyId(String legacyId) throws EntityNotFoundException;

    /**
     * Look up a resource, given its metadata key.
     *
     * @param key The metadata key.
     *
     * @return The corresponding resources in CCC7.
     */
    List<Resource> lookupWithMetadataKey(String key);

    /**
     * Lookup a root folder by name.
     *
     * @param name The name of the root folder.
     *
     * @throws EntityNotFoundException If no root exists with the specified id.
     *
     * @return The corresponding folder
     */
    Folder root(String name) throws EntityNotFoundException;

    /**
     * Create a new resource.
     *
     * @param newResource The new resource to add.
     */
    void create(Resource newResource);

    /**
     * List all image files.
     *
     * @param folderId The id of the folder whose images we will look up.
     * @param pageNo The page of results to return.
     * @param pageSize The number of results in a page.
     *
     * @return A list of files.
     * @throws EntityNotFoundException If no folder exists with the id.
     */
    List<File> images(UUID folderId, final int pageNo,
        final int pageSize) throws EntityNotFoundException;

    /**
     * List all root folders.
     *
     * @return A list of folders.
     */
    List<Folder> roots();

    /**
     * List all templates.
     *
     * @return A list of templates
     */
    List<Template> templates();

    /**
     * List all files.
     *
     * @return A list of files.
     */
    List<File> files();

    /**
     * List all pages.
     *
     * @return a list of pages.
     */
    List<Page> pages();

    /**
     * Find a template, given its name.
     *
     * @param name The name of the template.
     *
     * @throws EntityNotFoundException If no template exists with the specified
     *  name.
     *
     * @return The template with the specified name.
     */
    Template template(String name) throws EntityNotFoundException;

    /**
     * List folder children.
     *
     * @param parent Filter resources by parent. NULL will return all.
     * @param tag Filter resources by tag. NULL will return all.
     * @param after Only return comments created after this date.
     * @param before Only return comments before this date.
     * @param sort The sort results be sorted in.
     * @param sortOrder The order results be sorted in.
     * @param pageNo The page of results to return.
     * @param pageSize The number of results in a page.
     *
     * @return A list of comments.
     */
    List<Resource> list(Resource parent,
                        String tag,
                        Date before,
                        Date after,
                        String sort,
                        SortOrder sortOrder,
                        int pageNo,
                        int pageSize);

    /**
     * Return count of images in the folder.
     *
     * @param folderId The id of the folder whose images we will look up.
     * @return The count.
     * @throws EntityNotFoundException If no folder exists with the id.
     */
    long imagesCount(UUID folderId) throws EntityNotFoundException;
}
