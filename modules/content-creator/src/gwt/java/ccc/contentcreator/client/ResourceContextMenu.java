/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import java.util.Collection;
import java.util.Map;

import ccc.contentcreator.actions.IncludeInMainMenuAction;
import ccc.contentcreator.actions.PublishAction;
import ccc.contentcreator.actions.RemoveFromMainMenuAction;
import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.dialogs.ChooseTemplateDialog;
import ccc.contentcreator.dialogs.CreateAliasDialog;
import ccc.contentcreator.dialogs.EditAliasDialog;
import ccc.contentcreator.dialogs.EditTemplateDialog;
import ccc.contentcreator.dialogs.HistoryDialog;
import ccc.contentcreator.dialogs.MetadataDialog;
import ccc.contentcreator.dialogs.MoveDialog;
import ccc.contentcreator.dialogs.PreviewContentDialog;
import ccc.contentcreator.dialogs.RenameDialog;
import ccc.contentcreator.dialogs.UpdateFileDialog;
import ccc.contentcreator.dialogs.UpdatePageDialog;
import ccc.contentcreator.dialogs.UpdateTagsDialog;
import ccc.services.api.AliasDelta;
import ccc.services.api.FileDelta;
import ccc.services.api.LogEntrySummary;
import ccc.services.api.PageDelta;
import ccc.services.api.ResourceDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;


/**
 * A context menu providing actions for a {@link ResourceTable}.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceContextMenu
    extends
        AbstractContextMenu {

    private final UIConstants _constants = Globals.uiConstants();
    private final CommandServiceAsync _cs = Globals.commandService();
    private final QueriesServiceAsync _qs = Globals.queriesService();

    private final ResourceTable _table;

    // Actions
    private final Action _publishAction;
    private final Action _includeMainMenu;
    private final Action _removeMainMenu;


    /**
     * Constructor.
     *
     * @param tbl The table this menu will work for.
     */
    ResourceContextMenu(final ResourceTable tbl) {
        super(tbl);

        _table = tbl;

        _publishAction = new PublishAction(_table);
        _includeMainMenu = new IncludeInMainMenuAction(_table);
        _removeMainMenu = new RemoveFromMainMenuAction(_table);

        setWidth(CONTEXT_MENU_WIDTH);

        addListener(Events.BeforeShow,
            new Listener<MenuEvent>(){
                public void handleEvent(final MenuEvent be) {
                    removeAll();
                    final ModelData item =
                        _table._tbl.getSelectedItem().getModel();

                    addPreview();
                    addViewHistory();
                    if (item.get("locked") == null
                        || "".equals(item.get("locked"))) {
                        addLockResource();
                    } else {
                        addUnlockResource();
                        if (item.<String>get("published") == null
                            || "".equals(item.get("published"))) {
                            addPublishResource();
                        } else {
                            addUnpublishResource();
                        }
                        if ("PAGE".equals(item.get("type"))) {
                            addEditResource();
                            addChooseTemplate();
                        } else if ("ALIAS".equals(item.get("type"))) {
                            addEditResource();
                        } else if ("FOLDER".equals(item.get("type"))) {
                            addChooseTemplate();
                        } else if ("TEMPLATE".equals(item.get("type"))) {
                            addEditResource();
                        } else if ("FILE".equals(item.get("type"))) {
                            addEditResource();
                        }
                        addMove();
                        addRename();
                        addUpdateTags();
                        addUpdateMetadata();
                        addCreateAlias();

                        if (item.<Boolean>get("mmInclude")) {
                            addRemoveFromMainMenu();
                        } else {
                            addIncludeInMainMenu();
                        }
                    }
                }
            }
        );
    }


    private void addPublishResource() {
        addMenuItem(
            "publish-resource",
            _constants.publish(),
            _publishAction);
    }


    private void addIncludeInMainMenu() {
        addMenuItem(
            "mmInclude-resource",
            _constants.addToMainMenu(),
            _includeMainMenu);
    }


    private void addRemoveFromMainMenu() {
        addMenuItem(
            "mmRemove-resource",
            _constants.removeFromMainMenu(),
            _removeMainMenu);
    }


    private void addUnpublishResource() {
        final MenuItem lockResource = new MenuItem();
        lockResource.setId("unpublish-resource");
        lockResource.setText(_constants.unpublish());
        lockResource.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item =
                    _table._tbl.getSelectedItem().getModel();
                _cs.unpublish(
                    item.<String>get("id"),
                    new ErrorReportingCallback<ResourceSummary>(){
                        public void onSuccess(final ResourceSummary arg0) {
                            DataBinding.merge(item, arg0);
                            _table.detailsStore().update(item);
                        }
                    }
                );
            }
        });
        add(lockResource);
    }


    private void addChooseTemplate() {
        final MenuItem chooseTemplate = new MenuItem();
        chooseTemplate.setId("chooseTemplate-resource");
        chooseTemplate.setText(_constants.chooseTemplate());
        chooseTemplate.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item = _table._tbl.getSelectedItem().getModel();
                if ("PAGE".equals(item.<String>get("type"))) {
                    _qs.pageDelta(
                        item.<String>get("id"),
                        new ErrorReportingCallback<PageDelta>(){
                            public void onSuccess(final PageDelta delta) {
                                _qs.templates(
                                    new ErrorReportingCallback<Collection<TemplateDelta>>() {
                                        public void onSuccess(final Collection<TemplateDelta> templates) {
                                            new ChooseTemplateDialog(
                                                delta,
                                                templates)
                                            .show();
                                        }
                                    }
                                );
                            }
                        }
                    );
                } else if ("FOLDER".equals(item.<String>get("type"))) {
                    _qs.folderDelta(
                        item.<String>get("id"),
                        new ErrorReportingCallback<ResourceDelta>(){
                            public void onSuccess(final ResourceDelta delta) {
                                _qs.templates(
                                    new ErrorReportingCallback<Collection<TemplateDelta>>(){
                                        public void onSuccess(final Collection<TemplateDelta> templates) {
                                            new ChooseTemplateDialog(
                                                delta,
                                                templates)
                                            .show();
                                        }
                                    }
                                );
                            }
                        }
                    );
                } else {
                    Globals.alert(// TODO: I18n
                        "Template cannot be chosen for this resource.");
                }
            }
        });
        add(chooseTemplate);
    }


    private void addEditResource() {
        final MenuItem update = new MenuItem();
        update.setId("edit-resource");
        update.setText(_constants.edit());
        update.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                    final ModelData item =
                        _table._tbl.getSelectedItem().getModel();
                    if ("TEMPLATE".equals(item.get("type"))) {
                        _qs.templateDelta(
                            item.<String>get("id"),
                            new ErrorReportingCallback<TemplateDelta>(){
                                public void onSuccess(final TemplateDelta td) {
                                    new EditTemplateDialog(
                                        td,
                                        item,
                                        _table)
                                    .show();
                                }
                            }
                        );
                    } else if ("PAGE".equals(item.get("type"))) {
                        _qs.pageDelta(
                            item.<String>get("id"),
                            new ErrorReportingCallback<PageDelta>() {
                                public void onSuccess(final PageDelta page) {
                                    if (null==page._computedTemplate) {
                                        Globals.alert(
                                            _constants.noTemplateFound());
                                    } else {
                                        new UpdatePageDialog(
                                            page,
                                            page._computedTemplate,
                                            _table)
                                        .show();
                                    }
                                }
                            }
                        );
                    } else if ("ALIAS".equals(item.get("type"))) {
                        _qs.aliasDelta(
                            item.<String>get("id"),
                            new ErrorReportingCallback<AliasDelta>() {
                                public void onSuccess(final AliasDelta result) {
                                    new EditAliasDialog(
                                        result,
                                        _table,
                                        _table._root)
                                    .show();
                                }
                            }
                        );
                    } else if ("FILE".equals(item.get("type"))) {
                        _qs.fileDelta(
                            item.<String>get("id"),
                            new ErrorReportingCallback<FileDelta>() {
                                public void onSuccess(final FileDelta result) {
                                    new UpdateFileDialog(result, _table).show();
                                }
                            }
                        );
                    } else {
                        Globals.alert("No editor available for this resource.");
                    }
                }
            }
        );
        add(update);
    }


    private void addCreateAlias() {
        final MenuItem createAlias = new MenuItem();
        createAlias.setId("create-alias");
        createAlias.setText(_constants.createAlias());
        createAlias.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item =
                    _table._tbl.getSelectedItem().getModel();
                new CreateAliasDialog(item, _table._root).show();
            }
        });
        add(createAlias);
    }


    private void addPreview() {
        final MenuItem preview = new MenuItem();
        preview.setId("preview-resource");
        preview.setText(_constants.preview());
        preview.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item =
                    _table._tbl.getSelectedItem().getModel();
                _qs.getAbsolutePath(
                    item.<String>get("id"),
                    new ErrorReportingCallback<String>() {
                        public void onSuccess(final String arg0) {
                            new PreviewContentDialog(arg0).show();
                        }
                    }
                );
            }
        });
        add(preview);
    }


    private void addLockResource() {
        final MenuItem lockResource = new MenuItem();
        lockResource.setId("lock-resource");
        lockResource.setText("Lock"); // TODO: I18n
        lockResource.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item = _table._tbl.getSelectedItem().getModel();
                _cs.lock(
                    item.<String>get("id"),
                    new ErrorReportingCallback<ResourceSummary>(){
                        public void onSuccess(final ResourceSummary arg0) {
                            DataBinding.merge(item, arg0);
                            _table.detailsStore().update(item);
                        }
                    }
                );
            }
        });
        add(lockResource);
    }


    private void addUnlockResource() {
        final MenuItem unlockResource = new MenuItem();
        unlockResource.setId("unlock-resource");
        unlockResource.setText("Unlock"); // TODO: I18n
        unlockResource.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item =
                    _table._tbl.getSelectedItem().getModel();
                _cs.unlock(
                    item.<String>get("id"),
                    new ErrorReportingCallback<ResourceSummary>(){
                        public void onSuccess(final ResourceSummary arg0) {
                            DataBinding.merge(item, arg0);
                            _table.detailsStore().update(item);
                        }
                    }
                );
            }
        });
        add(unlockResource);
    }


    private void addMove() {
        final MenuItem move = new MenuItem();
        move.setId("move");
        move.setText(_constants.move());
        move.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item =
                    _table._tbl.getSelectedItem().getModel();
                new MoveDialog(item, _table, _table._root).show();
            }
        });
        add(move);
    }


    private void addRename() {
        final MenuItem rename = new MenuItem();
        rename.setId("rename");
        rename.setText(_constants.rename());
        rename.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item =
                    _table._tbl.getSelectedItem().getModel();
                new RenameDialog(item, _table).show();
            }
        });
        add(rename);
    }


    private void addUpdateTags() {
        final MenuItem move = new MenuItem();
        move.setId("update-tags");
        move.setText(_constants.updateTags());
        move.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item =
                    _table._tbl.getSelectedItem().getModel();
                _qs.resourceDelta(
                    item.<String>get("id"),
                    new ErrorReportingCallback<ResourceDelta>(){
                        public void onSuccess(final ResourceDelta delta) {
                            if (delta == null) {
                                Globals.alert(
                                    _constants.noTemplateFound());
                            } else {
                                new UpdateTagsDialog(delta).show();
                            }
                        }
                    }
                );
            }
        });
        add(move);
    }


    private void addViewHistory() {
        final MenuItem unlockResource = new MenuItem();
        unlockResource.setId("view-history");
        unlockResource.setText("View history"); // TODO: I18n
        unlockResource.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item =
                    _table._tbl.getSelectedItem().getModel();
                _qs.history(
                    item.<String>get("id"),
                    new ErrorReportingCallback<Collection<LogEntrySummary>>(){
                        public void onSuccess(
                                      final Collection<LogEntrySummary> data) {
                            new HistoryDialog(data).show();
                        }

                    }
                );
            }
        });
        add(unlockResource);
    }


    private void addUpdateMetadata() {
        final MenuItem menuItem = new MenuItem();
        menuItem.setId("update-metadata");
        menuItem.setText(_constants.updateMetadata());
        menuItem.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item =
                    _table._tbl.getSelectedItem().getModel();
                _qs.metadata(
                    item.<String>get("id"),
                    new ErrorReportingCallback<Map<String, String>>(){
                        public void onSuccess(final Map<String, String> data) {
                            new MetadataDialog(item.<String>get("id"),
                                               data.entrySet())
                            .show();
                        }
                    }
                );
            }
        });
        add(menuItem);
    }
}
