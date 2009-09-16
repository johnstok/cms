/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.views;

import java.util.Collection;
import java.util.UUID;

import ccc.contentcreator.client.Editable;
import ccc.contentcreator.client.View;
import ccc.rest.dto.TemplateSummary;


/**
 * MVP View for changing a resource's template.
 *
 * @author Civic Computing Ltd.
 */
public interface ChangeResourceTemplate
    extends
        View<Editable> {

    /**
     * Mutator.
     *
     * @param templates The list of available templates to choose from.
     */
    void setTemplates(Collection<TemplateSummary> templates);

    /**
     * Mutator.
     *
     * @param templateId The currently selected template.
     */
    void setSelectedTemplate(UUID templateId);

    /**
     * Accessor.
     *
     * @return The currently selected template.
     */
    UUID getSelectedTemplate();

}
