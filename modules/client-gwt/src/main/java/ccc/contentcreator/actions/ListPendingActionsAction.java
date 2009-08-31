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

import java.util.Collection;

import ccc.contentcreator.client.ActionTable;
import ccc.rest.ActionSummary;

import com.google.gwt.http.client.Response;


/**
 * Display the list of pending actions.
 *
 * @author Civic Computing Ltd.
 */
public class ListPendingActionsAction
    extends
        RemotingAction {

    private final ActionTable _actionTable;

    /**
     * Constructor.
     *
     * @param actionTable The UI component to notify.
     */
    public ListPendingActionsAction(final ActionTable actionTable) {
        super(USER_ACTIONS.viewActions());
        _actionTable = actionTable;
    }

    /** {@inheritDoc} */
    @Override protected String getPath() { return "/actions/pending"; }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final Collection<ActionSummary> actions =
            parseActionSummaryCollection(response);
        _actionTable.updatePagingModel(actions);
    }
}
