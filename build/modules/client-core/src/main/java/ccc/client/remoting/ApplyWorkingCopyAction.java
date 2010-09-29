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
package ccc.client.remoting;

import ccc.api.core.ResourceSummary;
import ccc.client.core.Callback;
import ccc.client.core.CallbackResponseHandler;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.Parser;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;
import ccc.client.core.SingleSelectionModel;


/**
 * Applies working copy.
 *
 * @author Civic Computing Ltd.
 */
public class ApplyWorkingCopyAction
    extends
        RemotingAction<Void> {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public ApplyWorkingCopyAction(final SingleSelectionModel selectionModel) {
        super(I18n.UI_CONSTANTS.applyWorkingCopy());
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    @Override
    protected Request getRequest(final Callback<Void> callback) {
        final ResourceSummary rs = _selectionModel.tableSelection();

        return new Request(
            HttpMethod.POST,
            Globals.API_URL
                + rs.wc().build(InternalServices.ENCODER),
            "",
            new CallbackResponseHandler<Void>(
                getActionName(),
                callback,
                new Parser<Void>() {
                    @Override public Void parse(final Response response) {
                        return null;
                    }}));
    }
}
