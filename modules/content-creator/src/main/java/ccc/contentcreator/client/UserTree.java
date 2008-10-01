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

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UserTree extends Tree {

    private final UserTable _ut =
        new UserTable(new GwtApplication().lookupService());


    /**
     * TODO: Add Description for this type.
     *
     * @author Civic Computing Ltd.
     */
    public class UserSelectedListener implements Listener<TreeEvent> {

        /** {@inheritDoc} */
        public void handleEvent(final TreeEvent te) {
            _ut.displayUsersFor(te.tree.getSelectedItem());
            _view.setRightHandPane(_ut);
        }

    }

    LeftRightPane _view;

    /**
     * Constructor.
     * @param _view
     *
     * @param rsa
     * @param root
     */
    UserTree(final LeftRightPane view) {

        _view = view;

        final TreeItem users = new TreeItem("Users");
        final TreeItem all = new TreeItem("All");
        final TreeItem creator = new TreeItem("Content creator");
        final TreeItem builder = new TreeItem("Site Builder");
        final TreeItem admin = new TreeItem("Administrator");
        final TreeItem search = new TreeItem("Search");
        getRootItem().add(users);
        users.add(all);
        users.add(search);
        all.add(creator);
        all.add(builder);
        all.add(admin);


        setSelectionMode(SelectionMode.SINGLE);
        setStyleAttribute("background", "white");

        addListener(
            Events.SelectionChange,
            new UserSelectedListener());
    }
}
