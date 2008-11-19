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

import java.util.List;

import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.api.Root;
import ccc.contentcreator.callbacks.OneItemListCallback;
import ccc.contentcreator.dto.ResourceDTO;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.TreeBinder;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
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

    private final TreeStore<ResourceDTO> _store;

    /**
     * Constructor.
     *
     * @param rsa ResourceServiceAsync.
     * @param root The root of the tree.
     */
    public ResourceTree(final ResourceServiceAsync rsa, final Root root) {

        setSelectionMode(SelectionMode.SINGLE);
        setStyleAttribute("background", "white");


        final RpcProxy<ResourceDTO, List<ResourceDTO>> proxy =
            new RpcProxy<ResourceDTO, List<ResourceDTO>>() {
            @Override
            protected void load(final ResourceDTO loadConfig,
                                final AsyncCallback<List<ResourceDTO>> callback) {
                if (null == loadConfig) {
                    rsa.getRootAsResource(root,
                        new OneItemListCallback<ResourceDTO>(callback));
                } else {
                    rsa.getChildren(loadConfig, callback);
                }
            }
        };

        final TreeLoader<ResourceDTO> loader =
            new BaseTreeLoader<ResourceDTO>(proxy) {
            @Override
            public boolean hasChildren(final ResourceDTO parent) {
                if (parent.getType().equals("FOLDER")) {
                    return true;
                }
                return false;
            }
        };

        _store = new TreeStore<ResourceDTO>(loader);


        final TreeBinder<ResourceDTO> binder =
            new TreeBinder<ResourceDTO>(this, _store) {
            @Override
            protected void update(final TreeItem item,
                                  final ResourceDTO model) {
                super.update(item, model);
                item.setId(model.getName());
            }
        };
        binder.setCaching(false);
        binder.setIconProvider(new ModelStringProvider<ResourceDTO>() {
            public String getStringValue(final ResourceDTO model,
                                         final String property) {
                if (model.getType().equals("FOLDER")) {
                    return "images/gxt/icons/folder.gif";
                } else if (model.getType().equals("PAGE")
                        || model.getType().equals("ALIAS")) {
                    return "images/gxt/icons/columns.gif"; // Replace with page
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
    public TreeStore<ResourceDTO> store() {
        return _store;
    }
}
