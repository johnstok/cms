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
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.TreeBinder;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.store.TreeStore;
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

    private final TreeStore<ModelData> _store;
    private final ResourceSummary _root;

    final QueriesServiceAsync qs = GWT.create(QueriesService.class);

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
                    String parentId = loadConfig.<String>get("id");

                    qs.getFolderChildren(
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
                int folderCount = parent.<Integer>get("folderCount").intValue();
                return folderCount > 0;
            }
        };

        _store = new TreeStore<ModelData>(loader);


        final TreeBinder<ModelData> binder =
            new TreeBinder<ModelData>(this, _store) {
            @Override
            protected void update(final TreeItem item, final ModelData model) {
                super.update(item, model);
                item.setId(model.<String>get("name"));
            }
        };
        binder.setCaching(false);
        binder.setDisplayProperty("name");
        binder.setIconProvider(new ModelStringProvider<ModelData>() {
            public String getStringValue(final ModelData model,
                                         final String property) {
                return (null == model) ? null : "images/gxt/icons/folder.gif";
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
