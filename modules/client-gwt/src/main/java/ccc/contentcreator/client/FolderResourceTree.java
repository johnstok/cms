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
import java.util.Collections;
import java.util.List;

import ccc.api.ResourceSummary;
import ccc.contentcreator.actions.GetChildrenAction;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.ResourceSummaryModelData;
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

    protected final TreeStore<ResourceSummaryModelData> _store;
    protected final FolderBinder _binder;

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
            protected void load(final Object loadConfig,
                                final AsyncCallback<List<ResourceSummaryModelData>> callback) {

                if (null==loadConfig || !(loadConfig instanceof ResourceSummaryModelData)) {
                    callback.onSuccess(
                        DataBinding.bindResourceSummary(
                            Collections.singletonList(_root)));
                } else {
                    new GetChildrenAction(_globals.userActions().loadData(),
                        ((ResourceSummaryModelData)loadConfig).getId()) {

                        // FIXME: Handle failure!
                        /*
                         * callback.onFailure(throwable);
                         */

                        @Override protected void execute(final Collection<ResourceSummary> children) {
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
    static final class FolderBinder
        extends
            TreeBinder<ResourceSummaryModelData> {

        /**
         * Constructor.
         *
         * @param tree
         * @param store
         */
        FolderBinder(final Tree tree,
                     final TreeStore<ResourceSummaryModelData> store) {
            super(tree, store);
        }


        /** {@inheritDoc} */
        @Override
        protected TreeItem createItem(final ResourceSummaryModelData model) {
            final TreeItem item = super.createItem(model);

            item.setId(model.getAbsolutePath());

            if (ResourceType.FOLDER!=model.getType()) {
                item.setVisible(false);
            }

            return item;
        }

        protected void loadChildren(final TreeItem item) {
            loader.loadChildren((ResourceSummaryModelData) item.getModel());
        }

        /** {@inheritDoc} */
        @Override
        protected void onRenderChildren(final TreeStoreEvent<ResourceSummaryModelData> te) {
            if (loader.hasChildren(te.getParent())) {
                super.onRenderChildren(te);
            }
        }
    }
}
