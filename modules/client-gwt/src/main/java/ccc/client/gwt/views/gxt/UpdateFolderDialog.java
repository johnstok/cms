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
import ccc.api.core.ResourceSummary;
import ccc.api.types.ResourceType;
import ccc.api.types.SortOrder;
import ccc.client.actions.GetChildrenAction;
import ccc.client.core.Editable;
import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.ValidationResult;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.widgets.ResourceTypeRendererFactory;
import ccc.client.views.UpdateFolder;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BeanModel;
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
public class UpdateFolderDialog
extends
AbstractEditDialog
implements UpdateFolder {

    private static final int GRID_WIDTH = 610;
    private static final int GRID_HEIGHT = 320;
    private Editable _presenter;

    private final ComboBox<BaseModelData> _indexPage =
        new ComboBox<BaseModelData>();

    private final Grid<BeanModel> _grid;
    private final ColumnModel _cm;
    private ListStore<BeanModel> _detailsStore =
        new ListStore<BeanModel>();
    private final ListStore<BaseModelData> _indexPageStore =
        new ListStore<BaseModelData>();

    private final BaseModelData _none = new BaseModelData();
    private Folder _folder = null;


    /**
     * Constructor.
     *
     */
    public UpdateFolderDialog() {
        super(I18n.uiConstants.edit(), InternalServices.globals);

        setHeight(Globals.DEFAULT_HEIGHT);

        configureComboBox(
            _indexPage,
            _indexPageStore,
            "folder-index-page",
            constants().indexPage());

        final Text fieldName = new Text(getUiConstants().manualOrder()+":");
        fieldName.setStyleName("x-form-item");
        addField(fieldName);

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        createColumnConfigs(configs);
        _cm = new ColumnModel(configs);
        _grid = new Grid<BeanModel>(_detailsStore, _cm);
        _grid.setHeight(GRID_HEIGHT);
        _grid.setWidth(GRID_WIDTH);
        _grid.setBorders(true);
        addField(_grid);

        new GridDragSource(_grid);

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
        final List<BaseModelData> pagesAndAliases =
            new ArrayList<BaseModelData>();

        _none.set("name", getUiConstants().none());
        _none.set("value", null);
        _indexPageStore.add(_none);

        for (final ResourceSummary item : arg0) {
            if (item.getType() == ResourceType.PAGE
                || item.getType() == ResourceType.ALIAS) {
                final BaseModelData pageModel = new BaseModelData();
                pageModel.set("name", item.getName());
                pageModel.set("value", item.getId());
                pagesAndAliases.add(pageModel);
            }
        }
        _indexPageStore.add(pagesAndAliases);
    }

    private void loadDetailStore(final UUID currentIndexPage) {
        _detailsStore =  new ListStore<BeanModel>();

        new GetChildrenAction(getUiConstants().edit()) {

            @Override
            protected String getPath() {
                final HashMap<String, String[]> params =
                    new HashMap<String, String[]>();
                    params.put("parent",
                        new String[] {_folder.getId().toString()});
                    params.put("sort", new String[] {"manual"});
                    params.put("order", new String[] {SortOrder.ASC.name()});
                    params.put("page", new String[] {"1"});
                    params.put("count", new String[] {"1000"});
                    return _folder.list().build(
                        params, InternalServices.encoder);
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
                setIndexPage(currentIndexPage);
                _detailsStore.add(DataBinding.bindResourceSummary(children));
                _grid.reconfigure(_detailsStore, _cm);
            }
        }.execute();
    }

    private void configureDropTarget() {

        final GridDropTarget target = new GridDropTarget(_grid);
        target.setFeedback(Feedback.INSERT);
        target.setAllowSelfAsSource(true);
    }


    private void configureComboBox(final ComboBox<BaseModelData> cb,
                                   final ListStore<BaseModelData> store,
                                   final String id,
                                   final String label) {
        cb.setFieldLabel(label);
        cb.setAllowBlank(false);
        cb.setDisplayField(ResourceSummary.Properties.NAME);
        cb.setTemplate(
            "<tpl for=\".\">"
            +"<div class=x-combo-list-item id=\"{"
            + ResourceSummary.Properties.NAME
            +"}\">{"
            + ResourceSummary.Properties.NAME
            + "}</div></tpl>");
        cb.setEditable(false);
        cb.setStore(store);
        cb.setTriggerAction(TriggerAction.ALL);
        addField(cb);
    }

    private void createColumnConfigs(final List<ColumnConfig> configs) {
        final DateTimeFormat dateTimeFormat =
            DateTimeFormat.getFormat("dd.MM.yyyy");

        final ColumnConfig typeColumn = new ColumnConfig(
            ResourceSummary.Properties.TYPE,
            getUiConstants().type(),
            40);
        typeColumn.setSortable(false);
        typeColumn.setMenuDisabled(true);
        typeColumn.setRenderer(
            ResourceTypeRendererFactory.rendererForResourceSummary());
        configs.add(typeColumn);

        final ColumnConfig nameColumn = new ColumnConfig(
            ResourceSummary.Properties.NAME,
            getUiConstants().name(),
            180);
        nameColumn.setSortable(false);
        nameColumn.setMenuDisabled(true);
        configs.add(nameColumn);

        final ColumnConfig titleColumn = new ColumnConfig(
            ResourceSummary.Properties.TITLE,
            getUiConstants().title(),
            180);
        titleColumn.setSortable(false);
        titleColumn.setMenuDisabled(true);
        configs.add(titleColumn);

        final ColumnConfig createdColumn = new ColumnConfig(
            ResourceSummary.Properties.DATE_CREATED,
            getUiConstants().created(),
            75);
        createdColumn.setSortable(false);
        createdColumn.setMenuDisabled(true);
        createdColumn.setDateTimeFormat(dateTimeFormat);
        configs.add(createdColumn);

        final ColumnConfig changedColumn = new ColumnConfig(
            ResourceSummary.Properties.DATE_CHANGED,
            getUiConstants().changed(),
            75);
        changedColumn.setSortable(false);
        changedColumn.setMenuDisabled(true);
        changedColumn.setDateTimeFormat(dateTimeFormat);
        configs.add(changedColumn);
    }

    @Override
    public UUID getIndexPage() {
        return _indexPage.getValue().<UUID>get("value");
    }

    @Override
    public void setIndexPage(final UUID id) {
        for (final BaseModelData md : _indexPageStore.getModels()) {
            if(id != null && id.equals(md.get("value"))) {
                _indexPage.setValue(md);
                return;
            }
        }
        _indexPage.setValue(_none);
    }

    @Override
    public void show(final Editable presenter) {
        _presenter = presenter;
        loadDetailStore(getFolder().getIndex());
        super.show();
    }

    /**
     * Accessor.
     *
     * @return Returns the presenter.
     */
    Editable getPresenter() {
        return _presenter;
    }


    @Override
    public ValidationResult getValidationResult() {
        final ValidationResult result = new ValidationResult();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                getPresenter().save();
            }
        };
    }

    @Override
    public List<String> getOrderList() {
        final ArrayList<String> orderList = new ArrayList<String>();
        final List<BeanModel> models =
            _grid.getStore().getModels();
        for(final BeanModel m : models) {
            orderList.add(
                m.<ResourceSummary>getBean().getId().toString());
        }
        return orderList;
    }

    @Override
    public Folder getFolder() {
        return _folder;
    }

    @Override
    public void setFolder(final Folder folder) {
        _folder = folder;
    }
}
