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
import ccc.contentcreator.dto.FolderDTO;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.TreeBinder;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTree extends Tree {

    private final TreeStore<FolderDTO> _store;

    /**
     * Constructor.
     *
     * @param rsa ResourceServiceAsync.
     * @param root The root of the tree.
     */
    ResourceTree(final ResourceServiceAsync rsa, final Root root) {

        setSelectionMode(SelectionMode.SINGLE);
        setStyleAttribute("background", "white");


        final RpcProxy<FolderDTO, List<FolderDTO>> proxy =
            new RpcProxy<FolderDTO, List<FolderDTO>>() {
            @Override
            protected void load(final FolderDTO loadConfig,
                                final AsyncCallback<List<FolderDTO>> callback) {
                if (null == loadConfig) {
                    rsa.getRoot(root,
                        new OneItemListCallback<FolderDTO>(callback));
                } else {
                    rsa.getFolderChildren(loadConfig, callback);
                }
            }
        };

        final TreeLoader<FolderDTO> loader =
            new BaseTreeLoader<FolderDTO>(proxy) {
            @Override
            public boolean hasChildren(final FolderDTO parent) {
                return parent.getFolderCount() > 0;
            }
        };

        _store = new TreeStore<FolderDTO>(loader);


        final TreeBinder<FolderDTO> binder =
            new TreeBinder<FolderDTO>(this, _store) {
            @Override
            protected void update(final TreeItem item, final FolderDTO model) {
                super.update(item, model);
                item.setId(model.getName());
            }
        };
        binder.setCaching(false);
        binder.setIconProvider(new ModelStringProvider<FolderDTO>() {
            public String getStringValue(final FolderDTO model,
                                         final String property) {
                return (null == model) ? null : "images/gxt/icons/folder.gif";
            }
        });

        loader.load(null);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public TreeStore<FolderDTO> store() {
        return _store;
    }

    public static final void getArray(final String relativeURL,
                                      final AsyncCallback<List<JsonModelData>> callback) {
             try {
                 final String url = Globals.hostURL()+relativeURL;
                 final RequestBuilder builder =
                     new RequestBuilder(RequestBuilder.GET, url);
                 builder.sendRequest(null, new AsyncCallbackBridge(callback));
             } catch (final RequestException e) {
                 callback.onFailure(e);
             }
         }
}
