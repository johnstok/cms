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
 * Tree for users. Users are grouped based on their roles.
 *
 * @author Civic Computing Ltd.
 */
public class UserTree extends Tree {

    private final UserTable _ut = new UserTable();

    private LeftRightPane _view;

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

    /**
     * Constructor.
     *
     * @param view LeftRightPane of the surrounding view.
     */
    UserTree(final LeftRightPane view) {

        _view = view;

        final TreeItem users = new TreeItem("Users");
        users.setId("Users");
        final TreeItem all = new TreeItem("All");
        all.setId("All");
        final TreeItem creator = new TreeItem("Content creator");
        creator.setIconStyle("images/icons/user.png");
        creator.setId("Content creator");

        final TreeItem builder = new TreeItem("Site Builder");
        builder.setIconStyle("images/icons/user.png");
        builder.setId("Site Builder");

        final TreeItem admin = new TreeItem("Administrator");
        admin.setId("Administrator");
        admin.setIconStyle("images/icons/user_gray.png");

        final TreeItem search = new TreeItem("Search");
        search.setId("Search");
        search.setIconStyle("images/icons/magnifier.png");

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
