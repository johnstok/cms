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

import ccc.contentcreator.api.UIConstants;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;


/**
 * A tree control for navigating scheduled actions.
 *
 * @author Civic Computing Ltd.
 */
public class ActionTree extends Tree {

    private final ActionTable   _at = new ActionTable();
    private final LeftRightPane _view;
    private final UIConstants _constants = new IGlobalsImpl().uiConstants();

    /** ACTIONS : String. */
    public static final String ACTIONS = "actions";
    /** PENDING : String. */
    public static final String PENDING = "pending";
    /** COMPLETED : String. */
    public static final String COMPLETED = "completed";

    /**
     * Constructor.
     *
     * @param view The split pane used to render this tree.
     */
    public ActionTree(final LeftRightPane view) {
        _view = view;

        final TreeItem actions = new TreeItem(_constants.actions());
        actions.setId(ACTIONS);
        final TreeItem pending = new TreeItem(_constants.pending());
        pending.setId(PENDING);
        pending.setIconStyle("images/icons/hourglass.gif");
        final TreeItem completed = new TreeItem(_constants.completed());
        completed.setId(COMPLETED);
        completed.setIconStyle("images/icons/accept.gif");

        getRootItem().add(actions);
        actions.add(pending);
        actions.add(completed);

        setSelectionMode(SelectionMode.SINGLE);
        setStyleAttribute("background", "white");

        addListener(
            Events.SelectionChange,
            new ActionSelectedListener());
    }

    /**
     * Notify the split pane that the tree's table should be displayed.
     */
    public void showTable() {
        _view.setRightHandPane(_at);
    }

    /**
     * Listener for selection events of the enclosing tree class.
     *
     * @author Civic Computing Ltd.
     */
    private class ActionSelectedListener implements Listener<TreeEvent> {

        /** {@inheritDoc} */
        public void handleEvent(final TreeEvent te) {
            _at.displayActionsFor(te.getTree().getSelectedItem());
        }
    }
}
