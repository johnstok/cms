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

import static ccc.types.ResourceOrder.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import ccc.contentcreator.actions.GetChildrenAction;
import ccc.contentcreator.actions.UpdateFolderAction;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.binding.ResourceSummaryModelData.Property;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.ResourceTypeRendererFactory;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.rest.dto.ResourceSummary;
import ccc.types.ResourceType;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.DateTimeFormat;


/**
 * Folder edit dialog.
 *
 * @author Civic Computing Ltd.
 */
public class EditFolderDialog
extends
AbstractEditDialog {

    private static final int GRID_WIDTH = 610;
    private static final int GRID_HEIGHT = 320;

    private final ComboBox<ModelData> _sortOrder = new ComboBox<ModelData>();
    private final ComboBox<ModelData> _indexPage = new ComboBox<ModelData>();

    private final SingleSelectionModel _selectionModel;
    private final ListStore<ModelData> _sortStore = new ListStore<ModelData>();


    private final Grid<ResourceSummaryModelData> _grid;
    private final ColumnModel _cm;
    private ListStore<ResourceSummaryModelData> _detailsStore =
        new ListStore<ResourceSummaryModelData>();
    private ListStore<ModelData> _indexPageStore = new ListStore<ModelData>();

    private GridDropTarget _target;
    private ModelData _none = new BaseModelData();

    private final UUID _currentIndexPage;


    /**
     * Constructor.
     *
     * @param ssm The selection model.
     * @param currentSortOrder The current sort order.
     * @param currentIndexPage The current index page.
     */
    public EditFolderDialog(final SingleSelectionModel ssm,
                                       final String currentSortOrder,
                                       final UUID currentIndexPage) {
        super(new IGlobalsImpl().uiConstants().edit(), new IGlobalsImpl());

        _currentIndexPage = currentIndexPage;
        setHeight(IGlobals.DEFAULT_HEIGHT);
        _selectionModel = ssm;
        loadDetailStore(_currentIndexPage);

        configureComboBox(_indexPage,
            _indexPageStore,
            "folder-index-page",
            constants().indexPage());

        populateSortOptions();
        configureComboBox(_sortOrder,
                          _sortStore,
                          "folder-sort-order",
                          constants().folderSortOrder());

        setCurrentSortValue(currentSortOrder);

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        createColumnConfigs(configs);
        _cm = new ColumnModel(configs);
        _grid = new Grid<ResourceSummaryModelData>(_detailsStore, _cm);
        _grid.setHeight(GRID_HEIGHT);
        _grid.setWidth(GRID_WIDTH);
        _grid.setBorders(true);
        addField(_grid);

        new GridDragSource(_grid);
        configureDropTarget();

        _sortOrder.addSelectionChangedListener(new SortChangeListener());

        addListener(Events.Resize,
            new Listener<BoxComponentEvent>() {
            @Override
            public void handleEvent(final BoxComponentEvent be) {
                final int height =
                    be.getHeight() - (IGlobals.DEFAULT_HEIGHT - GRID_HEIGHT);
                if (height > (IGlobals.DEFAULT_HEIGHT - GRID_HEIGHT)) {
                    _grid.setHeight(height);
                }
            }
        });
    }

    private void populateIndexOptions(final Collection<ResourceSummary> arg0) {
        _indexPageStore.removeAll();
        final List<ModelData> pagesOnly = new ArrayList<ModelData>();

        _none.set("name", getUiConstants().none());
        _none.set("value", null);
        _indexPageStore.add(_none);

        for (final ResourceSummary item : arg0) {
            if (item.getType() == ResourceType.PAGE) {
                final ModelData pageModel = new BaseModelData();
                pageModel.set("name", item.getName());
                pageModel.set("value", item.getId());
                pagesOnly.add(pageModel);
            }
        }
        _indexPageStore.add(pagesOnly);
    }

    private void loadDetailStore(final UUID currentIndexPage) {
        _detailsStore =  new ListStore<ResourceSummaryModelData>();
        final ResourceSummaryModelData selection =
            _selectionModel.tableSelection();

        new GetChildrenAction(getUiConstants().edit(), selection.getId()) {

            @Override
            protected String getPath() {
                return "/folders/"+selection.getId()+"/children-manual-order";
            }

            /** {@inheritDoc} */
            @Override protected void onFailure(final Throwable t) {
                _grid.disable();
            }

            /** {@inheritDoc} */
            @Override
            protected void execute(final Collection<ResourceSummary> children) {
                _detailsStore.removeAll();
                populateIndexOptions(children);
                setCurrentIndexPage(currentIndexPage);
                _detailsStore.add(DataBinding.bindResourceSummary(children));
                _grid.reconfigure(_detailsStore, _cm);
            }
        }.execute();
    }

    private void configureDropTarget() {

        _target = new GridDropTarget(_grid);
        _target.setFeedback(Feedback.INSERT);
        _target.setAllowSelfAsSource(true);
    }

    private void setCurrentSortValue(final String currentValue) {
        for (final ModelData md : _sortStore.getModels()) {
            if(md.get("value").equals(currentValue)) {
                _sortOrder.setValue(md);
                return;
            }
        }
        throw new RuntimeException("Invalid sort order: "+currentValue);
    }

    private void setCurrentIndexPage(final UUID currentValue) {
        for (final ModelData md : _indexPageStore.getModels()) {
            if(currentValue != null && currentValue.equals(md.get("value"))) {
                _indexPage.setValue(md);
                return;
            }
        }
        _indexPage.setValue(_none);
    }


    private void populateSortOptions() {
        final ModelData manual = new BaseModelData();
        manual.set("name", getUiConstants().manual());
        manual.set("value", MANUAL.name());
        _sortStore.add(manual);
        final ModelData nameAlphanumAsc = new BaseModelData();
        nameAlphanumAsc.set("name",
            getUiConstants().name()
            +" - "
            +getUiConstants().alphanumeric()
            +", "
            +getUiConstants().ascending());
        nameAlphanumAsc.set("value", NAME_ALPHANUM_ASC.name());
        _sortStore.add(nameAlphanumAsc);

        final ModelData dateChangedAsc = new BaseModelData();
        dateChangedAsc.set(
            "name",
            getUiConstants().dateChanged()+", "+getUiConstants().ascending());
        dateChangedAsc.set("value", DATE_CHANGED_ASC.name());
        _sortStore.add(dateChangedAsc);
        final ModelData dateChangedDesc = new BaseModelData();
        dateChangedDesc.set(
            "name",
            getUiConstants().dateChanged()+", "+getUiConstants().descending());
        dateChangedDesc.set("value", DATE_CHANGED_DESC.name());
        _sortStore.add(dateChangedDesc);

        final ModelData dateCreatedAsc = new BaseModelData();
        dateCreatedAsc.set(
            "name",
            getUiConstants().dateCreated()+", "+getUiConstants().ascending());
        dateCreatedAsc.set("value", DATE_CREATED_ASC.name());
        _sortStore.add(dateCreatedAsc);
        final ModelData dateCreatedDesc = new BaseModelData();
        dateCreatedDesc.set(
            "name",
            getUiConstants().dateCreated()+", "+getUiConstants().descending());
        dateCreatedDesc.set("value", DATE_CREATED_DESC.name());
        _sortStore.add(dateCreatedDesc);
    }

    private void configureComboBox(final ComboBox<ModelData> cb,
                                   final ListStore<ModelData> store,
                                   final String id,
                                   final String label) {
        cb.setFieldLabel(label);
        cb.setAllowBlank(false);
        cb.setId(id);
        cb.setDisplayField("name");
        cb.setTemplate("<tpl for=\".\">"
            +"<div class=x-combo-list-item id=\"{name}\">{name}</div></tpl>");
        cb.setEditable(false);
        cb.setStore(store);
        addField(cb);
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final ResourceSummaryModelData md =
                    _selectionModel.tableSelection();
                final String order = _sortOrder.getValue().<String>get("value");
                final UUID indexPageId =
                    _indexPage.getValue().<UUID>get("value");
                final List<String> orderList = new ArrayList<String>();

                if (order.equals(MANUAL.name())) {
                    final List<ResourceSummaryModelData> models =
                        _grid.getStore().getModels();
                    for(final ResourceSummaryModelData m : models) {
                        orderList.add(m.getId().toString());
                    }
                }

                new UpdateFolderAction(
                    md.getId(),
                    order,
                    indexPageId,
                    orderList) {
                    /** {@inheritDoc} */
                    @Override protected void onNoContent(final Response r) {
                        md.setIndexPageId(indexPageId);
                        md.setSortOrder(order);
                        hide();
                    }
                }.execute();
            }
        };
    }


    private void createColumnConfigs(final List<ColumnConfig> configs) {
        final DateTimeFormat dateTimeFormat =
            DateTimeFormat.getFormat("dd.MM.yyyy");

        final ColumnConfig typeColumn = new ColumnConfig(
            Property.TYPE.name(),
            getUiConstants().type(),
            40);
        typeColumn.setSortable(false);
        typeColumn.setMenuDisabled(true);
        typeColumn.setRenderer(
            ResourceTypeRendererFactory.rendererForResourceSummary());
        configs.add(typeColumn);

        final ColumnConfig nameColumn = new ColumnConfig(
            Property.NAME.name(),
            getUiConstants().name(),
            180);
        nameColumn.setSortable(false);
        nameColumn.setMenuDisabled(true);
        configs.add(nameColumn);

        final ColumnConfig titleColumn = new ColumnConfig(
            Property.TITLE.name(),
            getUiConstants().title(),
            180);
        titleColumn.setSortable(false);
        titleColumn.setMenuDisabled(true);
        configs.add(titleColumn);

        final ColumnConfig createdColumn = new ColumnConfig(
            Property.DATE_CREATED.name(),
            getUiConstants().created(),
            75);
        createdColumn.setSortable(false);
        createdColumn.setMenuDisabled(true);
        createdColumn.setDateTimeFormat(dateTimeFormat);
        configs.add(createdColumn);

        final ColumnConfig changedColumn = new ColumnConfig(
            Property.DATE_CHANGED.name(),
            getUiConstants().changed(),
            75);
        changedColumn.setSortable(false);
        changedColumn.setMenuDisabled(true);
        changedColumn.setDateTimeFormat(dateTimeFormat);
        configs.add(changedColumn);

    }

    /**
     * Listener for sort order changes.
     *
     * @author Civic Computing Ltd.
     */
    private final class SortChangeListener
        extends SelectionChangedListener<ModelData> {

        @Override
        public void selectionChanged(
                                 final SelectionChangedEvent<ModelData> se) {
            final ModelData md = se.getSelectedItem();
            if (md != null) {
                // FIXME: We should be using a switch statement here.
                final String order = md.<String>get("value");
                if (MANUAL.name().equals(order)) {
                    _grid.enable();
                    // reload original manual order - otherwise DnD fails.
                    loadDetailStore(_currentIndexPage);
                } else  {
                    _grid.disable();
                    if (NAME_ALPHANUM_ASC.name().equals(order)) {
                        _detailsStore.sort(
                            Property.NAME.name(), SortDir.ASC);
                    } else if (DATE_CHANGED_ASC.name().equals(order)) {
                        _detailsStore.sort(
                            Property.DATE_CHANGED.name(), SortDir.ASC);
                    } else if (DATE_CHANGED_DESC.name().equals(order)) {
                        _detailsStore.sort(
                            Property.DATE_CHANGED.name(), SortDir.DESC);
                    } else if (DATE_CREATED_ASC.name().equals(order)) {
                        _detailsStore.sort(
                            Property.DATE_CREATED.name(), SortDir.ASC);
                    } else if (DATE_CREATED_DESC.name().equals(order)) {
                        _detailsStore.sort(
                            Property.DATE_CREATED.name(), SortDir.DESC);
                    }
                }
                configureDropTarget();
            }
        }
    }
}
