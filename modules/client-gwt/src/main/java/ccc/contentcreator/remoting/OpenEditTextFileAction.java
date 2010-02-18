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
package ccc.contentcreator.remoting;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.RemotingAction;
import ccc.contentcreator.core.SelectionModelEventBus;
import ccc.contentcreator.core.SingleSelectionModel;
import ccc.contentcreator.presenters.EditTextFilePresenter;
import ccc.contentcreator.views.gxt.EditTextFileDialog;
import ccc.contentcreator.widgets.ResourceTable;
import ccc.rest.dto.TextFileDelta;

import com.google.gwt.http.client.Response;
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
        super(GLOBALS.uiConstants().updateTextFile());
        _selectionModel = selectionModel;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        return "/files/" + item.getId();
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONObject result =
            JSONParser.parse(response.getText()).isObject();
        final TextFileDelta dto = new TextFileDelta(new GwtJson(result));
        if (dto.getContent() != null) {
            new EditTextFilePresenter(
                GLOBALS,
                new SelectionModelEventBus(_selectionModel),
                new EditTextFileDialog(),
                dto);
        } else {
            GLOBALS.alert(
                GLOBALS.uiConstants().noEditorForResource());
        }
    }

}
