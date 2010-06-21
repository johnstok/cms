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

import ccc.api.types.CommentStatus;
import ccc.client.core.I18n;
import ccc.client.core.ImagePaths;
import ccc.client.i18n.UIConstants;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;

/**
 * Tree for users. Users are organised based on their group.
 *
 * @author Civic Computing Ltd.
 */
public class CommentTree extends Tree {

    /** USER_TREE_HEIGHT : int. */
    private static final int USER_TREE_HEIGHT = 300;

    private final CommentTable _commentTable = new CommentTable();
    private final UIConstants _constants = I18n.UI_CONSTANTS;
    private final LeftRightPane _view;

    /**
     * Constructor.
     *
     * @param view LeftRightPane of the surrounding view.
     */
    CommentTree(final LeftRightPane view) {
        _view = view;

        _tree.setDisplayProperty("name");
        _tree.setHeight(USER_TREE_HEIGHT);
        _tree.setIconProvider(new ModelIconProviderImplementation());
        _tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _tree.setStyleAttribute(BACKGROUND_ATTRIBUTE, BACKGROUND_COLOUR);
        _tree.getSelectionModel().addSelectionChangedListener(
            new UserSelectedListener());

        final ModelData all = getNewItem(_constants.comments(), null);
        _store.add(all, ADD_ALL_CHILDREN);
        _tree.setLeaf(all, IS_NOT_LEAF);
        _tree.setExpanded(all, EXPANDED);

        final ModelData approved = getNewItem(
            _constants.approved(),
            CommentStatus.APPROVED.name(),
            ImagePaths.TICK);
        _store.add(all, approved, DONT_ADD_CHILDREN);
        _tree.setLeaf(approved, IS_LEAF);

        final ModelData pending = getNewItem(
            _constants.pending(),
            CommentStatus.PENDING.name(),
            ImagePaths.HOURGLASS);
        _store.add(all, pending, DONT_ADD_CHILDREN);
        _tree.setLeaf(pending, IS_LEAF);

        final ModelData spam = getNewItem(
            _constants.spam(),
            CommentStatus.SPAM.name(),
            ImagePaths.EXCLAMATION);
        _store.add(all, spam, DONT_ADD_CHILDREN);
        _tree.setLeaf(spam, IS_LEAF);
    }

    /**
     * Selection listener for {@link CommentTree}.
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
            final String id =
                selectionChangedEvent.getSelectedItem().get("id");

            _commentTable.displayComments(
                (null==id) ? null : CommentStatus.valueOf(id));
        }
    }

    /**
     * Sets user table to be the content of the right hand pane.
     */
    @Override
    public void showTable() {
        _view.setRightHandPane(_commentTable);
    }
}
