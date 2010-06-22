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
import ccc.client.core.ValidationResult;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.AbstractPresenter;
import ccc.client.gwt.events.ActionCreated;
import ccc.client.gwt.events.ActionCreated.ActionCreatedHandler;
import ccc.client.gwt.remoting.CreateActionAction;
import ccc.client.views.CreateAction;


/**
 * MVP Presenter for creating action.
 *
 * @author Civic Computing Ltd.
 */
public class CreateActionPresenter
    extends
        AbstractPresenter<CreateAction, ResourceSummaryModelData>
    implements
        Editable,
        ActionCreatedHandler {


    /**
     * Constructor.
     *
     * @param view View implementation.
     * @param model Model implementation.
     */
    public CreateActionPresenter(final CreateAction view,
                                 final ResourceSummaryModelData model) {
        super(view, model);
        addHandler(ActionCreated.TYPE, this);
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
        final ValidationResult vr = getView().getValidationResult();
        if (vr.isValid()) {

            new CreateActionAction(
                getModel().getId(),
                getView().getCommandType(),
                getView().getDate(),
                getView().getActionParameters())
            .execute();
        } else {
            getView().alert(vr.getErrorText());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onCreate(final ActionCreated event) { hide(); }
}
