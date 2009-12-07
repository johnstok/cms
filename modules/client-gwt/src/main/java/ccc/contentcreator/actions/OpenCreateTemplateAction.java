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

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.EditTemplateDialog;

/**
 * Create a template.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenCreateTemplateAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public OpenCreateTemplateAction(
          final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.treeSelection();
        if (item == null) {
            GLOBALS.alert(GLOBALS.uiConstants().noFolderSelected());
        } else {
            new EditTemplateDialog(
                item.getId(),
                _selectionModel)
            .show();
        }
    }
}
