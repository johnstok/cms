/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.widgets;

import ccc.api.core.ResourceSummary;
import ccc.api.types.ResourceType;
import ccc.client.core.I18n;
import ccc.client.gwt.actions.PreviewHistoricalAction;
import ccc.client.gwt.remoting.CreateWorkingCopyFromHistoricalVersionAction;
import ccc.client.gwt.views.gxt.HistoryDialog;
import ccc.client.i18n.UIConstants;


/**
 * Toolbar for resource history dialog.
 *
 * @author Civic Computing Ltd.
 */
public class HistoryToolBar
    extends
        AbstractToolBar {

    private final UIConstants _constants = I18n.UI_CONSTANTS;
    private final HistoryDialog _historyDialog;

    /**
     * Constructor.
     *
     * @param historyDialog The history dialog.
     */
    public HistoryToolBar(final HistoryDialog historyDialog) {
        _historyDialog = historyDialog;

        addSeparator();
        addButton(
            "preview-historical",
            _constants.preview(),
            new PreviewHistoricalAction(_historyDialog));
        addSeparator();
        if (_historyDialog.hasLock()) {
            ResourceSummary resource = _historyDialog.getResource();
            if(resource != null
               && resource.getType()!=ResourceType.TEMPLATE) {
                addButton(
                    "create-historical-wc",
                    _constants.revert(),
                    new CreateWorkingCopyFromHistoricalVersionAction(
                        _historyDialog));
                addSeparator();
            }
        }
    }

}
