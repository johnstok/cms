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
package ccc.services;

import java.util.List;
import java.util.UUID;

import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.api.TemplateDelta;


/**
 * DAO API for the {@link Template} class.
 *
 * @author Civic Computing Ltd.
 */
public interface TemplateDao {

    /** NAME : String. */
    String NAME = "TemplateDao";

    /**
     * Look up all templates available.
     *
     * @return A list of templates available in the CCC.
     */
    List<Template> allTemplates();

    /**
     * Update an existing resource.
     *
     * @param templateId The id of the template to update.
     * @param delta The changes to apply.
     * @return The updated template.
     */
    Template update(final User actor, UUID templateId, TemplateDelta delta);

    /**
     * Query if a template already exists with the specified name.
     *
     * @param templateName The name to check.
     * @return True if a template exists with the specified name, false
     *  otherwise.
     */
    boolean nameExists(ResourceName templateName);

}
