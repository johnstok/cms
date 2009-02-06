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

import ccc.contentcreator.actions.CreateAliasAction;
import ccc.contentcreator.actions.IncludeInMainMenuAction;
import ccc.contentcreator.actions.LockAction;
import ccc.contentcreator.actions.MoveAction;
import ccc.contentcreator.actions.PreviewAction;
import ccc.contentcreator.actions.PublishAction;
import ccc.contentcreator.actions.RemoveFromMainMenuAction;
import ccc.contentcreator.actions.RenameAction;
import ccc.contentcreator.actions.UnlockAction;
import ccc.contentcreator.actions.UnpublishAction;
import ccc.contentcreator.actions.UpdateMetadataAction;
import ccc.contentcreator.actions.UpdateTagsAction;
import ccc.contentcreator.actions.ViewHistoryAction;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.dialogs.ChooseTemplateDialog;
import ccc.contentcreator.dialogs.EditAliasDialog;
import ccc.contentcreator.dialogs.EditTemplateDialog;
import ccc.contentcreator.dialogs.UpdateFileDialog;
import ccc.contentcreator.dialogs.UpdatePageDialog;
import ccc.services.api.AliasDelta;
import ccc.services.api.FileDelta;
import ccc.services.api.PageDelta;
import ccc.services.api.ResourceDelta;
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
    private final QueriesServiceAsync _qs = Globals.queriesService();

    private final ResourceTable _table;

    // Actions
    private final Action _publishAction;
    private final Action _includeMainMenu;
    private final Action _removeMainMenu;
    private final Action _unpublishAction;
    private final Action _createAliasAction;
    private final Action _updateMetadataAction;
    private final Action _viewHistory;
    private final Action _updateTagsAction;
    private final Action _renameAction;
    private final Action _moveAction;
    private final Action _unlockAction;
    private final Action _lockAction;
    private final Action _previewAction;


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
        _unpublishAction = new UnpublishAction(_table);
        _createAliasAction = new CreateAliasAction(_table, _table._root);
        _updateMetadataAction = new UpdateMetadataAction(_table);
        _viewHistory = new ViewHistoryAction(_table);
        _updateTagsAction = new UpdateTagsAction(_table);
        _renameAction = new RenameAction(_table);
        _moveAction = new MoveAction(_table, _table._root);
        _unlockAction = new UnlockAction(_table);
        _lockAction = new LockAction(_table);
        _previewAction = new PreviewAction(_table);

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
        addMenuItem(
            "unpublish-resource",
            _constants.unpublish(),
            _unpublishAction);
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
        addMenuItem(
            "create-alias",
            _constants.createAlias(),
            _createAliasAction);
    }


    private void addPreview() {
        addMenuItem(
            "preview-resource",
            _constants.preview(),
            _previewAction);
    }


    private void addLockResource() {
        addMenuItem(
            "lock-resource",
            "Lock", // TODO: I18n
            _lockAction);
    }


    private void addUnlockResource() {
        addMenuItem(
            "unlock-resource",
            "Unlock", // TODO: I18n
            _unlockAction);
    }


    private void addMove() {
        addMenuItem(
            "move",
            _constants.move(),
            _moveAction);
    }


    private void addRename() {
        addMenuItem(
            "rename",
            _constants.rename(),
            _renameAction);
    }


    private void addUpdateTags() {
        addMenuItem(
            "update-tags",
            _constants.updateTags(),
            _updateTagsAction);
    }


    private void addViewHistory() {
        addMenuItem(
            "view-history",
            "View history", // TODO: I18n
            _viewHistory);
    }


    private void addUpdateMetadata() {
        addMenuItem(
            "update-metadata",
            _constants.updateMetadata(),
            _updateMetadataAction);
    }
}
