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

package ccc.client.gwt.widgets;

import ccc.api.core.Group;
import ccc.api.core.PagedCollection;
import ccc.api.types.SortOrder;
import ccc.client.gwt.core.Globals;
import ccc.client.gwt.core.I18n;
import ccc.client.gwt.core.ImagePaths;
import ccc.client.gwt.i18n.UIConstants;
import ccc.client.gwt.remoting.ListGroups;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;

/**
 * Tree for users. Users are organised based on their groups.
 *
 * @author Civic Computing Ltd.
 */
public class UserTree extends Tree {

    /** USERS : String. */
    public static final String USERS = "Users";

    /** SEARCH : String. */
    public static final String SEARCH = "Search";

    private final UserTable _userTable = new UserTable();
    private final GroupTable _groupTable = new GroupTable();
    private final UIConstants _constants = I18n.UI_CONSTANTS;
    private final LeftRightPane _view;
    private final ModelData _users = getNewItem(_constants.users(), USERS);

    /**
     * Constructor.
     *
     * @param view LeftRightPane of the surrounding view.
     */
    UserTree(final LeftRightPane view) {
        _view = view;

        _tree.setDisplayProperty("name");
        _tree.setIconProvider(new ModelIconProviderImplementation());
        _tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _tree.setStyleAttribute(BACKGROUND_ATTRIBUTE, BACKGROUND_COLOUR);
        _tree.getSelectionModel().addSelectionChangedListener(
            new UserSelectedListener());

        _store.add(_users, ADD_ALL_CHILDREN);
        _tree.setLeaf(_users, IS_NOT_LEAF);
        _tree.setExpanded(_users, EXPANDED);

        final ModelData groups = getNewItem(_constants.groups(), "Groups");
        _store.add(groups, DONT_ADD_CHILDREN);
        _tree.setLeaf(groups, IS_LEAF);

        final ModelData search = getNewItem(
            _constants.search(),
            SEARCH,
            ImagePaths.SEARCH);
        _store.add(_users, search, DONT_ADD_CHILDREN);
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
            if (selectedItem != null) {
                if ("Groups".equals(selectedItem.get("id"))) {
                    _view.setRightHandPane(_groupTable);
                    _groupTable.displayGroups();
                } else {
                    _view.setRightHandPane(_userTable);
                    _userTable.displayUsersFor(selectedItem);
                }
            }
        }
    }

    /**
     * Sets user table to be the content of the right hand pane.
     */
    @Override
    public void showTable() {
        new ListGroups(1, Globals.MAX_FETCH, "name", SortOrder.ASC) {
            @Override
            protected void execute(final PagedCollection<Group> groups) {
                _store.removeAll(_users);
                for (Group group : groups.getElements()) {
                    ModelData userModel = null;
                    if ("ADMINISTRATOR".equals(group.getName())) {
                        userModel = getNewItem(
                            group.getName(),
                            group.getName(),
                            ImagePaths.ADMINISTRATOR);
                    } else {
                        userModel = getNewItem(
                            group.getName(),
                            group.getName(),
                            ImagePaths.USER);
                    }

                    _store.add(_users, userModel, DONT_ADD_CHILDREN);
                    _tree.setLeaf(userModel, IS_LEAF);
                }

            }}.execute();
        _view.setRightHandPane(_userTable);
    }
}
