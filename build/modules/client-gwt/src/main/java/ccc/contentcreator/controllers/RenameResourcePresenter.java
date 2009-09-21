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

import ccc.contentcreator.actions.RenameAction;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.AbstractPresenter;
import ccc.contentcreator.client.CommandResponseHandler;
import ccc.contentcreator.client.Editable;
import ccc.contentcreator.client.EventBus;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.views.RenameResource;


/**
 * MVP presenter for renaming resources.
 *
 * @author Civic Computing Ltd.
 */
public class RenameResourcePresenter
    extends
        AbstractPresenter<RenameResource, ResourceSummaryModelData>
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
    public RenameResourcePresenter(final IGlobals globals,
                                   final EventBus bus,
                                   final RenameResource view,
                                   final ResourceSummaryModelData model) {
        super(globals, bus, view, model);
        getView().setPresenter(this);
        getView().setName(getModel().getName());
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
            new RenameAction(getModel().getId(),
                             getView().getName(),
                             this).execute();
        } else {
            getGlobals().alert(
                getGlobals().uiConstants().resourceNameIsInvalid());
        }
    }


    /** {@inheritDoc} */
    @Override
    public void onSuccess(final Void result) {
        getModel().setName(getView().getName());
        getBus().put(new ResourceUpdatedEvent(getModel()));
        getView().hide();
    }
}
