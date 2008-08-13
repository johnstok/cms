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

}
