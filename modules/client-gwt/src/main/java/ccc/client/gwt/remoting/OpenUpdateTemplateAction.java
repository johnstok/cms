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
import ccc.api.types.URIBuilder;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.views.gxt.EditTemplateDialog;
import ccc.client.gwt.widgets.ResourceTable;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class OpenUpdateTemplateAction
    extends
        RemotingAction {

    private final ResourceSummaryModelData _template;
    private final ResourceTable _table;

    /**
     * Constructor.
     * @param resourceTable The table displaying the template.
     * @param template The template to update.
     */
    public OpenUpdateTemplateAction(final ResourceSummaryModelData template,
                                final ResourceTable resourceTable) {
        super(GLOBALS.uiConstants().editTemplate());
        _table = resourceTable;
        _template = template;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            new URIBuilder(Template.COLLECTION+Template.DELTA)
            .replace("id", _template.getId().toString())
            .toString();
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONObject result =
            JSONParser.parse(response.getText()).isObject();
        final Template delta = new Template();
        delta.fromJson(new GwtJson(result));
        new EditTemplateDialog(
            delta,
            _template,
            _table)
        .show();
    }
}
