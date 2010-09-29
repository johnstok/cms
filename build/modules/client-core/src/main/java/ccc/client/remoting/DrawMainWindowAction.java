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
package ccc.client.remoting;

import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceSummary;
import ccc.api.core.User;
import ccc.client.core.InternalServices;

/**
 * Draws the GXT client's main window.
 *
 * @author Civic Computing Ltd.
 */
public final class DrawMainWindowAction
    extends
        GetRootsAction {

    /** _user : UserSummary. */
    private final User _user;

    /**
     * Constructor.
     *
     * @param user The currently logged in user.
     */
    public DrawMainWindowAction(final User user) {
        _user = user;
    }

    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final PagedCollection<ResourceSummary> roots) {
        InternalServices.ROOTS = roots;
        InternalServices.DIALOGS.mainWindow(_user);

    }
}
