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

import ccc.contentcreator.api.ActionNameConstants;
import ccc.contentcreator.api.CommandService;
import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.api.QueriesService;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.UIConstants;

import com.google.gwt.core.client.GWT;


/**
 * A GUI action.
 *
 * @author Civic Computing Ltd.
 */
public interface Action {
    /** USER_ACTIONS : ActionNameConstants. */
    ActionNameConstants USER_ACTIONS =
        GWT.create(ActionNameConstants.class);
    /** UI_CONSTANTS : UIConstants. */
    UIConstants UI_CONSTANTS =
        GWT.create(UIConstants.class);
    /** QUERIES_SERVICE : QueriesServiceAsync. */
    QueriesServiceAsync QUERIES_SERVICE =
        GWT.create(QueriesService.class);
    /** COMMAND_SERVICE : CommandServiceAsync. */
    CommandServiceAsync COMMAND_SERVICE = GWT.create(CommandService.class);


    /**
     * Perform the action.
     */
    void execute();
}
