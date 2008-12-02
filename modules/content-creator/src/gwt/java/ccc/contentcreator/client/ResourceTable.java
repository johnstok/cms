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

import static ccc.contentcreator.dialogs.AbstractBaseDialog.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.dialogs.CreateAliasDialog;
import ccc.contentcreator.dialogs.EditAliasDialog;
import ccc.contentcreator.dialogs.EditTemplateDialog;
import ccc.contentcreator.dialogs.MoveDialog;
import ccc.contentcreator.dialogs.PreviewContentDialog;
import ccc.contentcreator.dialogs.RenameDialog;
import ccc.contentcreator.dialogs.TableDataDisplayDialog;
import ccc.contentcreator.dialogs.UpdatePageDialog;
import ccc.contentcreator.dialogs.UpdateTagsDialog;
import ccc.services.api.LogEntrySummary;
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.TableBinder;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;
import com.extjs.gxt.ui.client.widget.table.TableItem;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * TODO: Add Description for this type.
 * TODO: I18n for heading.
 * TODO: Extend simpler LayoutPanel rather than ContentPanel.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTable extends TablePanel {

    private final UIConstants _constants = Globals.uiConstants();
    private final ListStore<ModelData> _detailsStore =
        new ListStore<ModelData>();
    private TreeItem _previousItem = null;
    private final ResourceSummary _root;


    /**
     * Constructor.
     */
    ResourceTable(final ResourceSummary root) {

        _root = root;

        setHeading("Resource Details");
        setLayout(new FitLayout());

        final List<TableColumn> columns = new ArrayList<TableColumn>();
        TableColumn col;

        col = new TableColumn("type", _constants.type(), PERCENT_10);
        columns.add(col);

        col = new TableColumn("locked", _constants.lockedBy(), PERCENT_10);
        columns.add(col);

        col = new TableColumn("published",
            _constants.publishedBy(),
            PERCENT_10);
        columns.add(col);

        col = new TableColumn("name", _constants.name(), PERCENT_30);
        columns.add(col);

        col = new TableColumn("title", _constants.title(), PERCENT_40);
        columns.add(col);

        final TableColumnModel cm = new TableColumnModel(columns);

        final Table tbl = new Table(cm);
        tbl.setSelectionMode(SelectionMode.SINGLE);
        tbl.setHorizontalScroll(true);
        tbl.setBorders(false);

        final TableBinder<ModelData> binder =
            new TableBinder<ModelData>(tbl, _detailsStore) {

            /** {@inheritDoc} */
            @Override
            protected TableItem createItem(final ModelData model) {

                TableItem ti = super.createItem(model);
                ti.setId(model.<String>get("name"));
                return ti;
            }
        };
        binder.init();

        final Menu contextMenu = new Menu();
        contextMenu.setWidth(CONTEXT_MENU_WIDTH);

        addContextMenuLogic(tbl, contextMenu);

        tbl.setContextMenu(contextMenu);
        add(tbl);
    }

    private void addContextMenuLogic(final Table tbl, final Menu contextMenu) {

        contextMenu.addListener(Events.BeforeShow, new Listener<MenuEvent>(){
            public void handleEvent(final MenuEvent be) {
                contextMenu.removeAll();
                final ModelData item = tbl.getSelectedItem().getModel();
                addCreateAlias(tbl, contextMenu);
                if (item.get("lockedBy") == null
                    || "".equals(item.get("lockedBy"))) {
                    addLockResource(tbl, contextMenu);
                } else {
                    addUnlockResource(tbl, contextMenu);
                }
                if (item.<String>get("published") == null
                    || "".equals(item.get("published"))) {
                    addPublishResource(tbl, contextMenu);
                } else {
                    addUnpublishResource(tbl, contextMenu);
                }
                if ("PAGE".equals(item.get("type"))) {
                    addEditResource(tbl, contextMenu);
                    addChooseTemplate(tbl, contextMenu);
                } else if ("ALIAS".equals(item.get("type"))) {
                    addEditResource(tbl, contextMenu);
                } else if ("FOLDER".equals(item.get("type"))) {
                    addChooseTemplate(tbl, contextMenu);
                } else if ("TEMPLATE".equals(item.get("type"))) {
                    addEditResource(tbl, contextMenu);
                }
                addPreview(tbl, contextMenu);
                addViewHistory(tbl, contextMenu);
                addMove(tbl, contextMenu);
                addRename(tbl, contextMenu);
                addUpdateTags(tbl, contextMenu);
            }
        });
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param tbl
     * @param contextMenu
     */
    private void addPublishResource(final Table tbl, final Menu contextMenu) {
        final MenuItem lockResource = new MenuItem();
        lockResource.setId("publish-resource");
        lockResource.setText(_constants.publish());
        lockResource.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item =
                    tbl.getSelectedItem().getModel();

                _cs.publish(
                    item.<String>get("id"),
                    new AsyncCallback<ResourceSummary>(){

                        public void onFailure(final Throwable arg0) {
                            Globals.unexpectedError(arg0);
                        }

                        public void onSuccess(final ResourceSummary arg0) {
                            DataBinding.merge(item, arg0);
                            detailsStore().update(item);
                        }
                    }
                );
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
    private void addUnpublishResource(final Table tbl, final Menu contextMenu) {
        final MenuItem lockResource = new MenuItem();
        lockResource.setId("unpublish-resource");
        lockResource.setText(_constants.unpublish());
        lockResource.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item =
                    tbl.getSelectedItem().getModel();

                _cs.unpublish(
                    item.<String>get("id"),
                    new AsyncCallback<ResourceSummary>(){

                        public void onFailure(final Throwable arg0) {
                            Globals.unexpectedError(arg0);
                        }

                        public void onSuccess(final ResourceSummary arg0) {
                            DataBinding.merge(item, arg0);
                            detailsStore().update(item);
                        }
                    }
                );
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
    private void addChooseTemplate(final Table tbl, final Menu contextMenu) {

        final MenuItem chooseTemplate = new MenuItem();
        chooseTemplate.setId("chooseTemplate-resource");
        chooseTemplate.setText(Globals.uiConstants().chooseTemplate());
        chooseTemplate.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(final MenuEvent ce) {

                final ModelData item =
                    tbl.getSelectedItem().getModel();

                if ("PAGE".equals(item.<String>get("type"))
                    || "FOLDER".equals(item.<String>get("type"))) {
                    // FIXME: Erm.
//                    resourceService.listTemplateOptionsForResource(
//                        /* item, */ null, // TODO: Fix
//                        new AsyncCallback<List<OptionDTO<? extends DTO>>>(){
//
//                            public void onFailure(final Throwable arg0) {
//                                Window.alert(Globals.uiConstants().error());
//                            }
//
//                            public void onSuccess(
//                            final List<OptionDTO<? extends DTO>> options) {
//                                new ChooseTemplateDialog(options, item).show();
//                            }});

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
                    final ModelData item = tbl.getSelectedItem().getModel();

                     if ("TEMPLATE".equals(item.get("type"))) {
                         new EditTemplateDialog(item, detailsStore()).show();
                     } else if ("PAGE".equals(item.get("type"))) {
                         new UpdatePageDialog(
                             item.<String>get("id"),
                             ResourceTable.this)
                         .show();
                     } else if ("ALIAS".equals(item.get("type"))) {
                         new EditAliasDialog(item, ResourceTable.this).show();
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
                final ModelData item =
                    tbl.getSelectedItem().getModel();
                new CreateAliasDialog(item, _root).show();
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
                final ModelData item =
                    tbl.getSelectedItem().getModel();
                qs.getAbsolutePath(
                    item.<String>get("id"),
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
                final ModelData item = tbl.getSelectedItem().getModel();
                _cs.lock(item.<String>get("id"), new AsyncCallback<ResourceSummary>() {
                    public void onFailure(final Throwable arg0) {
                        Globals.unexpectedError(arg0);
                    }
                    public void onSuccess(final ResourceSummary arg0) {
                            item.set("locked", arg0._lockedBy);
                            detailsStore().update(item);
                    }
                });
            }

        });
        contextMenu.add(lockResource);
    }


    private void addUnlockResource(final Table tbl, final Menu contextMenu) {

        final MenuItem unlockResource = new MenuItem();
        unlockResource.setId("unlock-resource");
        unlockResource.setText("Unlock"); // TODO: I18n
        unlockResource.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item =
                    tbl.getSelectedItem().getModel();
                _cs.unlock(
                    item.<String>get("id"),
                    new AsyncCallback<ResourceSummary>(){

                        public void onFailure(final Throwable arg0) {
                            Globals.unexpectedError(arg0);
                        }

                        public void onSuccess(final ResourceSummary arg0) {
                            DataBinding.merge(item, arg0);
                            detailsStore().update(item);
                        }});
            }

        });
        contextMenu.add(unlockResource);
    }

    private void addMove(final Table tbl, final Menu contextMenu) {

        final MenuItem move = new MenuItem();
        move.setId("move");
        move.setText(Globals.uiConstants().move());
        move.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item =
                    tbl.getSelectedItem().getModel();
                new MoveDialog(item, ResourceTable.this, _root).show();
            }

        });
        contextMenu.add(move);
    }

    private void addRename(final Table tbl, final Menu contextMenu) {

        final MenuItem rename = new MenuItem();
        rename.setId("rename");
        rename.setText(Globals.uiConstants().rename());
        rename.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item =
                    tbl.getSelectedItem().getModel();
                new RenameDialog(item, ResourceTable.this).show();
            }

        });
        contextMenu.add(rename);
    }

    private void addUpdateTags(final Table tbl, final Menu contextMenu) {

        final MenuItem move = new MenuItem();
        move.setId("update-tags");
        move.setText(Globals.uiConstants().updateTags());
        move.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item =
                    tbl.getSelectedItem().getModel();
                new UpdateTagsDialog(item, _detailsStore).show();
            }
        });
        contextMenu.add(move);
    }

    private void addViewHistory(final Table tbl, final Menu contextMenu) {

        final MenuItem unlockResource = new MenuItem();
        unlockResource.setId("view-history");
        unlockResource.setText("View history"); // TODO: I18n
        unlockResource.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item =
                    tbl.getSelectedItem().getModel();
                qs.history(
                    item.<String>get("id"),
                    new AsyncCallback<Collection<LogEntrySummary>>(){

                        public void onFailure(final Throwable arg0) {
                            Globals.unexpectedError(arg0);
                        }

                        public void onSuccess(final Collection<LogEntrySummary> data) {
                            new TableDataDisplayDialog(
                                "Resource History", data).show(); //TODO: I18n
                        }

                    }
                );
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
        _previousItem = selectedItem;
        _detailsStore.removeAll();

        // TODO: handle getSelectedItem() being null.
        final ModelData f = selectedItem.getModel();

        qs.getChildren(
            f.<String>get("id"),
            new ErrorReportingCallback<Collection<ResourceSummary>>() {
                public void onSuccess(
                                  final Collection<ResourceSummary> result) {
                    detailsStore().add(DataBinding.bindResourceSummary(result));
                }
        });
    }

    /**
     * Refresh view.
     *
     */
    public void refreshTable() {
        if (_previousItem  != null) {
            displayResourcesFor(_previousItem);
        }
    }

    /**
     * Accessor for the details store.
     *
     * @return This table's details store.
     */
    protected ListStore<ModelData> detailsStore() {
        return _detailsStore;
    }
}
