/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

package ccc.contentcreator.client;

import ccc.contentcreator.api.UIConstants;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;

/**
 * Tree for users. Users are grouped based on their roles.
 *
 * @author Civic Computing Ltd.
 */
public class UserTree extends Tree {

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
    /** USER_TREE_HEIGHT : int. 
     * TODO load USER_TREE_HEIGHT from properties file*/
    private static final int USER_TREE_HEIGHT = 300;

    private final UserTable _userTable = new UserTable();
    private final UIConstants _constants = new IGlobalsImpl().uiConstants();
    private final LeftRightPane _view;

    /**
     * Constructor.
     *
     * @param view LeftRightPane of the surrounding view.
     */
    UserTree(final LeftRightPane view) {
        _view = view;
        
        _tree.setDisplayProperty("name");
        _tree.setHeight(USER_TREE_HEIGHT);
        _tree.setIconProvider(new ModelIconProviderImplementation());
        _tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _tree.setStyleAttribute(BACKGROUND_ATTRIBUTE, BACKGROUND_COLOUR);
        _tree.getSelectionModel().addSelectionChangedListener(
            new UserSelectedListener());
        
        final ModelData users = getNewItem(_constants.users(), USERS);
        _store.add(users, ADD_ALL_CHILDREN);
        _tree.setLeaf(users, IS_NOT_LEAF);
        _tree.setExpanded(users, EXPANDED);

        final ModelData all = getNewItem(_constants.all(), ALL);
        _store.add(users, all, DONT_ADD_CHILDREN);
        _tree.setLeaf(all, IS_NOT_LEAF);

        final ModelData creator = getNewItem(
            _constants.contentCreator(),
            CONTENT_CREATOR,
            ImagePaths.USER);
        _store.add(all, creator, DONT_ADD_CHILDREN);
        _tree.setLeaf(creator, IS_LEAF);

        final ModelData builder = getNewItem(
            _constants.siteBuilder(),
            SITE_BUILDER,
            ImagePaths.USER);
        _store.add(all, builder, DONT_ADD_CHILDREN);
        _tree.setLeaf(builder, IS_LEAF);

        final ModelData admin = getNewItem(
            _constants.administrator(),
            ADMINISTRATOR,
            ImagePaths.ADMINISTRATOR);
        _store.add(all, admin, DONT_ADD_CHILDREN);
        _tree.setLeaf(admin, IS_LEAF);

        final ModelData search = getNewItem(
            _constants.search(),
            SEARCH,
            ImagePaths.SEARCH);
        _store.add(users, search, DONT_ADD_CHILDREN);
        _tree.setLeaf(all, IS_NOT_LEAF);
    }
    
    /**
     * Selection listener for {@link UserTree}.
     *
     * @author Civic Computing Ltd.
     */
    public class UserSelectedListener
        extends
            SelectionChangedListener<ModelData> {

        /** {@inheritDoc} */
        @Override
        public void selectionChanged(final SelectionChangedEvent<ModelData>
                                     selectionChangedEvent) {
            final ModelData selectedItem = 
                selectionChangedEvent.getSelectedItem();
            _userTable.displayUsersFor(selectedItem);
        }
    }

    /**
     * Sets user table to be the content of the right hand pane.
     */
    public void showTable() {
        _view.setRightHandPane(_userTable);
    }
}
