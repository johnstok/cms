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
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.actions;

import ccc.api.core.ResourceSummary;
import ccc.api.core.Revision;
import ccc.api.types.ResourceType;
import ccc.client.core.Action;
import ccc.client.core.InternalServices;
import ccc.client.gwt.views.gxt.HistoryDialog;
import ccc.client.remoting.GetAbsolutePathAction;
import ccc.client.remoting.OpenTemplateRevisionAction;

/**
 * Open a dialog to preview the selected resource.
 *
 * @author Civic Computing Ltd.
 */
public final class PreviewHistoricalAction
    implements
        Action {

    private final HistoryDialog _historyDialog;

    /**
     * Constructor.
     *
     * @param dialog The history dialog.
     */
    public PreviewHistoricalAction(final HistoryDialog dialog) {
        _historyDialog = dialog;
    }

    /** {@inheritDoc} */
    public void execute() {
        final Revision item = _historyDialog.selectedItem();

        if (null==item) {
            return;
        }
        final ResourceSummary resource = _historyDialog.getResource();

        if (resource != null && resource.getType() == ResourceType.TEMPLATE) {
            new OpenTemplateRevisionAction(resource, item.getIndex()).execute();
        } else {
            new GetAbsolutePathAction(UI_CONSTANTS.preview(),
                resource) {
                @Override protected void execute(final String path) {
                    final String url =
                        InternalServices.GLOBALS.appURL()
                        + "preview"
                        + path
                        + "?v="
                        + item.getIndex();
                    InternalServices.WINDOW.openUrl(
                        url,
                        "ccc_preview",
                        "menubar=no,"
                        + "width=640,"
                        + "height=480,"
                        + "location=yes,"
                        + "toolbar=no,"
                        + "resizable=yes,"
                        + "scrollbars=yes,"
                        + "status=no");
                }
            }.execute();
        }
    }
}
