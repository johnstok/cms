/*-----------------------------------------------------------------------------
 * Copyright © 2011 Civic Computing Ltd.
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

import java.util.List;
import java.util.UUID;

import ccc.api.core.ResourceSummary;
import ccc.api.types.CommandType;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourcePath;
import ccc.client.core.SingleSelectionModel;
import ccc.client.events.Event;
import ccc.client.events.EventHandler;
import ccc.client.gwt.binding.DataBinding;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.grid.GridViewConfig;
import com.google.gwt.i18n.client.DateTimeFormat;


/**
 * Abstract class for resource tables.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractResourceTable extends
TablePanel
implements
    EventHandler<CommandType>,
    SingleSelectionModel,
    ColumnConfigSupport {

    protected Grid<BeanModel> _grid;
    protected ResourceTree _tree;
    protected ListStore<BeanModel> _detailsStore =
        new ListStore<BeanModel>();

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
                ResourceSummary.Properties.TYPE,
                UI_CONSTANTS.type(),
                40);
        typeColumn.setRenderer(rsRenderer);
        configs.add(typeColumn);

        final ColumnConfig workingCopyColumn =
            new ColumnConfig(
                ResourceSummary.Properties.WORKING_COPY,
                UI_CONSTANTS.draft(),
                40);
        workingCopyColumn.setSortable(false);
        workingCopyColumn.setMenuDisabled(true);
        workingCopyColumn.setRenderer(rsRenderer);
        configs.add(workingCopyColumn);

        final ColumnConfig mmIncludeColumn =
            new ColumnConfig(
                ResourceSummary.Properties.MM_INCLUDE,
                UI_CONSTANTS.menu(),
                40);
        mmIncludeColumn.setRenderer(rsRenderer);
        configs.add(mmIncludeColumn);

        final ColumnConfig createdColumn =
            new ColumnConfig(
                ResourceSummary.Properties.DATE_CREATED,
                UI_CONSTANTS.dateCreated(),
                100);
        createdColumn.setDateTimeFormat(
            DateTimeFormat.getShortDateTimeFormat());
        createdColumn.setHidden(true);
        configs.add(createdColumn);

        final ColumnConfig updatedColumn =
            new ColumnConfig(
                ResourceSummary.Properties.DATE_CHANGED,
                UI_CONSTANTS.dateChanged(),
                100);
        updatedColumn.setDateTimeFormat(
            DateTimeFormat.getShortDateTimeFormat());
        updatedColumn.setHidden(true);
        configs.add(updatedColumn);

        final ColumnConfig visibleColumn =
            new ColumnConfig(
                ResourceSummary.Properties.VISIBLE,
                UI_CONSTANTS.visible(),
                45);
        visibleColumn.setRenderer(rsRenderer);
        visibleColumn.setHidden(true);
        configs.add(visibleColumn);

        final ColumnConfig lockedColumn =
            new ColumnConfig(
                ResourceSummary.Properties.LOCKED,
                UI_CONSTANTS.lockedBy(),
                80);
        configs.add(lockedColumn);

        final ColumnConfig publishedByColumn =
            new ColumnConfig(
                ResourceSummary.Properties.PUBLISHED,
                UI_CONSTANTS.publishedBy(),
                80);
        configs.add(publishedByColumn);

        final ColumnConfig nameColumn =
            new ColumnConfig(
                ResourceSummary.Properties.NAME,
                UI_CONSTANTS.name(),
                250);
        configs.add(nameColumn);

        final ColumnConfig titleColumn =
            new ColumnConfig(
                ResourceSummary.Properties.TITLE,
                UI_CONSTANTS.title(),
                250);
        configs.add(titleColumn);

        final ColumnConfig changedByColumn =
            new ColumnConfig(
            ResourceSummary.Properties.CHANGED_BY,
            UI_CONSTANTS.changedBy(),
            80);
            changedByColumn.setHidden(true);
            configs.add(changedByColumn);
    }


    protected void setUpGrid() {
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
            ResourceSummary.Properties.TITLE);
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
                        ResourceSummary.Properties.UUID, event.getProperty("id"));
                final ResourceSummary md1 = bm1.<ResourceSummary>getBean();
                md1.setAbsolutePath(
                    event.<ResourcePath>getProperty("path").toString());
                md1.setName(
                    event.<ResourceName>getProperty("name"));
                update(md1);
                break;

            case RESOURCE_CHANGE_TEMPLATE:
                final BeanModel bm2 =
                    _detailsStore.findModel(
                        ResourceSummary.Properties.UUID, event.getProperty("resource"));
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
            _detailsStore.findModel(ResourceSummary.Properties.UUID, rs.getId());
        if (null!=tBean) {
            tBean.setProperties(
                DataBinding.bindResourceSummary(rs).getProperties());
            update(tBean.<ResourceSummary>getBean());
        }
    }


    private void removeResource(final UUID id) {
        final BeanModel tBean =
            _detailsStore.findModel(ResourceSummary.Properties.UUID, id);
        if (null!=tBean) {
            _detailsStore.remove(tBean);
        }
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
    public void move(final ResourceSummary model,
                     final ResourceSummary newParent,
                     final ResourceSummary oldParent) {
        removeResource(model.getId());
        _tree.move(oldParent, newParent, model);
    }


    /** {@inheritDoc} */
    public void update(final ResourceSummary model) {
        updateResource(model.getId());
        _tree.updateResource(model);
    }


    private void updateResource(final UUID id) {
        final BeanModel tBean =
            _detailsStore.findModel(ResourceSummary.Properties.UUID, id);
        if (null!=tBean) {
            _detailsStore.update(tBean);
        }
    }


    /** {@inheritDoc} */
    public ResourceSummary tableSelection() {
        if (_grid.getSelectionModel() == null) {
            return null;
        }
        final BeanModel selected = _grid.getSelectionModel().getSelectedItem();
        return (null==selected) ? null : selected.<ResourceSummary>getBean();
    }


    public String visibleColumns() {
        final StringBuilder names = new StringBuilder();
        final List<ColumnConfig> columns =
            _grid.getColumnModel().getColumns();
        for (final ColumnConfig c : columns) {
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
}
