/*-----------------------------------------------------------------------------
 * Copyright (c) 2011 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import ccc.api.core.ResourceCriteria;
import ccc.api.core.ResourceSummary;
import ccc.api.types.CommandType;
import ccc.api.types.ResourcePath;
import ccc.api.types.SortOrder;
import ccc.client.core.InternalServices;
import ccc.client.events.Event;
import ccc.client.events.EventHandler;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.client.gwt.remoting.GetResourcesPagedAction;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.grid.GridViewConfig;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * A search panel that displays resources using {@link Grid}.
 *
 * @author Civic Computing Ltd.
 */
public class SearchTable
    extends
        TablePanel
    implements
        EventHandler<CommandType>,
        SingleSelectionModel,
        ColumnConfigSupport {

    private ListStore<BeanModel> _detailsStore =
        new ListStore<BeanModel>();

    private final ResourceSummary _root;
    private final ResourceTree _tree;
    private final Grid<BeanModel> _grid;
    private final PagingToolBar _pagerBar;
    private final ToolBar _toolBar;
    
    private final Button _searchButton;
    private final CheckBox _locked;
    private final TextField<String> _searchString;
    
    private final String _preferences;


    /**
     * Constructor.
     *
     * @param root ResourceSummary
     * @param tree FolderResourceTree
     */
    SearchTable(final ResourceSummary root,
                  final ResourceTree tree,
                  final String preferences) {

        InternalServices.REMOTING_BUS.registerHandler(this);
        _preferences = preferences;
        _root = root;
        _tree = tree;
        _toolBar = new ToolBar();
        

        _searchString = new TextField<String>();
        _searchString.setToolTip(UI_CONSTANTS.searchToolTip());
        
        _locked = new CheckBox();
        _locked.setBoxLabel(UI_CONSTANTS.locked());
        _locked.setFieldLabel(UI_CONSTANTS.locked());
        _locked.setValue(Boolean.FALSE);
        
        _searchButton = new Button(UI_CONSTANTS.search());
        _searchButton.addListener(Events.Select, new SearchListener());
        
        _toolBar.add(_searchString);
        _toolBar.add(_locked);
        _toolBar.add(_searchButton);
        
        setTopComponent(_toolBar);
        setHeading(UI_CONSTANTS.resourceDetails());
        setLayout(new FitLayout());

        final Menu contextMenu = new ResourceContextMenu(this, root);
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        final ContextActionGridPlugin gp =
            new ContextActionGridPlugin(contextMenu, this);
        gp.setRenderer(new ResourceContextRenderer());
        configs.add(gp);
        createColumnConfigs(configs);
        applyPreferences(configs);
        

        final ColumnModel cm = new ColumnModel(configs);
        _grid = new Grid<BeanModel>(_detailsStore, cm);
        setUpGrid();
        _grid.setContextMenu(contextMenu);
        _grid.addPlugin(gp);

        _pagerBar = new PagingToolBar(PAGING_ROW_COUNT);
        setBottomComponent(_pagerBar);
        add(_grid);
    }


    private void applyPreferences(List<ColumnConfig> configs) {
    	if (_preferences != null) {
    		for (ColumnConfig config : configs) {
    			if (_preferences.indexOf(config.getId()) == -1 
    					&& !config.getHeader().equals("")) {
    				config.setHidden(true);
    			} else {
    				config.setHidden(false);
    			}
    		}
    	}
    }


	/**
     * Accessor for this table's root.
     *
     * @return The resource summary for the root.
     */
    public ResourceSummary root() {
        return _root;
    }


    /**
     * Reloads the detail store.
     *
     */
    public void reload() {
        if (_detailsStore.getLoader() != null) {
            _detailsStore.getLoader().load();
        }
    }


    /**
     * Config method for the columns.
     *
     * @param configs list of configurations.
     */
    public void createColumnConfigs(final List<ColumnConfig> configs) {

        final GridCellRenderer<BeanModel> rsRenderer =
            ResourceTypeRendererFactory.rendererForResourceSummary();

        final ColumnConfig typeColumn =
            new ColumnConfig(
                ResourceSummary.TYPE,
                UI_CONSTANTS.type(),
                40);
        typeColumn.setRenderer(rsRenderer);
        configs.add(typeColumn);

        final ColumnConfig workingCopyColumn =
            new ColumnConfig(
                ResourceSummary.WORKING_COPY,
                UI_CONSTANTS.draft(),
                40);
        workingCopyColumn.setSortable(false);
        workingCopyColumn.setMenuDisabled(true);
        workingCopyColumn.setRenderer(rsRenderer);
        configs.add(workingCopyColumn);

        final ColumnConfig mmIncludeColumn =
            new ColumnConfig(
                ResourceSummary.MM_INCLUDE,
                UI_CONSTANTS.menu(),
                40);
        mmIncludeColumn.setRenderer(rsRenderer);
        configs.add(mmIncludeColumn);

        final ColumnConfig createdColumn =
            new ColumnConfig(
                ResourceSummary.DATE_CREATED,
                UI_CONSTANTS.dateCreated(),
                100);
        createdColumn.setDateTimeFormat(
            DateTimeFormat.getShortDateTimeFormat());
        createdColumn.setHidden(true);
        configs.add(createdColumn);

        final ColumnConfig updatedColumn =
            new ColumnConfig(
                ResourceSummary.DATE_CHANGED,
                UI_CONSTANTS.dateChanged(),
                100);
        updatedColumn.setDateTimeFormat(
            DateTimeFormat.getShortDateTimeFormat());
        updatedColumn.setHidden(true);
        configs.add(updatedColumn);

        final ColumnConfig visibleColumn =
            new ColumnConfig(
                ResourceSummary.VISIBLE,
                UI_CONSTANTS.visible(),
                45);
        visibleColumn.setRenderer(rsRenderer);
        visibleColumn.setHidden(true);
        configs.add(visibleColumn);

        final ColumnConfig lockedColumn =
            new ColumnConfig(
                ResourceSummary.LOCKED,
                UI_CONSTANTS.lockedBy(),
                80);
        configs.add(lockedColumn);

        final ColumnConfig publishedByColumn =
            new ColumnConfig(
                ResourceSummary.PUBLISHED,
                UI_CONSTANTS.publishedBy(),
                80);
        configs.add(publishedByColumn);

        final ColumnConfig nameColumn =
            new ColumnConfig(
                ResourceSummary.NAME,
                UI_CONSTANTS.name(),
                250);
        configs.add(nameColumn);

        final ColumnConfig titleColumn =
            new ColumnConfig(
                ResourceSummary.TITLE,
                UI_CONSTANTS.title(),
                250);
        configs.add(titleColumn);
        
        final ColumnConfig changedByColumn =
        	new ColumnConfig(
        	ResourceSummary.CHANGED_BY,
        	UI_CONSTANTS.changedBy(),
        	80);
            changedByColumn.setHidden(true);
            configs.add(changedByColumn);
    }


    private void setUpGrid() {
        _grid.setId("ResourceGrid");
        _grid.setLoadMask(true);
        _grid.setBorders(false);

        // Assign a CSS style for each row with GridViewConfig
        final GridViewConfig vc = new GridViewConfig() {
            /** {@inheritDoc} */
            @Override
            public String getRowStyle(final ModelData model,
                                      final int rowIndex,
                                      final ListStore<ModelData> ds) {
                final ResourceSummary rs =
                    ((BeanModel) model).<ResourceSummary>getBean();
                return rs.getName()+"_row";
            }
        };
        final GridView view = _grid.getView();
        view.setViewConfig(vc);
        _grid.setView(view);

        final GridSelectionModel<BeanModel> gsm =
            new GridSelectionModel<BeanModel>();
        gsm.setSelectionMode(SelectionMode.SINGLE);
        _grid.setSelectionModel(gsm);
        _grid.setAutoExpandColumn(
            ResourceSummary.TITLE);
    }


    /** {@inheritDoc} */
    public ResourceSummary tableSelection() {
        if (_grid.getSelectionModel() == null) {
            return null;
        }
        final BeanModel selected = _grid.getSelectionModel().getSelectedItem();
        return (null==selected) ? null : selected.<ResourceSummary>getBean();
    }


    /** {@inheritDoc} */
    public void update(final ResourceSummary model) {
        updateResource(model.getId());
        _tree.updateResource(model);
    }


    /**
     * Remove a resource from the data store.
     *
     * @param item The model to remove.
     */
    public void delete(final UUID item) {
        removeResource(item);
        _tree.removeResource(item);
    }


    /** {@inheritDoc} */
    @Override
    public void create(final ResourceSummary model) {
        _tree.addResource(model);
        addResource(model);
    }


    private void updateResource(final UUID id) {
        final BeanModel tBean =
            _detailsStore.findModel(ResourceSummary.UUID, id);
        if (null!=tBean) {
            _detailsStore.update(tBean);
        }
    }


    private void removeResource(final UUID id) {
        final BeanModel tBean =
            _detailsStore.findModel(ResourceSummary.UUID, id);
        if (null!=tBean) {
            _detailsStore.remove(tBean);
        }
    }


    private void addResource(final ResourceSummary model) {
//        if (_proxy.isDisplaying(model.getParent())) {
//            final BeanModel tBean = DataBinding.bindResourceSummary(model);
//            _detailsStore.add(tBean);
//        }
    }


    /** {@inheritDoc} */
    public void move(final ResourceSummary model,
                     final ResourceSummary newParent,
                     final ResourceSummary oldParent) {
        removeResource(model.getId());
        _tree.move(oldParent, newParent, model);
    }


    /** {@inheritDoc} */
    @Override
    public void handle(final Event<CommandType> event) {
        switch (event.getType()) {
	        case PAGE_UPDATE:
	        case FOLDER_UPDATE:
            case RESOURCE_PUBLISH:
                mergeAndUpdate(event.<ResourceSummary>getProperty("resource"));
                break;

            case RESOURCE_DELETE:
                delete(event.<UUID>getProperty("resource"));
                break;

            case FILE_CREATE:
            case PAGE_CREATE:
            case FOLDER_CREATE:
            case ALIAS_CREATE:
                create(event.<ResourceSummary>getProperty("resource"));
                break;

            case RESOURCE_RENAME:
                final BeanModel bm1 =
                    _detailsStore.findModel(
                        ResourceSummary.UUID, event.getProperty("id"));
                final ResourceSummary md1 = bm1.<ResourceSummary>getBean();
                md1.setAbsolutePath(
                    event.<ResourcePath>getProperty("path").toString());
                md1.setName(
                    event.<String>getProperty("name"));
                update(md1);
                break;

            case RESOURCE_CHANGE_TEMPLATE:
                final BeanModel bm2 =
                    _detailsStore.findModel(
                        ResourceSummary.UUID, event.getProperty("resource"));
                if (null==bm2) { return; } // Not present in table.
                final ResourceSummary md2 = bm2.<ResourceSummary>getBean();
                md2.setTemplateId(event.<UUID>getProperty("template"));
                update(md2);
                break;

            case RESOURCE_CLEAR_WC:
                final ResourceSummary item1 =
                    event.getProperty("resource");
                item1.setHasWorkingCopy(false);
                update(item1);
                break;

            case RESOURCE_APPLY_WC:
                final ResourceSummary item2 =
                    event.getProperty("resource");
                item2.setHasWorkingCopy(false);
                update(item2);
                break;

            default:
                break;
        }
    }


    private void mergeAndUpdate(final ResourceSummary rs) {
        final BeanModel tBean =
            _detailsStore.findModel(ResourceSummary.UUID, rs.getId());
        if (null!=tBean) {
            tBean.setProperties(
                DataBinding.bindResourceSummary(rs).getProperties());
            update(tBean.<ResourceSummary>getBean());
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary currentFolder() {
        throw new UnsupportedOperationException("Method not implemented.");
    }
    
    public String visibleColumns() {
    	StringBuilder names = new StringBuilder();
    	List<ColumnConfig> columns =
    		_grid.getColumnModel().getColumns();
    	for (ColumnConfig c : columns) {
    		if (!c.isHidden()) {
    			if (names.length() > 0) {
    				names.append(",");
    			}
    			names.append(c.getId());
    		}
    	}
    	return names.toString();
    }


    @Override
    public String preferenceName() {
        return RESOURCE_COLUMNS;
    }
    
    /**
     * Listener for user search.
     *
     * @author Civic Computing Ltd.
     */
    private final class SearchListener implements Listener<ComponentEvent> {

        public void handleEvent(final ComponentEvent be) {
            if (_searchString.getValue() == null) {
                return;
            }
            _detailsStore.removeAll();
            updatePager();
        }
    }
    

    private void updatePager(){

        final RpcProxy<PagingLoadResult<BeanModel>> proxy =
            new RpcProxy<PagingLoadResult<BeanModel>>() {

            @Override
            protected void load(final Object loadConfig,
                                final AsyncCallback<PagingLoadResult
                                <BeanModel>> callback) {
                if (null==loadConfig
                    || !(loadConfig instanceof BasePagingLoadConfig)) {
                    final PagingLoadResult<BeanModel> plr =
                       new BasePagingLoadResult<BeanModel>(null);
                    callback.onSuccess(plr);
                } else {
                    final BasePagingLoadConfig config =
                        (BasePagingLoadConfig) loadConfig;

                    final int page =  config.getOffset()/config.getLimit()+1;
                    final SortOrder order = (
                        config.getSortDir() == Style.SortDir.ASC
                        ? SortOrder.ASC : SortOrder.DESC);

                    final ResourceCriteria criteria = new ResourceCriteria();
                    criteria.setName(_searchString.getValue().replace('*', '%')+"%");
                    criteria.setSortField(config.getSortField());
                    criteria.setSortOrder(order);
                    if (_locked.getValue().booleanValue()) {
                        criteria.setLocked(true);
                    }

                    new GetResourcesPagedAction(criteria,
                        page,
                        config.getLimit()) {

                        /** {@inheritDoc} */
                        @Override protected void onFailure(final Throwable t) {
                            callback.onFailure(t);
                        }

                        /** {@inheritDoc} */
                        @Override protected void execute(
                                 final Collection<ResourceSummary> children,
                                 final int totalCount) {
                            final List<BeanModel> results =
                                DataBinding.bindResourceSummary(children);

                            final PagingLoadResult<BeanModel> plr =
                                new BasePagingLoadResult<BeanModel>(
                            results, config.getOffset(), totalCount);
                            callback.onSuccess(plr);
                        }
                    }.execute();
                }
            }
        };


        final PagingLoader loader = new BasePagingLoader(proxy);
        loader.setRemoteSort(true);
        _detailsStore = new ListStore<BeanModel>(loader);
        _pagerBar.bind(loader);
        loader.load(0, PAGING_ROW_COUNT);
        final ColumnModel cm = _grid.getColumnModel();
        _grid.reconfigure(_detailsStore, cm);
    }
}
