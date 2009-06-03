/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.gwt.core.client.GWT;


/**
 * Abstract base class for table panels.
 *
 * @author Civic Computing Ltd.
 */
public abstract class TablePanel extends ContentPanel {

    /** _cs : CommandServiceAsync. */
    protected final CommandServiceAsync _cs =
        GWT.create(CommandService.class);
    /** qs : QueriesServiceAsync. */
    protected final QueriesServiceAsync qs =
        GWT.create(QueriesService.class);
    /** USER_ACTIONS : ActionNameConstants. */
    protected final ActionNameConstants USER_ACTIONS =
        GWT.create(ActionNameConstants.class);
    /** UI_CONSTANTS : UIConstants. */
    protected static final UIConstants UI_CONSTANTS =
        GWT.create(UIConstants.class);
    /** PAGING_ROW_COUNT : int. */
    protected static final int PAGING_ROW_COUNT = 20;
}
