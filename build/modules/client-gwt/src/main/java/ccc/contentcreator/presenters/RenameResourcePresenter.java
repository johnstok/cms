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

import ccc.contentcreator.actions.RenameAction;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.core.AbstractPresenter;
import ccc.contentcreator.core.CommandResponseHandler;
import ccc.contentcreator.core.Editable;
import ccc.contentcreator.core.EventBus;
import ccc.contentcreator.core.IGlobals;
import ccc.contentcreator.events.ResourceUpdatedEvent;
import ccc.contentcreator.views.RenameResource;
import ccc.types.ResourceName;
import ccc.types.ResourcePath;


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
        getView().setName(getModel().getName());
        getView().show(this);
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
        final ResourcePath p = new ResourcePath(getModel().getAbsolutePath());
        final ResourcePath newPath =
            p.parent().append(new ResourceName(getView().getName()));
        getModel().setAbsolutePath(newPath.toString());
        getBus().put(new ResourceUpdatedEvent(getModel()));
        getView().hide();
    }
}
