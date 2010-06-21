/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.client.gwt.presenters;

import ccc.client.core.Editable;
import ccc.client.core.I18n;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.AbstractPresenter;
import ccc.client.gwt.events.FolderCreated;
import ccc.client.gwt.events.FolderCreated.FolderCreatedHandler;
import ccc.client.gwt.remoting.CreateFolderAction;
import ccc.client.gwt.views.CreateFolder;
import ccc.client.gwt.widgets.ContentCreator;


/**
 * MVP Presenter for creating folders.
 *
 * @author Civic Computing Ltd.
 */
public class CreateFolderPresenter
    extends
        AbstractPresenter<CreateFolder, ResourceSummaryModelData>
    implements
        Editable,
        FolderCreatedHandler {


    /**
     * Constructor.
     *
     * @param view View implementation.
     * @param model Model implementation.
     */
    public CreateFolderPresenter(final CreateFolder view,
                                 final ResourceSummaryModelData model) {
        super(view, model);
        addHandler(FolderCreated.TYPE, this);
        getView().show(this);
    }


    private void hide() {
        clearHandlers();
        getView().hide();
    }


    /** {@inheritDoc} */
    @Override
    public void cancel() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void save() {
        if (getView().getValidationResult().isValid()) {
            new CreateFolderAction(
                getModel().getId(),
                getView().getName())
            .execute();
        } else {
            ContentCreator.WINDOW.alert(
                I18n.UI_CONSTANTS.resourceNameIsInvalid());
        }
    }


    /** {@inheritDoc} */
    @Override
    public void onCreate(final FolderCreated event) { hide(); }
}
