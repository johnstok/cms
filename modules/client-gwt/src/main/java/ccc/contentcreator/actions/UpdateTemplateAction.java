/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.actions;

import java.util.UUID;

import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.RemotingAction;
import ccc.rest.dto.TemplateDelta;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Remote action for template updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateTemplateAction
    extends
        RemotingAction {

    private final UUID _template;
    private final TemplateDelta _details;


    /**
     * Constructor.
     *
     * @param details The new details for the template.
     * @param template The template to update.
     */
    public UpdateTemplateAction(final UUID template,
                                 final TemplateDelta details) {
        super(UI_CONSTANTS.editTemplate(), RequestBuilder.POST);
        _template = template;
        _details = details;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/templates/"+_template;
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        _details.toJson(json);
        return json.toString();
    }
}
