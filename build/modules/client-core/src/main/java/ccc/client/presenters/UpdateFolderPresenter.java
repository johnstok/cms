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
package ccc.client.presenters;

import java.util.List;

import ccc.api.core.Folder;
import ccc.api.core.Resource;
import ccc.api.types.CommandType;
import ccc.client.actions.UpdateFolderAction;
import ccc.client.core.AbstractPresenter;
import ccc.client.core.Editable;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.events.Event;
import ccc.client.views.UpdateFolder;


/**
 * MVP Presenter for editing folders.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFolderPresenter
    extends
    AbstractPresenter<UpdateFolder, Resource>
implements
    Editable {


    /**
     * Constructor.
     *
     * @param view View implementation.
     * @param model Model implementation.
     */
    public UpdateFolderPresenter(final UpdateFolder view, final Folder model) {

        super(view, model);
        getView().setFolder(model);
        getView().show(this);
    }

    @Override
    public void cancel() {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public void save() {
        if (getView().getValidationResult().isValid()) {

            final List<String> orderList = getView().getOrderList();

            final Folder f = new Folder();
            f.setId(getView().getFolder().getId());
            f.setIndex(getView().getIndexPage());
            f.setSortList(orderList);
            f.addLink(
                Resource.Links.SELF,
                getView().getFolder().getLink(Resource.Links.SELF));

            new UpdateFolderAction(f).execute();

        } else {
            InternalServices.WINDOW.alert(
                I18n.UI_CONSTANTS.resourceNameIsInvalid());
        }
    }

    @Override
    public void handle(final Event<CommandType> event) {
        switch (event.getType()) {
            case FOLDER_UPDATE:
                dispose();
                break;

            default:
                break;
        }
    }
}
