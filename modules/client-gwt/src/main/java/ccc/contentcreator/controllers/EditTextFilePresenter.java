/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.controllers;

import ccc.contentcreator.actions.EditTextFileAction;
import ccc.contentcreator.client.AbstractPresenter;
import ccc.contentcreator.client.CommandResponseHandler;
import ccc.contentcreator.client.Editable;
import ccc.contentcreator.client.EventBus;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.views.EditTextFile;
import ccc.rest.dto.TextFileDelta;

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
        getView().setPresenter(this);
        getView().setText(model.getContent());
        getView().show();
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
        if (getView().getValidationResult().isValid()) {
            final TextFileDelta dto = new TextFileDelta(getModel().getId(),
                getView().getText(),
                getModel().getMimeType(),
                getView().isMajorEdit(),
                getView().getComment());
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
