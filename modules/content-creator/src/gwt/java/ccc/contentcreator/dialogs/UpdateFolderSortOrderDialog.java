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

import ccc.api.ID;
import ccc.api.ResourceSummary;
import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;

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
import com.google.gwt.i18n.client.DateTimeFormat;
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

    private static final String MANUAL = "MANUAL";
    private static final String DATE_CREATED_DESC = "DATE_CREATED_DESC";
    private static final String DATE_CREATED_ASC = "DATE_CREATED_ASC";
    private static final String DATE_CHANGED_DESC = "DATE_CHANGED_DESC";
    private static final String DATE_CHANGED_ASC = "DATE_CHANGED_ASC";
    private static final String NAME_ALPHANUM_ASC = "NAME_ALPHANUM_ASC";

    private final Grid<ResourceSummaryModelData> _grid;
    private final ColumnModel _cm;
    private ListStore<ResourceSummaryModelData> _detailsStore =
        new ListStore<ResourceSummaryModelData>();

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
        _grid = new Grid<ResourceSummaryModelData>(_detailsStore, _cm);
        _grid.setHeight(300);
        _grid.setWidth(610);
        _grid.setBorders(true);
        addField(_grid);

        new GridDragSource(_grid);
        configureDropTarget();

        _sortOrder.addSelectionChangedListener(
            new SelectionChangedListener<ModelData>() {
                @Override
                public void selectionChanged(final SelectionChangedEvent<ModelData> se) {
                    final ModelData md = se.getSelectedItem();
                    if (md != null) {
                        loadDetailStore();
                        final String order = md.<String>get("value");
                        if (MANUAL.equals(order)) {
                            _grid.enable();
                        } else  {
                            _grid.disable();
                            if (NAME_ALPHANUM_ASC.equals(order)) {
                                _detailsStore.sort("name", SortDir.ASC);
                            } else if (DATE_CHANGED_ASC.equals(order)) {
                                _detailsStore.sort("dateChanged", SortDir.ASC);
                            } else if (DATE_CHANGED_DESC.equals(order)) {
                                _detailsStore.sort("dateChanged", SortDir.DESC);
                            } else if (DATE_CREATED_ASC.equals(order)) {
                                _detailsStore.sort("dateCreated", SortDir.ASC);
                            } else if (DATE_CREATED_DESC.equals(order)) {
                                _detailsStore.sort("dateCreated", SortDir.DESC);
                            }
                        }
                        configureDropTarget();
                    }
                }
            });
    }

    private void loadDetailStore() {
        _detailsStore =  new ListStore<ResourceSummaryModelData>();
        final ID id = _selectionModel.tableSelection().getId();
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
                    final int size = grid.getStore().getModels().size();
                    if (insertIndex > size) {
                        insertIndex = size;
                    }
                    grid.getStore().insert((ModelData) data, insertIndex);
                } else if (data instanceof List) {
                    for (final ModelData item : (List<BaseModelData>) data) {
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
        final ModelData manual = new BaseModelData();
        manual.set("name", _constants.manual());
        manual.set("value", MANUAL);
        _sortStore.add(manual);
        final ModelData nameAlphanumAsc = new BaseModelData();
        nameAlphanumAsc.set("name",
            _constants.name()
            +" - "
            +_constants.alphanumeric()
            +", "
            +_constants.ascending());
        nameAlphanumAsc.set("value", NAME_ALPHANUM_ASC);
        _sortStore.add(nameAlphanumAsc);

        final ModelData dateChangedAsc = new BaseModelData();
        dateChangedAsc.set(
            "name", _constants.dateChanged()+", "+_constants.ascending());
        dateChangedAsc.set("value", DATE_CHANGED_ASC);
        _sortStore.add(dateChangedAsc);
        final ModelData dateChangedDesc = new BaseModelData();
        dateChangedDesc.set(
            "name", _constants.dateChanged()+", "+_constants.descending());
        dateChangedDesc.set("value", DATE_CHANGED_DESC);
        _sortStore.add(dateChangedDesc);

        final ModelData dateCreatedAsc = new BaseModelData();
        dateCreatedAsc.set(
            "name", _constants.dateCreated()+", "+_constants.ascending());
        dateCreatedAsc.set("value", DATE_CREATED_ASC);
        _sortStore.add(dateCreatedAsc);
        final ModelData dateCreatedDesc = new BaseModelData();
        dateCreatedDesc.set(
            "name", _constants.dateCreated()+", "+_constants.descending());
        dateCreatedDesc.set("value", DATE_CREATED_DESC);
        _sortStore.add(dateCreatedDesc);
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
                final ResourceSummaryModelData md = _selectionModel.tableSelection();
                final String order = _sortOrder.getValue().<String>get("value");
                _commands.updateFolderSortOrder(
                    md.getId(),
                    order,
                    new ErrorReportingCallback<Void>(){
                        public void onSuccess(final Void result) {
                            // TODO cleanup
                            if (order.equals(MANUAL)) {
                                final List<String> orderList = new ArrayList<String>();
                                final List<ResourceSummaryModelData> models = _grid.getStore().getModels();
                                for(final ResourceSummaryModelData m : models) {
                                    orderList.add(m.getId().toString());
                                }
                                _commands.reorder(md.getId(),
                                    orderList,
                                    new ErrorReportingCallback<Void>(){
                                    public void onSuccess(final Void result) {
                                        close();
                                        md.setSortOrder(order);
                                    }
                                });
                            } else {
                                close();
                                md.setSortOrder(order);
                            }
                        }
                    }
                );
            }
        };
    }

    private void createColumnConfigs(final List<ColumnConfig> configs) {
        final DateTimeFormat dateTimeFormat =
            DateTimeFormat.getFormat("dd.MM.yyyy");

        final ColumnConfig typeColumn =
            new ColumnConfig("type", _constants.type(), 70);
        typeColumn.setSortable(false);
        typeColumn.setMenuDisabled(true);
        configs.add(typeColumn);

        final ColumnConfig nameColumn =
            new ColumnConfig("name", _constants.name(), 170);
        nameColumn.setSortable(false);
        nameColumn.setMenuDisabled(true);
        configs.add(nameColumn);

        final ColumnConfig titleColumn =
            new ColumnConfig("title", _constants.title(), 170);
        titleColumn.setSortable(false);
        titleColumn.setMenuDisabled(true);
        configs.add(titleColumn);

        final ColumnConfig createdColumn =
            new ColumnConfig("dateCreated", _constants.created(), 70);
        createdColumn.setSortable(false);
        createdColumn.setMenuDisabled(true);
        createdColumn.setDateTimeFormat(dateTimeFormat);
        configs.add(createdColumn);

        final ColumnConfig changedColumn =
            new ColumnConfig("dateChanged", _constants.changed(), 70);
        changedColumn.setSortable(false);
        changedColumn.setMenuDisabled(true);
        changedColumn.setDateTimeFormat(dateTimeFormat);
        configs.add(changedColumn);

    }
}
