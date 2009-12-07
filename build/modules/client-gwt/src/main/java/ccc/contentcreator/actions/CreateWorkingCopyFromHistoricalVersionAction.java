/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.actions;

import ccc.contentcreator.client.GwtJson;
import ccc.contentcreator.dialogs.HistoryDialog;
import ccc.serialization.JsonKeys;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class CreateWorkingCopyFromHistoricalVersionAction
    extends
        RemotingAction {

    private final HistoryDialog _dialog;


    /**
     * Constructor.
     *
     * @param dialog The selection model for this action.
     */
    public CreateWorkingCopyFromHistoricalVersionAction(
                                                  final HistoryDialog dialog) {
        super(UI_CONSTANTS.revert(), RequestBuilder.POST);
        _dialog = dialog;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            "/resources/" + _dialog.getResourceId() + "/wc-create";
    }




    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        json.set(
            JsonKeys.REVISION, Long.valueOf(_dialog.selectedItem().getIndex()));
        json.set(JsonKeys.CACHE_DURATION, (String) null);
        json.set(JsonKeys.TEMPLATE_ID, (String) null);
        return json.toString();
    }


    /** {@inheritDoc} */
    @Override
    protected void onNoContent(final Response response) {
        _dialog.workingCopyCreated();
        _dialog.hide();
    }
}
