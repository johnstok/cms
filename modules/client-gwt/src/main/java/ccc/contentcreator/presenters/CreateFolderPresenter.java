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
package ccc.contentcreator.presenters;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.core.AbstractPresenter;
import ccc.contentcreator.core.Editable;
import ccc.contentcreator.core.Globals;
import ccc.contentcreator.events.FolderCreated;
import ccc.contentcreator.events.FolderCreated.FolderCreatedHandler;
import ccc.contentcreator.remoting.CreateFolderAction;
import ccc.contentcreator.views.CreateFolder;


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
     * @param globals Implementation of the Globals API.
     * @param view View implementation.
     * @param model Model implementation.
     */
    public CreateFolderPresenter(final Globals globals,
                                 final CreateFolder view,
                                 final ResourceSummaryModelData model) {
        super(globals, view, model);
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
            getGlobals().alert(
                getGlobals().uiConstants().resourceNameIsInvalid());
        }
    }


    /** {@inheritDoc} */
    @Override
    public void onCreate(final FolderCreated event) { hide(); }
}
