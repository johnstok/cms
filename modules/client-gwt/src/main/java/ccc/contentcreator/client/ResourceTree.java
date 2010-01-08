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
import java.util.List;
import java.util.UUID;

import ccc.contentcreator.actions.GetChildrenAction;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.rest.dto.ResourceSummary;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Renders a tree of resources.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTree{

    private final IGlobals _globals;
    private final ResourceSummary _root;

    private TreePanel<ResourceSummaryModelData> _tree = null;
    /**
     * Constructor.
     *
     * @param root The root of the tree.
     * @param globals IGlobals implementation.
     */
    public ResourceTree(final ResourceSummary root, final IGlobals globals) {

        _root = root;
        _globals = globals;

        final RpcProxy<List<ResourceSummaryModelData>> proxy =
            new RpcProxy<List<ResourceSummaryModelData>>() {

            @Override
            protected void load(
                final Object loadConfig,
                final AsyncCallback<List<ResourceSummaryModelData>> callback) {

                final UUID parentId =
                    (null==loadConfig
                        || !(loadConfig instanceof ResourceSummaryModelData))
                    ? _root.getId()
                    : ((ResourceSummaryModelData) loadConfig).getId();

                    new GetChildrenAction(_globals.userActions().loadData(),
                        parentId) {

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
        };

        final TreeLoader<ResourceSummaryModelData> loader =
            new BaseTreeLoader<ResourceSummaryModelData>(proxy) {
            @Override
            public boolean hasChildren(final ResourceSummaryModelData parent) {
                if (parent.getChildCount() > 0) {
                    return true;
                }
                return false;
            }
        };

        final TreeStore<ResourceSummaryModelData> _store;
        _store = new TreeStore<ResourceSummaryModelData>(loader);

        _tree = new TreePanel<ResourceSummaryModelData>(_store);
        _tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _tree.setStyleAttribute("background", "white");

        loader.load(null);
    }

    public TreePanel<ResourceSummaryModelData> getTree() {
        return _tree;
    }
}
