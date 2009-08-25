/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.actions;

import ccc.contentcreator.client.GwtJson;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.EditCacheDialog;
import ccc.types.Duration;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;


/**
 * Edit resource's cache setting.
 *
 * @author Civic Computing Ltd.
 */
public class EditCacheAction
    extends
        RemotingAction {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public EditCacheAction(final SingleSelectionModel selectionModel) {
        super(UI_CONSTANTS.editCacheDuration());
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final EditCacheDialog dialog =
            new EditCacheDialog(
                _selectionModel.tableSelection(),
                new Duration(
                    new GwtJson(
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
        return
            "/resources/"+_selectionModel.tableSelection().getId()+"/duration";
    }
}
