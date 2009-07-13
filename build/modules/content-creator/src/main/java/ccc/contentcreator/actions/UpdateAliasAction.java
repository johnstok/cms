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

import ccc.api.AliasDelta;
import ccc.api.ResourceSummary;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.GwtJson;
import ccc.contentcreator.dialogs.UpdateAliasDialog;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateAliasAction
    extends
        RemotingAction {

    private final ResourceSummaryModelData _alias;
    private final ResourceSummary _targetRoot;

    /**
     * Constructor.
     * @param alias The alias to update.
     * @param targetRoot
     */
    public UpdateAliasAction(final ResourceSummaryModelData alias,
                             final ResourceSummary targetRoot) {
        super(GLOBALS.uiConstants().updateAlias());
        _alias = alias;
        _targetRoot = targetRoot;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/aliases/" + _alias.getId() + "/delta";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONObject result = JSONParser.parse(response.getText()).isObject();
        final AliasDelta delta = new AliasDelta(new GwtJson(result));
        new UpdateAliasDialog(
            _alias.getId(), delta, _alias.getName(), _targetRoot)
        .show();
    }

}
