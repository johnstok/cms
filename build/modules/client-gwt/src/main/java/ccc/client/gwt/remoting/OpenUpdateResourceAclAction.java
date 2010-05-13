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
package ccc.client.gwt.remoting;

import java.util.Collection;

import ccc.api.core.ACL;
import ccc.api.core.Group;
import ccc.api.core.Resource;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Response;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.client.gwt.views.gxt.UpdateResourceAclDialog;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * Action to launch the 'update resource acl' dialog.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenUpdateResourceAclAction
    extends
        RemotingAction {


    private final SingleSelectionModel _selectionModel;
    private Collection<Group> _groups;

    /**
     * Constructor.
     *
     * @param ssm The selection model to use.
     * @param groups All groups available on the server.
     */
    public OpenUpdateResourceAclAction(final SingleSelectionModel ssm,
                                         final Collection<Group> groups) {
        super(UI_CONSTANTS.updateRoles());
        _selectionModel = ssm;
        _groups = groups;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return _selectionModel.tableSelection().getDelegate().acl().build(new GWTTemplateEncoder());
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {

        final JSONObject o = JSONParser.parse(response.getText()).isObject();
        final ACL acl = new ACL(new GwtJson(o));

        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        new UpdateResourceAclDialog(
            item.getDelegate(),
            acl,
            _groups)
        .show();
    }
}
