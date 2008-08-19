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
import ccc.domain.Template;


/**
 * Business methods that operate on assets.
 *
 * @author Civic Computing Ltd
 */
public interface AssetManager {

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
     * TODO: Add a description of this method.
     *
     * @param file The File object to create
     */
    void createFile(File file);

    /**
     * Create or retrieve a template. First try to look up this template, if it
     * exists return the current template. Otherwise persist the supplied
     * template.
     *
     * @param template An copy of the template stored in memory.
     * @return The current version of the template.
     */
    Template createOrRetrieve(Template template);

}
