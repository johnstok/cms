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

import java.util.ArrayList;
import java.util.List;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.core.AbstractPresenter;
import ccc.contentcreator.core.Editable;
import ccc.contentcreator.core.EventBus;
import ccc.contentcreator.core.Globals;
import ccc.contentcreator.events.FolderCreated;
import ccc.contentcreator.events.FolderCreated.FolderCreatedHandler;
import ccc.contentcreator.remoting.CreateFolderAction;
import ccc.contentcreator.views.CreateFolder;
import ccc.contentcreator.widgets.ContentCreator;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;


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

    private final List<HandlerRegistration> _handlers =
        new ArrayList<HandlerRegistration>();



    /**
     * Constructor.
     *
     * @param globals Implementation of the Globals API.
     * @param bus Implementation of the Event Bus API.
     * @param view View implementation.
     * @param model Model implementation.
     */
    public CreateFolderPresenter(final Globals globals,
                                 final EventBus bus,
                                 final CreateFolder view,
                                 final ResourceSummaryModelData model) {
        super(globals, bus, view, model);
        addHandler(FolderCreated.TYPE, this);
        getView().show(this);
    }


    private <T extends EventHandler> void addHandler(
                                                 final GwtEvent.Type<T> event,
                                                 final T handler) {
        _handlers.add(ContentCreator.EVENT_BUS.addHandler(event, handler));
    }


    private void hide() {
        clearHandlers();
        getView().hide();
    }


    private void clearHandlers() {
        for (final HandlerRegistration hr : _handlers) {
            hr.removeHandler();
        }
        _handlers.clear();
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
