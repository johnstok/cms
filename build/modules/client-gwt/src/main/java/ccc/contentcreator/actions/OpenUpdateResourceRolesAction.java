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
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.actions;

import java.util.ArrayList;
import java.util.List;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.UpdateResourceRolesDialog;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;

/**
 * Action to launch the 'update resource roles' dialog.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenUpdateResourceRolesAction
    extends
        RemotingAction {


    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param ssm The selection model to use.
     */
    public OpenUpdateResourceRolesAction(final SingleSelectionModel ssm) {
        super(UI_CONSTANTS.updateRoles());
        _selectionModel = ssm;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            "/resources/"
            + _selectionModel.tableSelection().getId()
            + "/roles";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final List<String> data = new ArrayList<String>();
        final JSONArray rawData =
            JSONParser.parse(response.getText()).isArray();
        for (int i=0; i<rawData.size(); i++) {
            data.add(rawData.get(i).isString().stringValue());
        }

        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        new UpdateResourceRolesDialog(
            item.getId(),
            data)
        .show();
    }
}
