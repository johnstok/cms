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
package ccc.contentcreator.presenters;


import ccc.contentcreator.actions.remote.CreateGroupAction;
import ccc.contentcreator.client.ContentCreator;
import ccc.contentcreator.events.GroupCreated;
import ccc.contentcreator.events.GroupCreated.GroupCreatedHandler;
import ccc.rest.dto.GroupDto;



/**
 * MVP presenter for group creation.
 *
 * TODO: Known issue: if multiple dialogs are open simultaneously they will all
 *  be disposed on a successful create.
 *
 * @author Civic Computing Ltd.
 */
public class CreateGroupPresenter
    extends
        GroupPresenter
    implements
        GroupCreatedHandler {

    /**
     * Constructor.
     *
     * @param view The view for this presenter.
     */
    public CreateGroupPresenter(final GroupView view) {
        super(view);
        render(ContentCreator.EVENT_BUS.addHandler(GroupCreated.TYPE, this));
    }


    /** {@inheritDoc} */
    @Override
    public void save() {
        if (valid()) {
            final GroupDto group = new GroupDto();
            unbind(group);
            new CreateGroupAction(group).execute();
        }
    }


    /** {@inheritDoc} */
    @Override
    public void onCreate(final GroupCreated event) {
        dispose();
    }
}
