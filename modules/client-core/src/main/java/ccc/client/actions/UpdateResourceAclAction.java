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
package ccc.client.actions;

import ccc.api.core.ACL;
import ccc.api.core.ResourceSummary;
import ccc.client.callbacks.ResourceAclUpdatedCallback;
import ccc.client.commands.UpdateResourceAclCommand;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;


/**
 * Remote action for resource's ACL updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceAclAction
    extends
        RemotingAction<Void> {

    private final ResourceSummary _resource;
    private final ACL _acl;


    /**
     * Constructor.
     *
     * @param resource The resource to update.
     * @param acl The updated access control list.
     */
    public UpdateResourceAclAction(final ResourceSummary resource,
                                   final ACL acl) {
        super(UI_CONSTANTS.updateRoles(), HttpMethod.POST);
        _resource = resource;
        _acl = acl;
    }

    /** {@inheritDoc} */
    @Override
    public void execute() {
        new UpdateResourceAclCommand(_resource, _acl).invoke(
            InternalServices.api,
            new ResourceAclUpdatedCallback(I18n.uiConstants.updateRoles()));
    }
}
