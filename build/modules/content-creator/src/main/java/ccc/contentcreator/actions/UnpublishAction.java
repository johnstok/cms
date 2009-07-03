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
 * Unpublish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class UnpublishAction
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
    public UnpublishAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        _cs.unpublish(
            item.getId(),
            new ErrorReportingCallback<Void>(UI_CONSTANTS.unpublish()){
                public void onSuccess(final Void arg0) {
                    item.setPublished(null);
                    _selectionModel.update(item);
                }
            }
        );
    }

}
