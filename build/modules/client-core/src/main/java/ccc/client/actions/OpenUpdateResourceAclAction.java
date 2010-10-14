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
package ccc.client.actions;

import java.util.Collection;

import ccc.api.core.ACL;
import ccc.api.core.Group;
import ccc.api.core.ResourceSummary;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.core.SingleSelectionModel;

/**
 * Action to launch the 'update resource acl' dialog.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenUpdateResourceAclAction
    extends
        RemotingAction<ACL> {


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
        final ResourceSummary delegate =
            _selectionModel.tableSelection();
        return delegate.acl().build(InternalServices.encoder);
    }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final ACL acl) {
        final ResourceSummary item = _selectionModel.tableSelection();
        InternalServices.dialogs.updateAcl(
            item,
            acl,
            _groups)
        .show();
    }


    /** {@inheritDoc} */
    @Override
    protected ACL parse(final Response response) {
        return readACL(response);
    }
}
