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

import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.EditCacheDialog;
import ccc.services.api.Duration;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class EditCacheAction implements Action {

    private final QueriesServiceAsync _queries = Globals.queriesService();
    private final SingleSelectionModel _selectionModel;

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
        _queries.cacheDuration(item.getId(),
            new ErrorReportingCallback<Duration>() {
                @Override
                public void onSuccess(final Duration arg0) {
                    final EditCacheDialog dialog =
                        new EditCacheDialog(item, arg0);
                    dialog.show();
                }

        });

    }

}
