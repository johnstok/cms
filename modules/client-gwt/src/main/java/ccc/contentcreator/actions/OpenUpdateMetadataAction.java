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
package ccc.contentcreator.actions;

import java.util.HashMap;
import java.util.Map;

import ccc.contentcreator.core.RemotingAction;
import ccc.contentcreator.core.SingleSelectionModel;
import ccc.contentcreator.views.gxt.MetadataDialog;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * Update resource's metadata.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenUpdateMetadataAction
    extends
        RemotingAction {

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
        return
            "/resources/"
            + _selectionModel.tableSelection().getId()
            + "/metadata";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONObject result =
            JSONParser.parse(response.getText()).isObject();
        final Map<String, String> metadata = new HashMap<String, String>();
        for (final String key : result.keySet()) {
            metadata.put(key, result.get(key).isString().stringValue());
        }

        new MetadataDialog(
            _selectionModel.tableSelection(),
            metadata.entrySet(),
            _selectionModel)
        .show();
    }
}
