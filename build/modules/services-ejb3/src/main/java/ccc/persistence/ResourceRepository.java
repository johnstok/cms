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

import java.util.List;
import java.util.Map;
import java.util.UUID;

import ccc.api.core.PageCriteria;
import ccc.api.core.ResourceCriteria;
import ccc.api.types.ResourcePath;
import ccc.api.types.SortOrder;
import ccc.domain.FileEntity;
import ccc.domain.FolderEntity;
import ccc.domain.PageEntity;
import ccc.domain.ResourceEntity;
import ccc.domain.RevisionEntity;
import ccc.domain.TemplateEntity;
import ccc.domain.WorkingCopySupport;


/**
 * API for resource repositories.
 *
 * @author Civic Computing Ltd.
 */
public interface ResourceRepository {


    /**
     * Retrieve the history of a resource.
     *
     * @param resourceId The id of the resource whose history we will look up.
     *
     * @return The revisions for the resource.
     */
    Map<Integer, ? extends RevisionEntity<?>> history(UUID resourceId);

    /**
     * Find a resource using its unique id.
     *
     * @param <T> The type of the resource to return.
     * @param type A class representing the type of the resource to return.
     * @param id The id of the resource to find.
     *
     * @return The resource for the specified id.
     */
    <T extends ResourceEntity> T find(final Class<T> type, final UUID id);

    /**
     * Look up a resource.
     *
     * @param contentPath ResourcePath The path to the resource.
     *
     * @return Resource The resource at the specified path, or NULL if it
     *  doesn't exist.
     */
    ResourceEntity lookup(ResourcePath contentPath);

    /**
     * Look up a resource, given its CCC6 id.
     *
     * @param legacyId The CCC6 id.
     *
     * @return The corresponding resource in CCC7.
     */
    ResourceEntity lookupWithLegacyId(String legacyId);

    /**
     * Look up a resource, given its metadata key.
     *
     * @param key The metadata key.
     *
     * @return The corresponding resources in CCC7.
     */
    List<ResourceEntity> lookupWithMetadataKey(String key);

    /**
     * Lookup a root folder by name.
     *
     * @param name The name of the root folder.
     *
     * @return The corresponding folder
     */
    FolderEntity root(String name);

    /**
     * Create a new resource.
     *
     * @param newResource The new resource to add.
     */
    void create(ResourceEntity newResource);

    /**
     * List all image files.
     *
     * @param criteria The criteria for images we will look up.
     * @param f Filter resources by parent. NULL will return all.
     * @param pageNo The page of results to return.
     * @param pageSize The number of results in a page.
     *
     * @return A list of files.
     */
    List<FileEntity> images(ResourceCriteria criteria,
        FolderEntity f,
        final int pageNo,
        final int pageSize);

    /**
     * List all root folders.
     *
     * @return A list of folders.
     */
    List<FolderEntity> roots();

    /**
     * List all templates.
     * @param pageNo The page of results to return.
     * @param pageSize The number of results in a page.
     *
     * @return A list of templates
     */
    List<TemplateEntity> templates(int pageNo, int pageSize);

    /**
     * List all files.
     *
     * @return A list of files.
     */
    List<FileEntity> files();

    /**
     * List all pages.
     *
     * @return a list of pages.
     */
    List<PageEntity> pages();

    /**
     * Find a template, given its name.
     *
     * @param name The name of the template.
     *
     * @return The template with the specified name.
     */
    TemplateEntity template(String name);

    /**
     * List resources with given criteria.
     *
     * @param criteria Search criteria.
     * @param f Filter resources by parent. NULL will return all.
     * @param sort The sort results be sorted in.
     * @param sortOrder The order results be sorted in.
     * @param pageNo The page of results to return.
     * @param pageSize The number of results in a page.
     *
     * @return A list of comments.
     */
    List<ResourceEntity> list(ResourceCriteria criteria,
                              FolderEntity f,
                              String sort,
                              SortOrder sortOrder,
                              int pageNo,
                              int pageSize);

    /**
     * List resources with given criteria.
     *
     * @param criteria Search criteria.
     * @param pageNo The page of results to return.
     * @param pageSize The number of results in a page.
     *
     * @return A list of comments.
     */
    List<PageEntity> list(PageCriteria criteria,
                              int pageNo,
                              int pageSize);

    /**
     * Return count of images in the folder.
     *
     * @param criteria The search criteria.
     * @param f Filter resources by parent. NULL will return all.
     * @return The count.
     */
    long imagesCount(ResourceCriteria criteria,
                     FolderEntity f);

    /**
     * Return count of resources with given criteria.
     *
     * @param criteria Search criteria.
     * @param f Filter resources by parent. NULL will return all.
     *
     * @return The count, as a long.
     */
    long totalCount(ResourceCriteria criteria, FolderEntity f);

    /**
     * Return count of pages with given criteria.
     *
     * @param criteria Search criteria.
     *
     * @return The count, as a long.
     */
    long totalCount(PageCriteria criteria);

    /**
     * Return count of templates.
     *
     * @return The count.
     */
    long templateCount();

    /**
     * Find an entity that supports working copies.
     *
     * @param id The ID of the entity to find.
     *
     * @return The corresponding entity or NULL if no such object exists.
     */
    WorkingCopySupport<?, ?, ?> findWcAware(UUID id);
}
