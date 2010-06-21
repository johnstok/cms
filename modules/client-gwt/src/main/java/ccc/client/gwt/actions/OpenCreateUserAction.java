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
package ccc.client.gwt.actions;

import ccc.api.core.Group;
import ccc.api.core.PagedCollection;
import ccc.api.types.SortOrder;
import ccc.client.core.Globals;
import ccc.client.gwt.core.Action;
import ccc.client.gwt.presenters.CreateUserPresenter;
import ccc.client.gwt.remoting.ListGroups;
import ccc.client.gwt.views.gxt.CreateUserDialog;


/**
 * Create an user.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenCreateUserAction
    implements
        Action {

    /** {@inheritDoc} */
    @Override public void execute() {
        new ListGroups(1, Globals.MAX_FETCH, "name", SortOrder.ASC) {
            @Override
            protected void execute(final PagedCollection<Group> groups) {
                new CreateUserPresenter(
                    new CreateUserDialog(groups.getElements()), GLOBALS);
            }}.execute();
        }
}
