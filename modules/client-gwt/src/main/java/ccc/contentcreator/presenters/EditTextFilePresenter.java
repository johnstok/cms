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
package ccc.contentcreator.presenters;

import ccc.contentcreator.actions.EditTextFileAction;
import ccc.contentcreator.client.CommandResponseHandler;
import ccc.contentcreator.core.AbstractPresenter;
import ccc.contentcreator.core.Editable;
import ccc.contentcreator.core.EventBus;
import ccc.contentcreator.core.IGlobals;
import ccc.contentcreator.views.EditTextFile;
import ccc.rest.dto.TextFileDelta;
import ccc.types.MimeType;

import com.google.gwt.http.client.Response;


/**
 * MVP presenter for renaming resources.
 *
 * @author Civic Computing Ltd.
 */
public class EditTextFilePresenter extends
AbstractPresenter<EditTextFile, TextFileDelta>
implements
Editable,
CommandResponseHandler<Void> {

    /**
     * Constructor.
     *
     * @param globals Implementation of the Globals API.
     * @param bus Implementation of the Event Bus API.
     * @param view View implementation.
     * @param model Model implementation.
     */
    public EditTextFilePresenter(final IGlobals globals,
                                 final EventBus bus,
                                 final EditTextFile view,
                                 final TextFileDelta model) {

        super(globals, bus, view, model);
        getView().show(this);
        getView().setText(model.getContent());
        getView().setPrimaryMime(model.getMimeType().getPrimaryType());
        getView().setSubMime(model.getMimeType().getSubType());
    }

    /** {@inheritDoc} */
    @Override
    public void onSuccess(final Void result) {
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
        final EditTextFile view = getView();
        if (view.getValidationResult().isValid()) {
            final TextFileDelta dto = new TextFileDelta(getModel().getId(),
                view.getText(),
                new MimeType(view.getPrimaryMime(), view.getSubMime()),
                view.isMajorEdit(),
                view.getComment());
            new EditTextFileAction(dto) {
                /** {@inheritDoc} */
                @Override
                protected void onNoContent(final Response response) {
                    getView().hide();
                }

            }.execute();
        } else {
            getGlobals().alert(
                getView().getValidationResult().getErrorText());
        }
    }
}
