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
package ccc.client.gwt.remoting;

import ccc.api.types.DBC;
import ccc.api.types.SortOrder;
import ccc.client.gwt.binding.ActionCollection;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.RemotingAction;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * Display the list of pending actions.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ListPendingActionsAction
    extends
        RemotingAction {

    private int           _page;
    private int           _count;
    private SortOrder     _order;
    private String        _sort;

    /**
     * Constructor.
     *
     * @param page The page of results to return.
     * @param count The number of results in a page.
     * @param sort The field to sort on.
     * @param order The order results be sorted in.
     */
    public ListPendingActionsAction(final int page,
                                    final int count,
                                    final String sort,
                                    final SortOrder order) {
        super(USER_ACTIONS.viewActions());
        DBC.require().toBeTrue(page>0);
        DBC.require().toBeTrue(count>0);

        _page = page;
        _count = count;
        _sort = sort;
        _order = order;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        final String path =
            GLOBALS.actions().pending(_page, _count, _order, _sort);
        return path;
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONObject obj =
            JSONParser.parse(response.getText()).isObject();
        final ActionCollection actions = new ActionCollection();
        actions.fromJson(new GwtJson(obj));

        execute(actions);
    }

    /**
     * Handle the result of a successful call.
     *
     * @param actions The page of actions returned.
     */
    protected abstract void execute(ActionCollection actions);
}
