/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.client.gwt.views.gxt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import ccc.api.core.Folder;
import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.types.ResourceType;
import ccc.api.types.SortOrder;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.binding.ResourceSummaryModelData.Property;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.Globals;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.I18n;
import ccc.client.gwt.core.Response;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.client.gwt.remoting.GetChildrenAction;
import ccc.client.gwt.remoting.UpdateFolderAction;
import ccc.client.gwt.widgets.ResourceTypeRendererFactory;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
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

    private final ComboBox<ModelData> _indexPage = new ComboBox<ModelData>();

    private final SingleSelectionModel _selectionModel;

    private final Grid<ResourceSummaryModelData> _grid;
    private final ColumnModel _cm;
    private ListStore<ResourceSummaryModelData> _detailsStore =
        new ListStore<ResourceSummaryModelData>();
    private ListStore<ModelData> _indexPageStore = new ListStore<ModelData>();

    private GridDropTarget _target;
    private ModelData _none = new BaseModelData();

    private final UUID _currentIndexPage;
    private final ResourceSummaryModelData _folder;


    /**
     * Constructor.
     *
     * @param ssm The selection model.
     * @param folder The current folder.
     */
    public EditFolderDialog(final SingleSelectionModel ssm,
                            final ResourceSummaryModelData folder) {
        super(I18n.UI_CONSTANTS.edit(), new GlobalsImpl());

        _currentIndexPage = folder.getIndexPageId();
        _folder = folder;

        setHeight(Globals.DEFAULT_HEIGHT);
        _selectionModel = ssm;

        configureComboBox(_indexPage,
            _indexPageStore,
            "folder-index-page",
            constants().indexPage());

        final Text fieldName = new Text(getUiConstants().manualOrder()+":");
        fieldName.setStyleName("x-form-item");
        addField(fieldName);

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        createColumnConfigs(configs);
        _cm = new ColumnModel(configs);
        _grid = new Grid<ResourceSummaryModelData>(_detailsStore, _cm);
        _grid.setHeight(GRID_HEIGHT);
        _grid.setWidth(GRID_WIDTH);
        _grid.setBorders(true);
        addField(_grid);

        new GridDragSource(_grid);

        loadDetailStore(_currentIndexPage);

        configureDropTarget();

        addListener(Events.Resize,
            new Listener<BoxComponentEvent>() {
            @Override
            public void handleEvent(final BoxComponentEvent be) {
                final int newHeight =
                    be.getHeight() - (Globals.DEFAULT_HEIGHT - GRID_HEIGHT);
                if (newHeight > (Globals.DEFAULT_HEIGHT - GRID_HEIGHT)) {
                    _grid.setHeight(newHeight);
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

        new GetChildrenAction(getUiConstants().edit()) {

            @Override
            protected String getPath() {
                final HashMap<String, String[]> params =
                    new HashMap<String, String[]>();
                params.put("parent",
                    new String[] {selection.getId().toString()});
                params.put("sort", new String[] {"manual"});
                params.put("order", new String[] {SortOrder.ASC.name()});
                params.put("page", new String[] {"1"});
                params.put("count", new String[] {"1000"});
                return selection.getDelegate().list().build(
                    params, new GWTTemplateEncoder());
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


    private void setCurrentIndexPage(final UUID currentValue) {
        for (final ModelData md : _indexPageStore.getModels()) {
            if(currentValue != null && currentValue.equals(md.get("value"))) {
                _indexPage.setValue(md);
                return;
            }
        }
        _indexPage.setValue(_none);
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
        cb.setTriggerAction(TriggerAction.ALL);
        addField(cb);
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final ResourceSummaryModelData md =
                    _selectionModel.tableSelection();

                final UUID indexPageId =
                    _indexPage.getValue().<UUID>get("value");

                final List<String> orderList = new ArrayList<String>();
                    final List<ResourceSummaryModelData> models =
                        _grid.getStore().getModels();
                    for(final ResourceSummaryModelData m : models) {
                        orderList.add(m.getId().toString());
                    }

                final Folder f = new Folder();
                f.setId(md.getId());
                f.setIndexPage(indexPageId);
                f.setSortList(orderList);
                f.addLink(
                    Resource.SELF,
                    _folder.getDelegate().getLink(Resource.SELF));

                new UpdateFolderAction(f) {
                    /** {@inheritDoc} */
                    @Override protected void onNoContent(final Response r) {
                        md.setIndexPageId(indexPageId);
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

}
