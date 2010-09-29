/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.remoting;

import java.util.Map;

import ccc.api.core.ResourceSummary;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.core.SingleSelectionModel;

/**
 * Update resource's metadata.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenUpdateMetadataAction
    extends
        RemotingAction<Map<String, String>> {

    private final SingleSelectionModel _selectionModel;


    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public OpenUpdateMetadataAction(final SingleSelectionModel selectionModel) {
        super(UI_CONSTANTS.updateMetadata());
        _selectionModel = selectionModel;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        final ResourceSummary delegate =
            _selectionModel.tableSelection();
        return delegate.uriMetadata().build(InternalServices.ENCODER);
    }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final Map<String, String> metadata) {
        InternalServices.DIALOGS.updateMetadata(
            _selectionModel.tableSelection(),
            metadata.entrySet(),
            _selectionModel)
        .show();
    }


    /** {@inheritDoc} */
    @Override
    protected Map<String, String> parse(final Response response) {
        return InternalServices.PARSER.parseStringMap(response.getText());
    }
}
