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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.remoting;

import ccc.api.core.Resource;
import ccc.api.types.DurationSerializer;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.client.gwt.views.gxt.EditCacheDialog;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;


/**
 * Edit resource's cache setting.
 *
 * @author Civic Computing Ltd.
 */
public class OpenEditCacheAction
    extends
        RemotingAction {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public OpenEditCacheAction(final SingleSelectionModel selectionModel) {
        super(UI_CONSTANTS.editCacheDuration());
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final EditCacheDialog dialog =
            new EditCacheDialog(
                _selectionModel.tableSelection(),
                new DurationSerializer().read(new GwtJson(
                    JSONParser.parse(response.getText()).isObject())));
        dialog.show();
    }

    /** {@inheritDoc} */
    @Override
    protected void onNoContent(final Response response) {
        final EditCacheDialog dialog =
            new EditCacheDialog(
                _selectionModel.tableSelection(),
                null);
        dialog.show();
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return _selectionModel.tableSelection().getDelegate().duration().build(new GWTTemplateEncoder());
    }
}
