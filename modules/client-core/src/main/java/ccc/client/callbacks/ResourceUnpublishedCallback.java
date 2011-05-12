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
package ccc.client.callbacks;

import ccc.api.core.ResourceSummary;
import ccc.client.core.DefaultCallback;
import ccc.client.core.SingleSelectionModel;


/**
 * Handles the un-publishing of a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceUnpublishedCallback
    extends
        DefaultCallback<Void> {

    private final SingleSelectionModel _selectionModel;


    /**
     * Constructor.
     *
     * @param actionName The name of the action performed.
     * @param ssm        The selection model containing the selected item.
     */
    public ResourceUnpublishedCallback(final String actionName,
                                       final SingleSelectionModel ssm) {
        super(actionName);
        _selectionModel = ssm;
    }


    /** {@inheritDoc} */
    @Override
    public void onSuccess(final Void result) {
        final ResourceSummary item = _selectionModel.tableSelection();
        item.setPublishedBy(null);
        item.setVisible(false);
        _selectionModel.update(item);
    }
}
