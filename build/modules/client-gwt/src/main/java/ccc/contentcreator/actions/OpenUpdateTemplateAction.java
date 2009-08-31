/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.actions;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.GwtJson;
import ccc.contentcreator.client.ResourceTable;
import ccc.contentcreator.dialogs.EditTemplateDialog;
import ccc.rest.TemplateDelta;

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
        return "/templates/"+_template.getId()+"/delta";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONObject result =
            JSONParser.parse(response.getText()).isObject();
        final TemplateDelta delta = new TemplateDelta(new GwtJson(result));
        new EditTemplateDialog(
            delta,
            _template,
            _table)
        .show();
    }
}
