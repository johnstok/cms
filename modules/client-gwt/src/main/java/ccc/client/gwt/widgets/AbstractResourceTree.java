/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ccc.api.core.ResourceSummary;
import ccc.api.types.DBC;
import ccc.api.types.ResourceType;
import ccc.client.core.Globals;
import ccc.client.gwt.binding.DataBinding;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;


/**
 * Abstract base class for resource trees.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractResourceTree {

    private final TreePanel<BeanModel>      _tree;
    private final TreeStore<BeanModel>      _store;
    private final BaseTreeLoader<BeanModel> _loader;
    private final ResourceSummary           _root;


    /**
     * Constructor.
     *
     * @param root The root of the tree.
     */
    public AbstractResourceTree(final ResourceSummary root) {
        _root = DBC.require().notNull(root);
        _loader = createLoader();

        _store = new TreeStore<BeanModel>(_loader);
        _tree = new TreePanel<BeanModel>(_store);
        _tree.setCaching(false);
        _tree.setDisplayProperty(ResourceSummary.NAME);
        _tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _tree.setStyleAttribute("background", "white");

        _tree.setIconProvider(
            new ModelIconProvider<BeanModel>() {
            @Override
            public AbstractImagePrototype getIcon(
                                        final BeanModel model) {
                return IconHelper.createPath(
                    new ResourceIconProvider().getStringValue(model, null));
            }
        });
    }


    /**
     * Loads data using loader.
     *
     */
    public void load() {
        _loader.load(null);
    }


    /**
     * Add a listener for selection of resource in the tree.
     *
     * @param listener The listener to add.
     */
    protected final void addSelectionChangedListener(
                         final SelectionChangedListener<BeanModel> listener) {
        _tree.getSelectionModel().addSelectionChangedListener(listener);
    }


    /**
     * Add a listener for tree events.
     *
     * @param event The event to listen for.
     * @param listener The listener to notify.
     */
    protected final void addListener(
                         final EventType event,
                         final Listener<TreePanelEvent<BeanModel>> listener) {
        _tree.addListener(event, listener);
    }


    /**
     * Set a style attribute for the tree.
     *
     * @param attr The attribute to set.
     * @param value The value to set.
     */
    protected final void setStyleAttribute(final String attr,
                                           final String value) {
        _tree.setStyleAttribute(attr, value);
    }


    /**
     * Return the currently selected item.
     *
     * @return The selected resource.
     */
    public final ResourceSummary getSelectedItem() {
        final BeanModel selected = _tree.getSelectionModel().getSelectedItem();
        return
            (null==selected) ? null : selected.<ResourceSummary>getBean();
    }


    /**
     * Set the context menu for the tree.
     *
     * @param contextMenu The menu to set.
     */
    protected final void setContextMenu(final Menu contextMenu) {
        _tree.setContextMenu(contextMenu);
    }


    /**
     * Convert this resource tree to a UI component.
     *
     * @return A GXT component.
     */
    public final Component asComponent() { return _tree; }


    /**
     * Move a resource in the tree.
     *
     * @param model     The model to move.
     * @param oldParent The current parent of the model.
     * @param newParent The target parent of the model.
     */
    public void move(final ResourceSummary oldParent,
                     final ResourceSummary newParent,
                     final ResourceSummary model) {
        final BeanModel mBean =
            _store.findModel(ResourceSummary.UUID, model.getId());
        final BeanModel sBean =
            _store.findModel(ResourceSummary.UUID, model.getParent());
        final BeanModel tBean =
            _store.findModel(ResourceSummary.UUID, newParent.getId());

        // Correct the resource's fields.
        model.setAbsolutePath(newParent.getAbsolutePath()+"/"+model.getName());
        model.setParent(newParent.getId());

        if (ResourceType.FOLDER==model.getType()) {

            // Update the old parent.
            if (null!=sBean) { // May not exist in tree store.
                final ResourceSummary source = sBean.getBean();
                source.decrementFolderCount();
                if (source.getFolderCount()<1) {
                    _tree.setExpanded(sBean, false);
                }
                if (null!=mBean) {
                    _store.remove(mBean);
                }
                _store.update(sBean);
            }

            // Update the new parent.
            if (null!=tBean) { // May not exist in tree store.
                final ResourceSummary target = tBean.getBean();
                target.incrementFolderCount();
                if (null!=mBean) {
                    _store.add(tBean, mBean, false);
                } else {
                    _store.add(
                        tBean, DataBinding.bindResourceSummary(model), false);
                }
                _store.update(tBean);
            }
        }
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param item
     */
    public void removeResource(final UUID item) {
        final BeanModel tBean =
            _store.findModel(ResourceSummary.UUID, item);

        if (null!=tBean) {
            final ResourceSummary target = tBean.<ResourceSummary>getBean();
            final BeanModel pBean =
                _store.findModel(ResourceSummary.UUID, target.getParent());

            _store.remove(tBean);

            if (null!=pBean) {
                final ResourceSummary parent = pBean.<ResourceSummary>getBean();
                if (ResourceType.FOLDER==target.getType()) {
                    parent.decrementFolderCount();
                }
                _tree.setExpanded(pBean, false);
                if (parent.getFolderCount()<1) {
                    _tree.setLeaf(pBean, true);
                } else {
                    _tree.setExpanded(pBean, true);
                }
            }
            _store.update(pBean);
        }
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param model
     */
    public void updateResource(final ResourceSummary model) {
        final BeanModel tBean =
            _store.findModel(ResourceSummary.UUID, model.getId());
        final BeanModel pBean =
            _store.findModel(ResourceSummary.UUID, model.getParent());

        _store.update(tBean);

        if (null!=pBean) {
            _tree.setExpanded(pBean, false);
            _tree.setExpanded(pBean, true);
        }
    }


    /**
     * Add a resource to the tree.
     *
     * @param res The resource to add.
     */
    public void addResource(final ResourceSummary res) {
        if (res.getType() == ResourceType.FOLDER) {

            final BeanModel pBean =
                _store.findModel(ResourceSummary.UUID, res.getParent());

            if (null!=pBean) { // May not exist in the store
                final BeanModel tBean = DataBinding.bindResourceSummary(res);
                _store.add(pBean, tBean, false);

                final ResourceSummary parent = pBean.<ResourceSummary>getBean();
                parent.incrementFolderCount();

                _store.update(tBean);
                _store.update(pBean);
            }
        }
    }


    /**
     * Creates {@link RpcProxy} for resource data.
     *
     * @return Returns proxy for data.
     */
    protected abstract RpcProxy<List<BeanModel>> createProxy();


    /**
     * Creates tree specific {@link BaseTreeLoader}.
     *
     * @return A base tree loader.
     */
    protected abstract BaseTreeLoader<BeanModel> createLoader();


    /**
     * Create list of range folders.
     *
     * @param count The parent folder or children count.
     * @param parent The parent folder.
     * @return The list of range folders.
     */
    protected List<ResourceSummary> createRangeFolders(
                                               final int count,
                                               final ResourceSummary parent) {

        final List<ResourceSummary> children = new ArrayList<ResourceSummary>();
        int page = 1;
        for (int i = 0; i < count; i=i+Globals.MAX_FETCH) {
            final ResourceSummary rs = new ResourceSummary();
            rs.setName(""+(i+1)+" ... "+((i+Globals.MAX_FETCH) > count
                    ? count
                    : i+Globals.MAX_FETCH));
            rs.setParent(parent.getId());
            rs.setId(parent.getId());
            rs.setAbsolutePath(""+page);
            rs.setFolderCount(1);
            rs.setChildCount(1);
            rs.setType(ResourceType.RANGE_FOLDER);
            rs.addLinks(parent.getLinks());
            children.add(rs);
            page++;
        }
        return children;
    }


    /**
     * Accessor.
     *
     * @return The root resource for this tree.
     */
    protected ResourceSummary getRoot() {
        return _root;
    }


    /**
     * Clear any selections made on the tree.
     */
    public void clearSelection() {
       _tree.getSelectionModel().deselectAll();
    }
}
