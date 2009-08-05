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

import ccc.contentcreator.api.UIConstants;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.Events;
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
    private final UIConstants _constants = new IGlobalsImpl().uiConstants();
    private final LeftRightPane _view;

    /** USERS : String. */
    public static final String USERS = "Users";
    /** ALL : String. */
    public static final String ALL = "All";
    /** CONTENT_CREATOR : String. */
    public static final String CONTENT_CREATOR = "Content creator";
    /** SITE_BUILDER : String. */
    public static final String SITE_BUILDER = "Site Builder";
    /** ADMINISTRATOR : String. */
    public static final String ADMINISTRATOR = "Administrator";
    /** SEARCH : String. */
    public static final String SEARCH = "Search";

    /**
     * Selection listener for {@link UserTree}.
     *
     * @author Civic Computing Ltd.
     */
    public class UserSelectedListener implements Listener<TreeEvent> {

        /** {@inheritDoc} */
        public void handleEvent(final TreeEvent te) {
            _ut.displayUsersFor(te.getTree().getSelectedItem());
        }

    }

    /**
     * Constructor.
     *
     * @param view LeftRightPane of the surrounding view.
     */
    UserTree(final LeftRightPane view) {

        _view = view;

        final TreeItem users = new TreeItem(_constants.users());
        users.setId(USERS);
        final TreeItem all = new TreeItem(_constants.all());
        all.setId(ALL);
        final TreeItem creator = new TreeItem(_constants.contentCreator());
        creator.setIconStyle("images/icons/user.gif");
        creator.setId(CONTENT_CREATOR);

        final TreeItem builder = new TreeItem(_constants.siteBuilder());
        builder.setIconStyle("images/icons/user.gif");
        builder.setId(SITE_BUILDER);

        final TreeItem admin = new TreeItem(_constants.administrator());
        admin.setId(ADMINISTRATOR);
        admin.setIconStyle("images/icons/user_gray.gif");

        final TreeItem search = new TreeItem(_constants.search());
        search.setId(SEARCH);
        search.setIconStyle("images/icons/magnifier.gif");

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

    /**
     * Sets user table to be the content of the right hand pane.
     */
    public void showTable() {
        _view.setRightHandPane(_ut);
    }
}
