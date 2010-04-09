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

import java.util.UUID;

import ccc.api.dto.GroupDto;
import ccc.contentcreator.events.GroupUpdated;
import ccc.contentcreator.events.GroupUpdated.GroupUpdatedHandler;
import ccc.contentcreator.remoting.UpdateGroupAction;
import ccc.contentcreator.widgets.ContentCreator;


/**
 * MVP presenter for group update.
 *
 * TODO: Known issue: if multiple dialogs are open simultaneously they will all
 *  be disposed on a successful update.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateGroupPresenter
    extends
        GroupPresenter
    implements
        GroupUpdatedHandler {

    private final UUID _groupId;


    /**
     * Constructor.
     *
     * @param view The view for this presenter.
     * @param group The group to update.
     */
    public UpdateGroupPresenter(final GroupView view, final GroupDto group) {
        super(view);
        _groupId = group.getId();
        bind(group);
        render(ContentCreator.EVENT_BUS.addHandler(GroupUpdated.TYPE, this));
    }


    /** {@inheritDoc} */
    @Override
    public void save() {
        if (valid()) {
            final GroupDto group = new GroupDto();
            group.setId(_groupId);
            unbind(group);
            new UpdateGroupAction(group).execute();
        }
    }


    /** {@inheritDoc} */
    @Override
    public void onUpdate(final GroupUpdated event) {
        dispose();
    }
}
