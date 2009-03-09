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
package ccc.contentcreator.client;

import ccc.contentcreator.actions.PreviewHistoricalAction;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.dialogs.HistoryDialog;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class HistoryToolBar
    extends
        AbstractToolBar {

    private final UIConstants _constants = Globals.uiConstants();
    private final HistoryDialog _historyDialog;

    /**
     * Constructor.
     *
     * @param historyDialog
     */
    public HistoryToolBar(final HistoryDialog historyDialog) {
        _historyDialog = historyDialog;

        addSeparator();
        addButton(
            "preview-historical",
            _constants.preview(),
            new PreviewHistoricalAction(_historyDialog));
        addSeparator();
    }

}
