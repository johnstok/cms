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
     * TODO: Add a description of this method.
     *
     * @return
     */
    List<Template> lookupTemplates();

}
