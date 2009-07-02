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

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;


/**
 * Applies working copy.
 *
 * @author Civic Computing Ltd.
 */
public class ApplyWorkingCopyAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public ApplyWorkingCopyAction(
          final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        COMMAND_SERVICE.applyWorkingCopy(
            item.getId(),
            new ErrorReportingCallback<Void>(UI_CONSTANTS.applyWorkingCopy()){
                public void onSuccess(final Void arg0) {
                    item.setWorkingCopy(false);
                    _selectionModel.update(item);
                }
            }
        );
    }

}
