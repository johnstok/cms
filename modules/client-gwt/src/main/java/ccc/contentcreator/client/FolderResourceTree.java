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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ccc.contentcreator.actions.GetChildrenAction;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.rest.dto.ResourceSummary;
import ccc.types.ResourceType;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.TreeBinder;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.store.TreeStoreEvent;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Renders a tree of folders.
 *
 * @author Civic Computing Ltd.
 */
public class FolderResourceTree extends Tree {

    private final ResourceSummary _root;
    private final IGlobals _globals;

    private final TreeStore<ResourceSummaryModelData> _store;
    private final FolderBinder _binder;

    /**
     * Constructor.
     *
     * @param root The root of the tree.
     * @param globals IGlobals globals.
     */
    public FolderResourceTree(final ResourceSummary root,
                              final IGlobals globals) {

        _root = root;
        _globals = globals;

        setSelectionMode(SelectionMode.SINGLE);
        setStyleAttribute("background", "white");


        final RpcProxy<List<ResourceSummaryModelData>> proxy =
            new RpcProxy<List<ResourceSummaryModelData>>() {

            @Override
            protected void load(
                final Object loadConfig,
                final AsyncCallback<List<ResourceSummaryModelData>> callback) {

                if (null==loadConfig
                    || !(loadConfig instanceof ResourceSummaryModelData)) {
                    callback.onSuccess(
                        DataBinding.bindResourceSummary(
                            Collections.singletonList(_root)));
                } else {
                    new GetChildrenAction(_globals.userActions().loadData(),
                        ((ResourceSummaryModelData) loadConfig).getId()) {


                        /** {@inheritDoc} */
                        @Override protected void onFailure(final Throwable t) {
                            callback.onFailure(t);
                        }


                        /** {@inheritDoc} */
                        @Override protected void execute(
                                 final Collection<ResourceSummary> children) {
                            callback.onSuccess(
                                DataBinding.bindResourceSummary(children));
                        }
                    }.execute();
                }
            }
        };


        final TreeLoader<ResourceSummaryModelData> loader =
            new BaseTreeLoader<ResourceSummaryModelData>(proxy) {
            @Override
            public boolean hasChildren(final ResourceSummaryModelData parent) {
                final int folderCount = parent.getFolderCount();
                return folderCount > 0;
            }
        };

        _store = new TreeStore<ResourceSummaryModelData>(loader);

        _binder = new FolderBinder(this, _store);
        _binder.setCaching(false);
        _binder.setDisplayProperty(ResourceSummaryModelData.DISPLAY_PROPERTY);
        _binder.setIconProvider(new ResourceIconProvider());

        loader.load(null);
    }


    /**
     * Accessor.
     *
     * @return Returns the binder.
     */
    protected FolderBinder getBinder() {
        return _binder;
    }


    /**
     * Accessor for this tree's data store.
     *
     * @return The internal store.
     */
    public TreeStore<ResourceSummaryModelData> store() {
        return _store;
    }


    /**
     * Tree binder for folder trees.
     *
     * @author Civic Computing Ltd.
     */
    final class FolderBinder
        extends
            TreeBinder<ResourceSummaryModelData> {

        /**
         * Constructor.
         *
         * @param tree The UI tree used for rendering.
         * @param store The underlying data store for the tree.
         */
        FolderBinder(final Tree tree,
                     final TreeStore<ResourceSummaryModelData> store) {
            super(tree, store);
        }


        /** {@inheritDoc} */
        @Override
        protected TreeItem createItem(final ResourceSummaryModelData model) {
            final TreeItem item = super.createItem(model);

            item.setId("/"+_root.getName()+model.getAbsolutePath());

            if (ResourceType.FOLDER!=model.getType()) {
                item.setVisible(false);
            }

            return item;
        }

        /**
         * Load the children for a specified tree node.
         *
         * @param item The node whose children should be loaded.
         */
        protected void loadChildren(final TreeItem item) {
            loader.loadChildren((ResourceSummaryModelData) item.getModel());
        }

        /** {@inheritDoc} */
        @Override
        protected void onRenderChildren(
                            final TreeStoreEvent<ResourceSummaryModelData> te) {
            if (loader.hasChildren(te.getParent())) {
                super.onRenderChildren(te);
            }
        }
    }
}
