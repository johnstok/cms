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

import ccc.api.core.ResourceSummary;
import ccc.api.types.CommandType;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourcePath;
import ccc.client.actions.RenameAction;
import ccc.client.core.AbstractPresenter;
import ccc.client.core.Editable;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.events.Event;
import ccc.client.views.RenameResource;


/**
 * MVP presenter for renaming resources.
 *
 * @author Civic Computing Ltd.
 */
public class RenameResourcePresenter
    extends
        AbstractPresenter<RenameResource, ResourceSummary>
    implements
        Editable {


    /**
     * Constructor.
     *
     * @param view View implementation.
     * @param model Model implementation.
     */
    public RenameResourcePresenter(final RenameResource view,
                                   final ResourceSummary model) {
        super(view, model);

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

            final ResourcePath p =
                new ResourcePath(getModel().getAbsolutePath());
            final ResourcePath newPath =
                p.parent().append(new ResourceName(getView().getName()));

            new RenameAction(getModel(),
                             getView().getName(),
                             newPath)
            .execute();

        } else {
            InternalServices.WINDOW.alert(
                I18n.UI_CONSTANTS.resourceNameIsInvalid());
        }
    }


    /** {@inheritDoc} */
    @Override
    public void handle(final Event<CommandType> event) {
        switch (event.getType()) {
            case RESOURCE_RENAME:
                dispose();
                break;

            default:
                break;
        }
    }
}
