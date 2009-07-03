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
 * Remove a resource from the main menu.
 *
 * @author Civic Computing Ltd.
 */
public class RemoveFromMainMenuAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public RemoveFromMainMenuAction(
          final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        _cs.includeInMainMenu(
            item.getId(),
            false,
            new ErrorReportingCallback<Void>(UI_CONSTANTS.removeFromMainMenu()){
                public void onSuccess(final Void arg0) {
                    item.setIncludeInMainMenu(false);
                    _selectionModel.update(item);
                }
            }
        );
    }
}
