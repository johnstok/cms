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
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;


/**
 * Tree for users. Users are grouped based on their roles.
 *
 * @author Civic Computing Ltd.
 */
public class UserTree {

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

    private final TreeStore<ModelData> _store = new TreeStore<ModelData>();
    private final TreePanel<ModelData> _tree = new TreePanel<ModelData>(_store);
    /**
     * Selection listener for {@link UserTree}.
     *
     * @author Civic Computing Ltd.
     */
    public class UserSelectedListener extends SelectionChangedListener<ModelData> {

        /** {@inheritDoc} */
        @Override
        public void selectionChanged(final SelectionChangedEvent<ModelData> se) {
            final ModelData objItem = se.getSelectedItem();
            _ut.displayUsersFor(objItem);
        }
    }

    private ModelData getNewItem(final String name, final String id) {
        return getNewItem(name, id, null);
    }

    private ModelData getNewItem(final String name,
                                 final String id,
                                 final String iconPath) {
        final ModelData objItem = new BaseModelData();
        objItem.set("name", name);
        objItem.set("id", id);
        if (iconPath != null) {
            objItem.set("icon", iconPath);
        }
        return objItem;
    }

    /**
     * Constructor.
     *
     * @param view LeftRightPane of the surrounding view.
     */
    UserTree(final LeftRightPane view) {
        _view = view;
        _tree.setDisplayProperty("name");

        _tree.setIconProvider(new ModelIconProvider<ModelData>() {
            public AbstractImagePrototype getIcon(final ModelData model) {
                if (model.get("icon") != null) {
                    return IconHelper.createPath((String) model.get("icon"));
                }
                return null;
            }
        });

        _tree.setHeight(300);

        final ModelData users = getNewItem(_constants.users(), USERS);
        _store.add(users, true);
        _tree.setLeaf(users, false);
        _tree.setExpanded(users, true);

        final ModelData all = getNewItem(_constants.all(), ALL);
        _store.add(users, all, false);
        _tree.setLeaf(all, false);

        final ModelData creator = getNewItem(
            _constants.contentCreator(),
            CONTENT_CREATOR,
            "images/icons/user.gif");
        _store.add(all, creator, false);
        _tree.setLeaf(creator, true);

        final ModelData builder = getNewItem(
            _constants.siteBuilder(),
            SITE_BUILDER,
            "images/icons/user.gif");
        _store.add(all, builder, false);
        _tree.setLeaf(builder, true);

        final ModelData admin = getNewItem(
            _constants.administrator(),
            ADMINISTRATOR,
            "images/icons/user_gray.gif");
        _store.add(all, admin, false);
        _tree.setLeaf(admin, true);

        final ModelData search = getNewItem(
            _constants.search(),
            SEARCH,
            "images/icons/magnifier.gif");
        _store.add(users, search, false);
        _tree.setLeaf(all, false);

        _tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _tree.setStyleAttribute("background", "white");

        _tree.getSelectionModel().addSelectionChangedListener(
            new UserSelectedListener());

    }

    /**
     * Accessor.
     *
     * @return The tree.
     */
    public TreePanel<ModelData> getTree() {
        return _tree;
    }

    /**
     * Sets user table to be the content of the right hand pane.
     */
    public void showTable() {
        _view.setRightHandPane(_ut);
    }
}
