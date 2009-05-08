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
import ccc.services.api.ID;
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.TreeBinder;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
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
            TreeBinder<ModelData> {

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
            for (final ModelData root : store.getRootItems()) {
                final String path = "/" + root.get(DataBinding.NAME);
                root.set("absolutePath", path);
            }
            super.createAll();
        }



        /** {@inheritDoc} */
        @Override
        protected void renderChildren(final ModelData parent,
                                      final List<ModelData> children) {
            final String parentPath = parent.get("absolutePath");

            for (final ModelData child : children) {
                final String path = parentPath + "/" + child.get(DataBinding.NAME);
                child.set("absolutePath", path);
            }

            super.renderChildren(parent, children);
        }


        /** {@inheritDoc} */
        @Override
        protected TreeItem createItem(final ModelData model) {
            final TreeItem item = super.createItem(model);

            item.setId(model.<String>get("absolutePath"));

            if (!"FOLDER".equals(model.get(DataBinding.TYPE))) {
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

    protected final TreeStore<ModelData> _store;
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


        final RpcProxy<ModelData, List<ModelData>> proxy =
            new RpcProxy<ModelData, List<ModelData>>() {
            @Override
            protected void load(final ModelData loadConfig,
                                final AsyncCallback<List<ModelData>> callback) {


                if (null==loadConfig) {
                    callback.onSuccess(DataBinding.bindResourceSummary(Collections.singletonList(_root)));
                } else {
                    final String parentId = loadConfig.<ID>get(DataBinding.ID).toString();

                    qs.getChildren(
                        parentId,
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


        final TreeLoader<ModelData> loader =
            new BaseTreeLoader<ModelData>(proxy) {
            @Override
            public boolean hasChildren(final ModelData parent) {
                final int folderCount = parent.<Integer>get(DataBinding.FOLDER_COUNT).intValue();
                return folderCount > 0;
            }
        };

        _store = new TreeStore<ModelData>(loader);

        _binder = new FolderBinder(this, _store);
        _binder.setCaching(false);
        _binder.setDisplayProperty(DataBinding.NAME);
        _binder.setIconProvider(new ModelStringProvider<ModelData>() {
            public String getStringValue(final ModelData model,
                                         final String property) {
                if (model.<String>get(DataBinding.TYPE).equals("FOLDER")) {
                    return "images/gxt/icons/folder.gif";
                } else if (model.<String>get(DataBinding.TYPE).equals("PAGE")) {
                    return "images/icons/page.png";
                } else if (model.<String>get(DataBinding.TYPE).equals("TEMPLATE")) {
                    return "images/icons/page_code.png";
                } else if (model.<String>get(DataBinding.TYPE).equals("ALIAS")) {
                    return "images/icons/link.png";
                } else if (model.<String>get(DataBinding.TYPE).equals("FILE")) {
                    return "images/icons/image.png";
                } else {
                    return null;
                }
            }
        });

        loader.load(null);
    }

    /**
     * Accessor for this tree's data store.
     *
     * @return The internal store.
     */
    public TreeStore<ModelData> store() {
        return _store;
    }
}
