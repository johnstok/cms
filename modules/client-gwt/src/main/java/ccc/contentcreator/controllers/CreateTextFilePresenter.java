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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.controllers;

import ccc.contentcreator.actions.CreateTextFileAction;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.AbstractPresenter;
import ccc.contentcreator.client.CommandResponseHandler;
import ccc.contentcreator.client.Editable;
import ccc.contentcreator.client.EventBus;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.views.CreateTextFile;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TextFileDto;
import ccc.types.MimeType;


/**
 * MVP presenter for creating text file.
 *
 * @author Civic Computing Ltd.
 */
public class CreateTextFilePresenter extends
AbstractPresenter<CreateTextFile, ResourceSummaryModelData>
implements
Editable,
CommandResponseHandler<ResourceSummaryModelData> {

    /**
     * Constructor.
     *
     * @param globals Implementation of the Globals API.
     * @param bus Implementation of the Event Bus API.
     * @param view View implementation.
     * @param model Model implementation.
     */
    public CreateTextFilePresenter(final IGlobals globals,
                                 final EventBus bus,
                                 final CreateTextFile view,
                                 final ResourceSummaryModelData model) {

        super(globals, bus, view, model);
        getView().setPresenter(this);
        getView().show();
    }

    /** {@inheritDoc} */
    @Override
    public void onSuccess(final ResourceSummaryModelData newFolder) {
        getBus().put(
            new ResourceCreatedEvent(newFolder, getModel()));
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
        final CreateTextFile view = getView();
        if (view.getValidationResult().isValid()) {
            final TextFileDto dto = new TextFileDto(
                getModel().getId(),
                view.getName(),
                new MimeType(view.getPrimaryMime(), view.getSubMime()),
                view.isMajorEdit(),
                view.getComment(),
                view.getText());

            new CreateTextFileAction(dto){
                @Override protected void execute(final ResourceSummary folder) {
                    final ResourceSummaryModelData newFolder =
                        new ResourceSummaryModelData(folder);
                    onSuccess(newFolder);
                }
            }.execute();
        } else {
            getGlobals().alert(
                view.getValidationResult().getErrorText());
        }
    }
}
