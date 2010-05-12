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
package ccc.client.gwt.remoting;

import ccc.api.core.Resource;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.HttpMethod;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Response;
import ccc.client.gwt.views.gxt.HistoryDialog;


/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class CreateWorkingCopyFromHistoricalVersionAction
    extends
        RemotingAction {

    private final HistoryDialog _dialog;


    /**
     * Constructor.
     *
     * @param dialog The selection model for this action.
     */
    public CreateWorkingCopyFromHistoricalVersionAction(
                                                  final HistoryDialog dialog) {
        super(UI_CONSTANTS.revert(), HttpMethod.POST);
        _dialog = dialog;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return _dialog.getResource().revisions().build(new GWTTemplateEncoder());
    }




    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        final Resource r = new Resource();
        r.setRevision((int) _dialog.selectedItem().getIndex()); // FIXME
        r.toJson(json);
        return json.toString();
    }


    /** {@inheritDoc} */
    @Override
    protected void onNoContent(final Response response) {
        _dialog.workingCopyCreated();
        _dialog.hide();
    }
}
