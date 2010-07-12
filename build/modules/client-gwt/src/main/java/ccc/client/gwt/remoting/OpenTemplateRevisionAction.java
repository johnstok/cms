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
package ccc.client.gwt.remoting;

import ccc.api.core.ResourceSummary;
import ccc.api.core.Template;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.views.gxt.PreviewTemplateDialog;
import ccc.plugins.s11n.json.TemplateSerializer;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * Action to open specific revision of a template.
 *
 * @author Civic Computing Ltd.
 */
public class OpenTemplateRevisionAction
    extends
        RemotingAction {

    private final ResourceSummary _template;
    private final long _index;

    /**
     * Constructor.
     *
     * @param template The template to update.
     * @param index The index
     */
    public OpenTemplateRevisionAction(final ResourceSummary template,
                                      final long index) {
        super(UI_CONSTANTS.editTemplate());
        _template = template;
        _index = index;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return _template.templateRevision().build(
            "revision", ""+_index, new GWTTemplateEncoder());
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONObject result =
            JSONParser.parse(response.getText()).isObject();
        final Template delta =
            new TemplateSerializer().read(new GwtJson(result));
        new PreviewTemplateDialog(delta)
        .show();
    }
}
