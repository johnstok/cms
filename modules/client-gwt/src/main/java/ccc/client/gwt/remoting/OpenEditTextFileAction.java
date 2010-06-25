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
package ccc.client.gwt.remoting;

import ccc.api.core.File;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.client.gwt.presenters.EditTextFilePresenter;
import ccc.client.gwt.views.gxt.EditTextFileDialog;
import ccc.client.gwt.widgets.ContentCreator;
import ccc.client.gwt.widgets.ResourceTable;
import ccc.plugins.s11n.json.FileSerializer;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * Action for text file edit dialog opening.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenEditTextFileAction
extends
    RemotingAction {


    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public OpenEditTextFileAction(final ResourceTable selectionModel) {
        super(UI_CONSTANTS.updateTextFile());
        _selectionModel = selectionModel;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        return item.getDelegate().self().build(new GWTTemplateEncoder());
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONObject result =
            JSONParser.parse(response.getText()).isObject();
        final File dto = new FileSerializer().read(new GwtJson(result));
        if (dto.getContent() != null) {
            new EditTextFilePresenter(
                new EditTextFileDialog(),
                dto);
        } else {
            ContentCreator.WINDOW.alert(
                UI_CONSTANTS.noEditorForResource());
        }
    }

}
