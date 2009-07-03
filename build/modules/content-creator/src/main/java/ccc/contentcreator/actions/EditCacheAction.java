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

import ccc.api.Duration;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.EditCacheDialog;


/**
 * Edit resource's cache setting.
 *
 * @author Civic Computing Ltd.
 */
public class EditCacheAction implements Action {

    private final SingleSelectionModel _selectionModel;
    private IGlobals _globals = new IGlobalsImpl();
    private QueriesServiceAsync _qs = _globals.queriesService();

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public EditCacheAction(
          final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    @Override
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        _qs.cacheDuration(item.getId(),
            new ErrorReportingCallback<Duration>(
                UI_CONSTANTS.editCacheDuration()) {
                @Override public void onSuccess(final Duration arg0) {
                    final EditCacheDialog dialog =
                        new EditCacheDialog(item, arg0);
                    dialog.show();
                }

        });

    }

}
