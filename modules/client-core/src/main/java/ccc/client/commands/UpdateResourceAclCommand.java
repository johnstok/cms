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
package ccc.client.commands;

import ccc.api.core.ACL;
import ccc.api.core.API;
import ccc.api.core.ResourceSummary;
import ccc.client.core.Callback;
import ccc.client.core.CallbackResponseHandler;
import ccc.client.core.Command;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.Request;


/**
 * Command for resource's ACL updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceAclCommand  extends
Command<API, Void> {

    private final ResourceSummary _resource;
    private final ACL _acl;

    /**
     * Constructor.
     *
     * @param resource The resource to update.
     * @param acl The updated access control list.
     */
    public UpdateResourceAclCommand(final ResourceSummary resource,
                                    final ACL acl) {
        _resource = resource;
        _acl = acl;
    }

    @Override
    public void invoke(final API subject, final Callback<Void> callback) {
        final String path =
            getBaseUrl()+_resource.acl().build(InternalServices.encoder);

        final Request r =
            new Request(
                HttpMethod.POST,
                path,
                writeACL(_acl),
                new CallbackResponseHandler<Void>(
                    I18n.uiConstants.updateRoles(),
                    callback,
                    voidParser()));

        getExecutor().invokeRequest(r);
    }

}
