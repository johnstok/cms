/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.controllers;

import ccc.contentcreator.actions.CreateFolderAction;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.AbstractPresenter;
import ccc.contentcreator.client.CommandResponseHandler;
import ccc.contentcreator.client.Editable;
import ccc.contentcreator.client.EventBus;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.views.CreateFolder;
import ccc.rest.dto.ResourceSummary;


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
        CommandResponseHandler<ResourceSummaryModelData> {



    /**
     * Constructor.
     *
     * @param globals Implementation of the Globals API.
     * @param bus Implementation of the Event Bus API.
     * @param view View implementation.
     * @param model Model implementation.
     */
    public CreateFolderPresenter(final IGlobals globals,
                                 final EventBus bus,
                                 final CreateFolder view,
                                 final ResourceSummaryModelData model) {
        super(globals, bus, view, model);
        getView().setPresenter(this);
        getView().show();
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
            new CreateFolderAction(
                getModel().getId(),
                getView().getName()){
                @Override protected void execute(final ResourceSummary folder) {
                    final ResourceSummaryModelData newFolder =
                        new ResourceSummaryModelData(folder);
                    onSuccess(newFolder);
                }
            }.execute();
        } else {
            getGlobals().alert(
                getGlobals().uiConstants().resourceNameIsInvalid());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onSuccess(final ResourceSummaryModelData newFolder) {
        getBus().put(
            new ResourceCreatedEvent(newFolder, getModel()));
        getView().hide();
    }

}
