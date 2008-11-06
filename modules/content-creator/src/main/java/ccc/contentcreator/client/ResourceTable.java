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

import ccc.contentcreator.api.Action;
import ccc.contentcreator.api.JsonModelData;
import ccc.contentcreator.api.ResourceMgr;
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
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * TODO: Add Description for this type.
 * TODO: I18n for column names.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTable extends ContentPanel {

    /** CONTEXT_MENU_WIDTH : int. */
    private static final int CONTEXT_MENU_WIDTH = 130;
    /** PERCENT_45 : float. */ private static final float PERCENT_45 = .45f;
    /** PERCENT_10 : float. */ private static final float PERCENT_10 = .1f;
    /** PERCENT_40 : float. */ private static final float PERCENT_40 = .4f;

    private final ListStore<ResourceDTO> _detailsStore =
        new ListStore<ResourceDTO>();

    /**
     * Constructor.
     */
    ResourceTable() {

        setHeading("Resource Details");
        setLayout(new FitLayout());

        final List<TableColumn> columns = new ArrayList<TableColumn>();

        TableColumn col = new TableColumn("type", "Type", PERCENT_10);
        columns.add(col);

        col = new TableColumn("locked", "Locked by", PERCENT_10);
        columns.add(col);

        col = new TableColumn("name", "Name", PERCENT_40);
        columns.add(col);

        col = new TableColumn("title", "Title", PERCENT_40);
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
        contextMenu.setWidth(CONTEXT_MENU_WIDTH);

        addPreview(tbl, contextMenu);
        addEditResource(tbl, contextMenu);
        addCreateAlias(tbl, contextMenu);
        addLockResource(tbl, contextMenu);
        addUnlockResource(tbl, contextMenu);
        addChooseTemplate(tbl, contextMenu);

        tbl.setContextMenu(contextMenu);

        add(tbl);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param tbl
     * @param contextMenu
     */
    private void addChooseTemplate(final Table tbl, final Menu contextMenu) {

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

                if ("PAGE".equals(item.getType())
                    || "FOLDER".equals(item.getType())) {
                    resourceService.listTemplateOptionsForResource(item,
                        new AsyncCallback<List<OptionDTO<? extends DTO>>>(){

                            public void onFailure(final Throwable arg0) {
                                Window.alert(Globals.uiConstants().error());
                            }

                            public void onSuccess(
                            final List<OptionDTO<? extends DTO>> options) {
                                new ChooseTemplateDialog(options, item).show();
                            }});

                } else {
                  Globals.alert("Template cannot be chosen for this resource.");
                }
            }
        }
        );
        contextMenu.add(chooseTemplate);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param tbl
     * @param contextMenu
     */
    private void addEditResource(final Table tbl, final Menu contextMenu) {

        final MenuItem update = new MenuItem();
        update.setId("edit-resource");
        update.setText(Globals.uiConstants().edit());
        update.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                    final ResourceDTO item =
                        (ResourceDTO) tbl.getSelectedItem().getModel();
                     if ("TEMPLATE".equals(item.getType())) {
                         new EditTemplateDialog(
                             (TemplateDTO) item, detailsStore()).show();
                     } else if ("PAGE".equals(item.getType())) {
                         new UpdatePageDialog(item.getId()).show();
                     } else {
                        Globals.alert("No editor available for this resource.");
                     }
                }
            }
        );
        contextMenu.add(update);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param tbl
     * @param contextMenu
     */
    private void addCreateAlias(final Table tbl, final Menu contextMenu) {

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
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param tbl
     * @param contextMenu
     */
    private void addPreview(final Table tbl, final Menu contextMenu) {

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
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param tbl
     * @param contextMenu
     */
    private void addLockResource(final Table tbl, final Menu contextMenu) {

        final MenuItem lockResource = new MenuItem();
        lockResource.setId("lock-resource");
        lockResource.setText("Lock"); // TODO: I18n
        lockResource.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override public void componentSelected(final MenuEvent ce) {
                final ResourceDTO item =
                    (ResourceDTO) tbl.getSelectedItem().getModel();
                new ResourceMgr().lock(item.getId(), new Action<JSONValue>() {
                    public void execute(final JSONValue inputData) {
                        final JsonModelData md =
                            JsonModelData.fromObject(inputData);
                        item.set("locked", md.get("locked"));
                        detailsStore().update(item);
                    }});
            }

        });
        contextMenu.add(lockResource);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param tbl
     * @param contextMenu
     */
    private void addUnlockResource(final Table tbl, final Menu contextMenu) {

        final MenuItem unlockResource = new MenuItem();
        unlockResource.setId("unlock-resource");
        unlockResource.setText("Unlock"); // TODO: I18n
        unlockResource.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override public void componentSelected(final MenuEvent ce) {
                final ResourceDTO item =
                    (ResourceDTO) tbl.getSelectedItem().getModel();
                new ResourceMgr().unlock(item.getId(), new Action<JSONValue>() {
                    public void execute(final JSONValue inputData) {
                        final JsonModelData md =
                            JsonModelData.fromObject(inputData);
                        item.set("locked", md.get("locked"));
                        detailsStore().update(item);
                    }});
            }

        });
        contextMenu.add(unlockResource);
    }

    /**
     * Updated this table to render the children of the specified TreeItem.
     *
     * @param selectedItem The item whose children we should display.
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
                    detailsStore().add(result);
                }
        });
    }

    /**
     * Accessor for the details store.
     *
     * @return This table's details store.
     */
    protected ListStore<ResourceDTO> detailsStore() {
        return _detailsStore;
    }
}
