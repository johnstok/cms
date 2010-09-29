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
package ccc.client.remoting;

import ccc.api.core.ResourceSummary;
import ccc.api.core.Template;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.core.SingleSelectionModel;


/**
 * Retrieves details of a resource from the server.
 *
 * @author Civic Computing Ltd.
 */
public class OpenUpdateTemplateAction
    extends
        RemotingAction<Template> {

    private final ResourceSummary _template;
    private final SingleSelectionModel _table;


    /**
     * Constructor.
     * @param resourceTable The table displaying the template.
     * @param template The template to update.
     */
    public OpenUpdateTemplateAction(final ResourceSummary template,
                                    final SingleSelectionModel resourceTable) {
        super(UI_CONSTANTS.editTemplate());
        _table = resourceTable;
        _template = template;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return _template.self().build(InternalServices.ENCODER);
    }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final Template delta) {
        InternalServices.DIALOGS.editTemplate(
            delta,
            _template,
            _table)
        .show();
    }


    /** {@inheritDoc} */
    @Override
    protected Template parse(final Response response) {
        return parseTemplate(response);
    }
}
