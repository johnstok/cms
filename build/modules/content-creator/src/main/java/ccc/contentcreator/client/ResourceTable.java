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

import java.util.ArrayList;
import java.util.List;

import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.dialogs.ChooseTemplateDialog;
import ccc.contentcreator.client.dialogs.CreateAliasDialog;
import ccc.contentcreator.client.dialogs.EditTemplateDialog;
import ccc.contentcreator.client.dialogs.PreviewContentDialog;
import ccc.contentcreator.client.dialogs.UpdatePageDialog;
import ccc.contentcreator.dto.DTO;
import ccc.contentcreator.dto.FolderDTO;
import ccc.contentcreator.dto.OptionDTO;
import ccc.contentcreator.dto.ResourceDTO;
import ccc.contentcreator.dto.TemplateDTO;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.TableBinder;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;
import com.extjs.gxt.ui.client.widget.table.TableItem;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTable extends ContentPanel {

    private final ListStore<ResourceDTO> _detailsStore =
        new ListStore<ResourceDTO>();

    /**
     * Constructor.
     */
    ResourceTable() {

        setHeading("Resource Details");
        setLayout(new FitLayout());

        final List<TableColumn> columns = new ArrayList<TableColumn>();

        TableColumn col = new TableColumn("type", "Type", .1f);
        columns.add(col);

        col = new TableColumn("name", "Name", .45f);
        columns.add(col);

        col = new TableColumn("title", "Title", .45f);
        columns.add(col);

        final TableColumnModel cm = new TableColumnModel(columns);

        final Table tbl = new Table(cm);
        tbl.setSelectionMode(SelectionMode.SINGLE);
        tbl.setHorizontalScroll(true);
        tbl.setBorders(false);

        final TableBinder<ResourceDTO> binder =
            new TableBinder<ResourceDTO>(tbl, _detailsStore) {

            /** {@inheritDoc} */
            @Override
            protected TableItem createItem(final ResourceDTO model) {

                TableItem ti = super.createItem(model);
                ti.setId(model.getName());
                return ti;
            }
        };
        binder.init();

        final Menu contextMenu = new Menu();
        contextMenu.setWidth(130);

        final MenuItem preview = new MenuItem();
        preview.setId("preview-resource");
        preview.setText(Globals.uiConstants().preview());
        preview.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final ResourceDTO item =
                    (ResourceDTO) tbl.getSelectedItem().getModel();
                Globals.resourceService().getAbsolutePath(
                    item,
                    new ErrorReportingCallback<String>() {
                        public void onSuccess(final String arg0) {
                            new PreviewContentDialog(arg0).center();
                        }
                });
            }
        });
        contextMenu.add(preview);

        final MenuItem update = new MenuItem();
        update.setId("edit-resource");
        update.setText(Globals.uiConstants().edit());
        update.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                    final ResourceDTO item =
                        (ResourceDTO) tbl.getSelectedItem().getModel();
                     if ("TEMPLATE".equals(item.getType())) {
                         new EditTemplateDialog((TemplateDTO) item, _detailsStore).show();
                     } else if ("PAGE".equals(item.getType())) {
                         new UpdatePageDialog(item.getId()).show();
                     } else {
                         Globals.alert("No editor available for this resource.");
                     }
                }
            }
        );
        contextMenu.add(update);

        final MenuItem createAlias = new MenuItem();
        createAlias.setId("create-alias");
        createAlias.setText(Globals.uiConstants().createAlias());
        createAlias.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override public void componentSelected(final MenuEvent ce) {
                final ResourceDTO item =
                    (ResourceDTO) tbl.getSelectedItem().getModel();
                new CreateAliasDialog(item).show();
            }

        });
        contextMenu.add(createAlias);

        final MenuItem chooseTemplate = new MenuItem();
        chooseTemplate.setId("chooseTemplate-resource");
        chooseTemplate.setText(Globals.uiConstants().chooseTemplate());
        chooseTemplate.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(final MenuEvent ce) {
                final ResourceServiceAsync resourceService =
                    Globals.resourceService();

                final ResourceDTO item =
                    (ResourceDTO) tbl.getSelectedItem().getModel();

                if ("PAGE".equals(item.getType()) ||
                        "FOLDER".equals(item.getType())) {
                    resourceService.listTemplateOptionsForResource(item,
                        new AsyncCallback<List<OptionDTO<? extends DTO>>>(){

                            public void onFailure(final Throwable arg0) {
                                Window.alert(Globals.uiConstants().error());
                            }

                            public void onSuccess(
                            final List<OptionDTO<? extends DTO>> options) {
                                new ChooseTemplateDialog( options, item).show();
                            }});

                } else {
                    Globals.alert("Template cannot be chosen for this resource.");
                }
            }
        }
        );
        contextMenu.add(chooseTemplate);

        tbl.setContextMenu(contextMenu);

        add(tbl);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param selectedItem
     */
    public void displayResourcesFor(final TreeItem selectedItem) {
        _detailsStore.removeAll();

        // TODO: handle getSelectedItem() being null.
        final FolderDTO f = (FolderDTO) selectedItem.getModel();
        Globals.resourceService().getChildren(
            f,
            new ErrorReportingCallback<List<ResourceDTO>>() {
                public void onSuccess(
                                  final List<ResourceDTO> result) {
                    _detailsStore.add(result);
                }
        });
    }
}
