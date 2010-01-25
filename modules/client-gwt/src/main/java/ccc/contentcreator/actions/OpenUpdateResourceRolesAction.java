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

import java.util.Collection;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.GwtJson;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.UpdateResourceRolesDialog;
import ccc.rest.dto.AclDto;
import ccc.rest.dto.GroupDto;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
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
    private Collection<GroupDto> _groups;

    /**
     * Constructor.
     *
     * @param ssm The selection model to use.
     * @param groups All groups available on the server.
     */
    public OpenUpdateResourceRolesAction(final SingleSelectionModel ssm,
                                         final Collection<GroupDto> groups) {
        super(UI_CONSTANTS.updateRoles());
        _selectionModel = ssm;
        _groups = groups;
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

        final JSONObject o = JSONParser.parse(response.getText()).isObject();
        final AclDto acl = new AclDto(new GwtJson(o));

        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        new UpdateResourceRolesDialog(
            item.getId(),
            acl,
            _groups)
        .show();
    }
}
