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

import ccc.contentcreator.core.Action;
import ccc.contentcreator.presenters.CreateUserPresenter;
import ccc.contentcreator.remoting.ListGroups;
import ccc.contentcreator.views.gxt.CreateUserDialog;
import ccc.rest.dto.GroupDto;


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
        new ListGroups() {
            @Override
            protected void execute(final Collection<GroupDto> g) {
                new CreateUserPresenter(
                    new CreateUserDialog(g), GLOBALS);
            }}.execute();
        }
}
