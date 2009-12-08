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