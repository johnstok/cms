/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.acceptance.client.views;

import java.util.Collection;
import java.util.UUID;

import ccc.api.core.Template;
import ccc.client.core.Editable;
import ccc.client.views.ChangeResourceTemplate;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ChangeResourceTemplateFake implements ChangeResourceTemplate {

    private UUID _templateId;
    private Object _presenter;
    private boolean _showing;

    @Override
    public UUID getSelectedTemplate() {
        return _templateId;
    }

    @Override
    public void setSelectedTemplate(final UUID templateId) {
        _templateId = templateId;
    }

    @Override
    public void setTemplates(final Collection<Template> templates) {
        // NO-OP
    }

    /** {@inheritDoc} */
    @Override
    public void hide() {
        _presenter = null;
        _showing   = false;
    }


    /** {@inheritDoc} */
    @Override
    public void show(final Editable presenter) {
        _presenter = presenter;
        _showing   = true;
    }

}
