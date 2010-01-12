/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
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

import ccc.contentcreator.binding.ResourceSummaryModelData;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;


/**
 * Abstract base class for resource trees.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractResourceTree {

    private final TreePanel<ResourceSummaryModelData> _tree;
    private final TreeStore<ResourceSummaryModelData> _store;
    private final BaseTreeLoader<ResourceSummaryModelData> _loader;

    /**
     * Constructor.
     *
     */
    public AbstractResourceTree() {
        _loader = createLoader();

        _store = new TreeStore<ResourceSummaryModelData>(_loader);
        _tree = new TreePanel<ResourceSummaryModelData>(_store);
        _tree.setCaching(false);
        _tree.setDisplayProperty(ResourceSummaryModelData.DISPLAY_PROPERTY);
        _tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _tree.setStyleAttribute("background", "white");

        _tree.setIconProvider(
            new ModelIconProvider<ResourceSummaryModelData>() {
            @Override
            public AbstractImagePrototype getIcon(
                                        final ResourceSummaryModelData model) {
                return IconHelper.createPath(
                    new ResourceIconProvider().getStringValue(model, null));
            }
        });
    }

    /**
     * Accessor for this tree's tree panel.
     *
     * @return The internal tree.
     */
    public TreePanel<ResourceSummaryModelData> treePanel() {
        return _tree;
    }

    /**
     * Loads data using loader.
     *
     */
    public void load() {
        _loader.load(null);
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
     * Creates {@link RpcProxy} for resource data.
     *
     * @return Returns proxy for data.
     */
    protected abstract RpcProxy<List<ResourceSummaryModelData>> createProxy();

    /**
     * Creates tree specific {@link BaseTreeLoader}.
     *
     * @return A base tree loader.
     */
    protected abstract BaseTreeLoader<ResourceSummaryModelData> createLoader();

}
