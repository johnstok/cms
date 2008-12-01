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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ccc.contentcreator.api.QueriesService;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.services.api.FolderSummary;

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
    private final FolderSummary _root;

    final QueriesServiceAsync qs = GWT.create(QueriesService.class);

    /**
     * Constructor.
     *
     * @param rsa ResourceServiceAsync.
     * @param root The root of the tree.
     */
    public FolderResourceTree(final ResourceServiceAsync rsa, final FolderSummary root) {

        _root = root;

        setSelectionMode(SelectionMode.SINGLE);
        setStyleAttribute("background", "white");


        final RpcProxy<ModelData, List<ModelData>> proxy =
            new RpcProxy<ModelData, List<ModelData>>() {
            @Override
            protected void load(final ModelData loadConfig,
                                final AsyncCallback<List<ModelData>> callback) {
                String parentId =
                    (null==loadConfig) ? _root._id : loadConfig.<String>get("id");
                qs.getFolderChildren(
                                parentId,
                                new AsyncCallback<Collection<FolderSummary>>(){

                    public void onFailure(final Throwable arg0) {
                        callback.onFailure(arg0);
                    }

                    public void onSuccess(final Collection<FolderSummary> arg0) {
                        callback.onSuccess(bind(arg0));
                    }

                    private List<ModelData> bind(Collection<FolderSummary> arg0) {
                        List<ModelData> boundData = new ArrayList<ModelData>();
                        for (FolderSummary fs : arg0) {
                            boundData.add(new FolderSummaryModelData(fs));
                        }
                        return boundData;
                    }
                });
            }
        };


        final TreeLoader<ModelData> loader =
            new BaseTreeLoader<ModelData>(proxy) {
            @Override
            public boolean hasChildren(final ModelData parent) {
                int childCount = parent.<Integer>get("folderCount").intValue();
                return childCount > 0;
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
