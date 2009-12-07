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

import java.util.Collection;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.CreatePageDialog;
import ccc.rest.dto.TemplateSummary;

/**
 * Create a page.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenCreatePageAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public OpenCreatePageAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.treeSelection();
        if (item == null) {
            GLOBALS.alert(UI_CONSTANTS.noFolderSelected());
            return;
        }
        new GetTemplatesAction(UI_CONSTANTS.createPage()){
            @Override protected void execute(
                                 final Collection<TemplateSummary> templates) {
                new CreatePageDialog(templates, item, _selectionModel).show();
            }

        }.execute();
    }
}
