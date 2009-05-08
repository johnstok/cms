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

import ccc.contentcreator.api.QueriesService;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.TreeBinder;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Renders a tree of resources.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTree extends Tree {

    private final TreeStore<ResourceSummaryModelData> _store;
    private final ResourceSummary _root;
    private final QueriesServiceAsync _qs = GWT.create(QueriesService.class);

    /**
     * Constructor.
     *
     * @param root The root of the tree.
     */
    public ResourceTree(final ResourceSummary root) {

        _root = root;

        setSelectionMode(SelectionMode.SINGLE);
        setStyleAttribute("background", "white");


        final RpcProxy<ResourceSummaryModelData, List<ResourceSummaryModelData>> proxy =
            new RpcProxy<ResourceSummaryModelData, List<ResourceSummaryModelData>>() {
            @Override
            protected void load(final ResourceSummaryModelData loadConfig,
                                final AsyncCallback<List<ResourceSummaryModelData>> callback) {

                final String parentId =
                  (null==loadConfig) ? _root.getId().toString() : loadConfig.getId().toString();

                _qs.getChildren(
                    parentId,
                    new AsyncCallback<Collection<ResourceSummary>>(){

                    public void onFailure(final Throwable arg0) {
                        callback.onFailure(arg0);
                    }

                    public void onSuccess(
                                      final Collection<ResourceSummary> arg0) {
                        callback.onSuccess(
                            DataBinding.bindResourceSummary(arg0));
                    }
                });
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
        binder.setIconProvider(new ModelStringProvider<ResourceSummaryModelData>() {
            public String getStringValue(final ResourceSummaryModelData model,
                                         final String property) {
                if (model.getType().equals("FOLDER")) {
                    return "images/gxt/icons/folder.gif";
                } else if (model.getType().equals("PAGE")) {
                    return "images/icons/page.png";
                } else if (model.getType().equals("TEMPLATE")) {
                    return "images/icons/page_code.png";
                } else if (model.getType().equals("ALIAS")) {
                    return "images/icons/link.png";
                } else if (model.getType().equals("FILE")) {
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
    public TreeStore<ResourceSummaryModelData> store() {
        return _store;
    }
}
