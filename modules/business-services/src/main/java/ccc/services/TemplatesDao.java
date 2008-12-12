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


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface TemplatesDao {

    /**
     * Look up all templates available.
     *
     * @return A list of templates available in the CCC.
     */
    List<Template> allTemplates();


    /**
     * TODO: Add a description of this method.
     *
     * @param templateId
     * @param templateVersion
     * @param title
     * @param description
     * @param definition
     * @param body
     */
    Template update(UUID templateId,
                    long templateVersion,
                    String title,
                    String description,
                    String definition,
                    String body);

    /**
     * Creates a new template.
     *
     * @param parentId The {@link UUID} for the containing folder.
     * @param template The template to create
     */
    void create(UUID parentId, Template template);


    /**
     * TODO: Add a description of this method.
     *
     * @param templateName
     * @return
     */
    boolean nameExists(ResourceName templateName);

}
