/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
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

import ccc.domain.EntityNotFoundException;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.Revision;
import ccc.domain.Template;
import ccc.types.ResourcePath;


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
     * @param rootName The name of the root folder in which the resource exists.
     *
     * @throws EntityNotFoundException If no entity exists with the specified
     *  path.
     *
     * @return Resource The resource at the specified path, or NULL if it
     *  doesn't exist.
     */
    Resource lookup(String rootName, ResourcePath contentPath)
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
     * @return A list of files.
     */
    List<File> images();

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
}
