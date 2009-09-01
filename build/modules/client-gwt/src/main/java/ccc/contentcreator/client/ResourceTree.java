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

import java.util.Collection;
import java.util.List;

import ccc.contentcreator.actions.GetChildrenAction;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.rest.dto.ResourceSummary;
import ccc.types.ID;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.TreeBinder;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Renders a tree of resources.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTree extends Tree {

    private final IGlobals _globals;
    private final TreeStore<ResourceSummaryModelData> _store;
    private final ResourceSummary _root;


    /**
     * Constructor.
     *
     * @param root The root of the tree.
     * @param globals IGlobals implementation.
     */
    public ResourceTree(final ResourceSummary root, final IGlobals globals) {

        _root = root;
        _globals = globals;

        setSelectionMode(SelectionMode.SINGLE);
        setStyleAttribute("background", "white");


        final RpcProxy<List<ResourceSummaryModelData>> proxy =
            new RpcProxy<List<ResourceSummaryModelData>>() {

            @Override
            protected void load(final Object loadConfig,
                                final AsyncCallback<List<ResourceSummaryModelData>> callback) {

                final ID parentId =
                    (null==loadConfig || !(loadConfig instanceof ResourceSummaryModelData))
                    ? _root.getId() : ((ResourceSummaryModelData)loadConfig).getId();

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

        _store = new TreeStore<ResourceSummaryModelData>(loader);


        final TreeBinder<ResourceSummaryModelData> binder =
            new TreeBinder<ResourceSummaryModelData>(this, _store) {
            @Override
            protected void update(final TreeItem item,
                                  final ResourceSummaryModelData model) {
                super.update(item, model);
                item.setId(model.getName());
            }
        };
        binder.setDisplayProperty(ResourceSummaryModelData.DISPLAY_PROPERTY);
        binder.setCaching(false);
        binder.setIconProvider(new ResourceIconProvider());

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
}
