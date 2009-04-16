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
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.data.ModelData;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UnlockAction
    implements
        Action {

    private final CommandServiceAsync _commands = Globals.commandService();

    private final SingleSelectionModel _selectionModel;
    private final boolean _isTreeSelection;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     * @param isTreeSelection
     */
    public UnlockAction(final SingleSelectionModel selectionModel,
                        final boolean isTreeSelection) {
        _selectionModel = selectionModel;
        _isTreeSelection = isTreeSelection;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ModelData item =
            (_isTreeSelection)
                ?_selectionModel.treeSelection()
                :_selectionModel.tableSelection();
        _commands.unlock(
            item.<String>get("id"),
            new ErrorReportingCallback<ResourceSummary>(){
                public void onSuccess(final ResourceSummary arg0) {
                    DataBinding.merge(item, arg0);
                    _selectionModel.update(item);
                }
            }
        );
    }

}
