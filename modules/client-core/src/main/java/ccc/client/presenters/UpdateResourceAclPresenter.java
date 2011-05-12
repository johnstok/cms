/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
import ccc.client.actions.UpdateResourceAclAction;
import ccc.client.core.AbstractPresenter;
import ccc.client.core.Editable;
import ccc.client.events.Event;
import ccc.client.views.UpdateResourceAcl;


/**
 *  MVP presenter for ACL update.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceAclPresenter
extends
AbstractPresenter<UpdateResourceAcl, ResourceSummary>
implements
    Editable {

    /**
     * Constructor.
     *
     * @param view The view for this presenter.
     * @param model The ResourceSummary.
     */
    public UpdateResourceAclPresenter(final UpdateResourceAcl view,
                                      final ResourceSummary model) {

        super(view, model);
        getView().setResourceSummary(model);
        getView().show(this);
    }

    @Override
    public void cancel() {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public void save() {

        new UpdateResourceAclAction(getView().getResourceSummary(),
                                    getView().getAcl()).execute();
    }

    /** {@inheritDoc} */
    @Override
    public void handle(final Event<CommandType> event) {
        switch (event.getType()) {
            case RESOURCE_CHANGE_ROLES:
                dispose();
                break;

            default:
                break;
        }
    }

}
