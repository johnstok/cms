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

import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.SingleSelectionModel;


/**
 * Include a resource in the main menu.
 *
 * @author Civic Computing Ltd.
 */
public class IncludeInMainMenuAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;
    private IGlobals _globals = new IGlobalsImpl();
    private CommandServiceAsync _cs = _globals.commandService();

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public IncludeInMainMenuAction(
          final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        _cs.includeInMainMenu(
            item.getId(),
            true,
            new ErrorReportingCallback<Void>(UI_CONSTANTS.addToMainMenu()){
                public void onSuccess(final Void arg0) {
                    item.setIncludeInMainMenu(true);
                    _selectionModel.update(item);
                }
            }
        );
    }
}
