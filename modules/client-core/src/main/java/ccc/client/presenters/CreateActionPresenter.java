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
package ccc.client.presenters;

import java.util.UUID;

import ccc.api.types.CommandType;
import ccc.client.actions.CreateActionAction;
import ccc.client.core.AbstractPresenter;
import ccc.client.core.Editable;
import ccc.client.core.ValidationResult;
import ccc.client.events.Event;
import ccc.client.views.CreateAction;


/**
 * MVP Presenter for creating action.
 *
 * @author Civic Computing Ltd.
 */
public class CreateActionPresenter
    extends
        AbstractPresenter<CreateAction, UUID>
    implements
        Editable {


    /**
     * Constructor.
     *
     * @param view View implementation.
     * @param model Model implementation.
     */
    public CreateActionPresenter(final CreateAction view,
                                 final UUID model) {
        super(view, model);
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
        final ValidationResult vr = getView().getValidationResult();
        if (vr.isValid()) {

            new CreateActionAction(
                getModel(),
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
    public void handle(final Event<CommandType> event) {
        switch (event.getType()) {
            case ACTION_CREATE:
                dispose();
                break;

            default:
                break;
        }
    }
}
