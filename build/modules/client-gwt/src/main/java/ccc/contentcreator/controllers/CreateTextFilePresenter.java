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

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.AbstractPresenter;
import ccc.contentcreator.client.CommandResponseHandler;
import ccc.contentcreator.client.Editable;
import ccc.contentcreator.client.EventBus;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.views.CreateTextFile;


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
//        getView().setText(model.getContent());
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
        if (getView().isValid()) {
            // SAVE
        } else {
            getGlobals().alert(
                getGlobals().uiConstants().isNotValid());
        }
    }
}
