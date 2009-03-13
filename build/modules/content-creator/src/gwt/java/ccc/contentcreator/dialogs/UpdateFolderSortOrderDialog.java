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
package ccc.contentcreator.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Updates the sort order for a folder.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFolderSortOrderDialog
extends
AbstractEditDialog {

    private final CommandServiceAsync _commands = Globals.commandService();
    private final QueriesServiceAsync _queries = Globals.queriesService();
    private final ComboBox<ModelData> _sortOrder = new ComboBox<ModelData>();

    private final SingleSelectionModel _selectionModel;
    private final ListStore<ModelData> _sortStore = new ListStore<ModelData>();

    private final Grid<ModelData> _grid;
    final ColumnModel _cm;
    private ListStore<ModelData> _detailsStore =
        new ListStore<ModelData>();

    private GridDropTarget _target;


    /**
     * Constructor.
     *
     * @param ssm
     * @param currentSortOrder The current sort order.
     */
    public UpdateFolderSortOrderDialog(final SingleSelectionModel ssm,
                                       final String currentSortOrder) {
        super(Globals.uiConstants().folderSortOrder());
        setHeight(Globals.DEFAULT_HEIGHT);

        _selectionModel = ssm;

        populateSortOptions();

        configureComboBox(currentSortOrder,
            "folder-sort-order",
            false,
            constants().folderSortOrder());

        setCurrentValue(currentSortOrder);

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        createColumnConfigs(configs);
        _cm = new ColumnModel(configs);
        _grid = new Grid<ModelData>(_detailsStore, _cm);
        _grid.setHeight(300);
        _grid.setBorders(true);
        addField(_grid);

        final GridDragSource gds = new GridDragSource(_grid);
        configureDropTarget();

        _sortOrder.addSelectionChangedListener(
            new SelectionChangedListener<ModelData>() {
                @Override
                public void selectionChanged(final SelectionChangedEvent<ModelData> se) {
                    final ModelData md = se.getSelectedItem();
                    if (md != null) {
                        loadDetailStore();
                        final String order = md.<String>get("value");
                        if ("MANUAL".equals(order)) {
                            _grid.enable();
                        } else {
                            _grid.disable();
                            _detailsStore.sort("name", SortDir.ASC);
                        }
                        configureDropTarget();
                    }
                }
            });
    }

    private void loadDetailStore() {
        _detailsStore =  new ListStore<ModelData>();
        final String id = _selectionModel.tableSelection().get("id");
        _queries.getChildren(id,
            new AsyncCallback<Collection<ResourceSummary>>(){
            public void onFailure(final Throwable arg0) {
                _grid.disable();
            }
            public void onSuccess(final Collection<ResourceSummary> arg0) {
                _detailsStore.removeAll();
                _detailsStore.add(DataBinding.bindResourceSummary(arg0));
                _grid.reconfigure(_detailsStore, _cm);
            }
        });
    }

    private void configureDropTarget() {

        _target = new GridDropTarget(_grid) {
            /** {@inheritDoc} */
            @Override
            protected void onDragDrop(final DNDEvent e) {
                final Object data = e.data;
                // fix to avoid losing items if placed to last
                if (data instanceof ModelData) {
                    final int x = grid.getStore().indexOf((ModelData) data);
                    final int size = grid.getStore().getModels().size();
                    if (insertIndex > size) {
                        insertIndex = size;
                    }
                    grid.getStore().insert((ModelData) data, insertIndex);
                } else if (data instanceof List) {
                    for (final ModelData item : (List<BaseModelData>)data) {
                        final int size = grid.getStore().getModels().size();
                        if (insertIndex > size) {
                            insertIndex = size;
                        }
                        grid.getStore().insert(item, insertIndex);
                    }
                }
            }
        };
        _target.setFeedback(Feedback.INSERT);
        _target.setAllowSelfAsSource(true);
    }

    private void setCurrentValue(final String currentValue) {
        for (final ModelData md : _sortStore.getModels()) {
            if(md.get("value").equals(currentValue)) {
                _sortOrder.setValue(md);
                return;
            }
        }
        throw new RuntimeException("Invalid sort order: "+currentValue);
    }


    private void populateSortOptions() {
        final ModelData nameAlphanumAsc = new BaseModelData();
        nameAlphanumAsc.set("name", "Name - alphanumeric, ascending"); // TODO: I18n
        nameAlphanumAsc.set("value", "NAME_ALPHANUM_ASC");
        _sortStore.add(nameAlphanumAsc);
        final ModelData manual = new BaseModelData();
        manual.set("name", "Manual"); // TODO: I18n
        manual.set("value", "MANUAL");
        _sortStore.add(manual);
    }

    private void configureComboBox(final String value,
                                   final String id,
                                   final boolean allowBlank,
                                   final String label) {
        _sortOrder.setFieldLabel(label);
        _sortOrder.setAllowBlank(allowBlank);
        _sortOrder.setId(id);
        _sortOrder.setDisplayField("name");
        _sortOrder.setTemplate("<tpl for=\".\">"
            +"<div class=x-combo-list-item id=\"{name}\">{name}</div></tpl>");
        _sortOrder.setEditable(false);
        _sortOrder.setStore(_sortStore);
        addField(_sortOrder);
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final ModelData md = _selectionModel.tableSelection();
                final String order = _sortOrder.getValue().<String>get("value");
                _commands.updateFolderSortOrder(
                    md.<String>get("id"),
                    order,
                    new ErrorReportingCallback<Void>(){
                        public void onSuccess(final Void result) {
                            // TODO cleanup
                            if (order.equals("MANUAL")) {
                                final List<String> orderList = new ArrayList<String>();
                                final List<ModelData> models = _grid.getStore().getModels();
                                for(final ModelData m : models) {
                                    orderList.add(m.<String>get("id"));
                                }
                                _commands.reorder(md.<String>get("id"),
                                    orderList,
                                    new ErrorReportingCallback<Void>(){
                                    public void onSuccess(final Void result) {
                                        close();
                                        md.set("sortOrder", order);
                                    }
                                });
                            } else {
                                close();
                                md.set("sortOrder", order);
                            }
                        }
                    }
                );
            }
        };
    }

    private void createColumnConfigs(final List<ColumnConfig> configs) {
        final ColumnConfig typeColumn =
            new ColumnConfig("type", _constants.type(), 70);
        typeColumn.setSortable(false);
        typeColumn.setMenuDisabled(true);
        configs.add(typeColumn);

        final ColumnConfig nameColumn =
            new ColumnConfig("name", _constants.name(), 250);
        nameColumn.setSortable(false);
        nameColumn.setMenuDisabled(true);
        configs.add(nameColumn);

        final ColumnConfig titleColumn =
            new ColumnConfig("title", _constants.title(), 250);
        titleColumn.setSortable(false);
        titleColumn.setMenuDisabled(true);
        configs.add(titleColumn);
    }
}
