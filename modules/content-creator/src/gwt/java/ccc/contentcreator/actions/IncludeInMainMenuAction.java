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
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;

import com.extjs.gxt.ui.client.data.ModelData;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class IncludeInMainMenuAction
    implements
        Action {

    private final CommandServiceAsync _commands = Globals.commandService();

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public IncludeInMainMenuAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ModelData item = _selectionModel.tableSelection();
        _commands.includeInMainMenu(
            item.<String>get("id"),
            true,
            new ErrorReportingCallback<Void>(){
                public void onSuccess(final Void arg0) {
                    item.set("mmInclude", Boolean.TRUE);
                    _selectionModel.update(item);
                }
            }
        );
    }

}
