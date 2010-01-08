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

import ccc.contentcreator.actions.GetChildrenFolderAction;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.rest.dto.ResourceSummary;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;


/**
 * Renders a tree of folders.
 *
 * @author Civic Computing Ltd.
 */
public class FolderResourceTree  {

    private final ResourceSummary _root;
    private final IGlobals _globals;

    private TreeStore<ResourceSummaryModelData> _store;
    private final TreePanel<ResourceSummaryModelData> _tree;


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
                    new GetChildrenFolderAction(
                        _globals.userActions().loadData(),
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
        _tree = new TreePanel<ResourceSummaryModelData>(_store);
        _tree.setCaching(false);
        _tree.setDisplayProperty(ResourceSummaryModelData.DISPLAY_PROPERTY);
        _tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _tree.setStyleAttribute("background", "white");
        _tree.setHeight(600);

        _tree.setIconProvider(new ModelIconProvider<ResourceSummaryModelData>() {
            @Override
            public AbstractImagePrototype getIcon(final ResourceSummaryModelData model) {
                return IconHelper.createPath(
                    new ResourceIconProvider().getStringValue(model, null));
            }
        });

        loader.load(null);
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
     * Accessor for this tree's tree.
     *
     * @return The internal tree.
     */
    public TreePanel<ResourceSummaryModelData> tree() {
        return _tree;
    }
}
