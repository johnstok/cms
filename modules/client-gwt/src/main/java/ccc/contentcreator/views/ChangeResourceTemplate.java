/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.views;

import java.util.Collection;
import java.util.UUID;

import ccc.api.dto.TemplateSummary;
import ccc.contentcreator.core.Editable;
import ccc.contentcreator.core.View;


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
