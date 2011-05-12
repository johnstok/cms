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
 * Revision      $Rev: 3212 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-10-14 14:54:42 +0100 (Thu, 14 Oct 2010) $
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.callbacks;

import ccc.api.types.CommandType;
import ccc.client.core.DefaultCallback;
import ccc.client.core.InternalServices;
import ccc.client.events.Event;


/**
 * Handles successful ACL editing of a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceAclUpdatedCallback
    extends
        DefaultCallback<Void> {

    /**
     * Constructor.
     *
     * @param actionName The action that was performed.
     */
    public ResourceAclUpdatedCallback(final String actionName) {
        super(actionName);
    }


    /** {@inheritDoc} */
    @Override
    public void onSuccess(final Void result) {
        InternalServices.remotingBus.fireEvent(
            new Event<CommandType>(CommandType.RESOURCE_CHANGE_ROLES));
    }

}
