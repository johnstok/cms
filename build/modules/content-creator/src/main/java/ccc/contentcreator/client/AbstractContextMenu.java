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
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.UIConstants;

import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.core.client.GWT;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class AbstractContextMenu
    extends
        Menu {

    /** USER_ACTIONS : ActionNameConstants. */
    protected static final ActionNameConstants USER_ACTIONS =
        GWT.create(ActionNameConstants.class);
    /** _constants : UIConstants. */
    protected final UIConstants _constants = Globals.uiConstants();
    /** _qs : QueriesServiceAsync. */
    protected final QueriesServiceAsync _qs = Globals.queriesService();

    private final SingleSelectionModel _ssm;

    /**
     * Constructor.
     *
     * @param ssm
     */
    public AbstractContextMenu(final SingleSelectionModel ssm) {
        _ssm = ssm;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param id
     * @param text
     * @param action
     * @return
     */
    protected MenuItem createMenuItem(final String id,
                                      final String text,
                                      final Action action) {
        final MenuItem item = new MenuItem();
        item.setId(id);
        item.setText(text);
        item.addSelectionListener(new SelectionListenerAction(action));
        return item;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param id
     * @param text
     * @param action
     */
    protected void addMenuItem(final String id,
                               final String text,
                               final Action action) {
        final MenuItem menuItem = createMenuItem(id, text, action);
        add(menuItem);
    }
}
