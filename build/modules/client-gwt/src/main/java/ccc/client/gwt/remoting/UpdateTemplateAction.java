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
package ccc.client.gwt.remoting;

import ccc.api.core.Template;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;


/**
 * Remote action for template updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateTemplateAction
    extends
        RemotingAction<Template> {

    private final Template _details;


    /**
     * Constructor.
     *
     * @param details The new details for the template.
     */
    public UpdateTemplateAction(final Template details) {
        super(UI_CONSTANTS.editTemplate(), HttpMethod.PUT);
        _details = details;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return _details.self().build(InternalServices.ENCODER);
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        return writeTemplate(_details);
    }


    /** {@inheritDoc} */
    @Override
    protected Template parse(final Response response) {
        return readTemplate(response);
    }
}
