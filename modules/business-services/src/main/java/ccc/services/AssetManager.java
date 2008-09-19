/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.services;

import java.util.List;
import java.util.UUID;

import ccc.domain.File;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.Template;


/**
 * Business methods that operate on assets.
 * TODO: Should have package scope.
 *
 * @author Civic Computing Ltd
 */
interface AssetManager {

    /**
     * Lookup a resource, given its absolute path.
     * TODO: Remove - duplicate of {@link ContentManager#lookup(ResourcePath)}.
     *
     * @param path The absolute path to the resource.
     * @param <T> The type of the resource to look up.
     * @return The resource.
     */
    <T extends Resource> T lookup(ResourcePath path);

    /**
     * Creates a new template.
     *
     * @param template The template to create
     */
    void createDisplayTemplate(Template template);

    /**
     * Create the root folder for assets.
     */
    void createRoot();

    /**
     * Lookup a resource, given its id.
     *
     * @param id The unique identifier for the resource to look up.
     * @param <T> The type of the resource to look up.
     * @return The resource.
     */
    <T extends Resource> T lookup(UUID id);

    /**
     * Look up all templates available.
     *
     * @return A list of templates available in the CCC.
     */
    List<Template> lookupTemplates();

    /**
     * Create a file.
     *
     * @param file The File to persists.
     * @param path The unique id of the folder acting as a parent for file.
     */
    void createFile(File file, final UUID path);

    /**
     * Create or retrieve a template. First try to look up this template, if it
     * exists return the current template. Otherwise persist the supplied
     * template.
     *
     * @param template An copy of the template stored in memory.
     * @return The current version of the template.
     */
    Template createOrRetrieve(Template template);

    /**
     * Update a template.
     *
     * @param t The new version of the template.
     */
    void update(Template t);

}
