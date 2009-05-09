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

import ccc.contentcreator.api.QueriesService;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.services.api.ResourceSummary;
import ccc.services.api.ResourceType;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.TreeBinder;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.store.TreeStoreEvent;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Renders a tree of folders.
 *
 * @author Civic Computing Ltd.
 */
public class FolderResourceTree extends Tree {

    /**
     * TODO: Add Description for this type.
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
        FolderBinder(final Tree tree, final TreeStore store) {
            super(tree, store);
        }



        /** {@inheritDoc} */
        @Override
        protected void createAll() {
            for (final ResourceSummaryModelData root : store.getRootItems()) {
                final String path = "/" + root.getName();
                root.setAbsolutePath(path);
            }
            super.createAll();
        }



        /** {@inheritDoc} */
        @Override
        protected void renderChildren(final ResourceSummaryModelData parent,
                                      final List<ResourceSummaryModelData> children) {
            final String parentPath = parent.getAbsolutePath();

            for (final ResourceSummaryModelData child : children) {
                final String path = parentPath + "/" + child.getName();
                child.setAbsolutePath(path);
            }

            super.renderChildren(parent, children);
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
            loader.loadChildren(item.getModel());
        }

        /** {@inheritDoc} */
        @Override
        protected void onRenderChildren(final TreeStoreEvent te) {
            if (loader.hasChildren(te.parent)) {
                super.onRenderChildren(te);
            }
        }
    }

    protected final TreeStore<ResourceSummaryModelData> _store;
    private final ResourceSummary _root;

    final QueriesServiceAsync qs = GWT.create(QueriesService.class);
    protected final FolderBinder _binder;

    /**
     * Constructor.
     *
     * @param root The root of the tree.
     */
    public FolderResourceTree(final ResourceSummary root) {

        _root = root;

        setSelectionMode(SelectionMode.SINGLE);
        setStyleAttribute("background", "white");


        final RpcProxy<ResourceSummaryModelData, List<ResourceSummaryModelData>> proxy =
            new RpcProxy<ResourceSummaryModelData, List<ResourceSummaryModelData>>() {
            @Override
            protected void load(final ResourceSummaryModelData loadConfig,
                                final AsyncCallback<List<ResourceSummaryModelData>> callback) {


                if (null==loadConfig) {
                    callback.onSuccess(DataBinding.bindResourceSummary(Collections.singletonList(_root)));
                } else {
                    qs.getChildren(
                        loadConfig.getId(),
                        new AsyncCallback<Collection<ResourceSummary>>(){

                            public void onFailure(final Throwable arg0) {
                                callback.onFailure(arg0);
                            }

                            public void onSuccess(final Collection<ResourceSummary> arg0) {
                                callback.onSuccess(DataBinding.bindResourceSummary(arg0));
                            }
                        }
                    );
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
}
