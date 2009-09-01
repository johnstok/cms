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
import ccc.contentcreator.dialogs.UpdateAliasDialog;
import ccc.rest.dto.ResourceSummary;

import com.google.gwt.http.client.Response;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class OpenUpdateAliasAction
    extends
        RemotingAction {

    private final ResourceSummaryModelData _alias;
    private final ResourceSummary _targetRoot;

    /**
     * Constructor.
     * @param alias The alias to update.
     * @param targetRoot The target root of the alias.
     */
    public OpenUpdateAliasAction(final ResourceSummaryModelData alias,
                             final ResourceSummary targetRoot) {
        super(GLOBALS.uiConstants().updateAlias());
        _alias = alias;
        _targetRoot = targetRoot;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/aliases/" + _alias.getId() + "/targetname";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final String targetName = response.getText();
        new UpdateAliasDialog(
            _alias.getId(), targetName, _alias.getName(), _targetRoot)
        .show();
    }

}
