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
import ccc.contentcreator.actions.ApplyWorkingCopyAction;
import ccc.contentcreator.actions.ChooseTemplateAction;
import ccc.contentcreator.actions.ClearWorkingCopyAction;
import ccc.contentcreator.actions.CreateActionAction;
import ccc.contentcreator.actions.CreateAliasAction;
import ccc.contentcreator.actions.EditCacheAction;
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
import ccc.contentcreator.actions.UpdateResourceRolesAction;
import ccc.contentcreator.actions.UpdateSortOrderAction;
import ccc.contentcreator.actions.UpdateTagsAction;
import ccc.contentcreator.actions.ViewHistoryAction;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.dialogs.EditTemplateDialog;
import ccc.contentcreator.dialogs.UpdateAliasDialog;
import ccc.contentcreator.dialogs.UpdateFileDialog;
import ccc.contentcreator.dialogs.UpdatePageDialog;
import ccc.services.api.AliasDelta;
import ccc.services.api.FileDelta;
import ccc.services.api.PageDelta;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserSummary;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;


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
    private final Action _updateSortAction;
    private final Action _clearWorkingCopyAction;
    private final Action _previewWorkingCopyAction;
    private final Action _chooseTemplateAction;
    private final Action _createActionAction;
    private final Action _updateRolesAction;
    private final Action _applyWorkingCopyAction;
    private final Action _editCacheAction;


    /**
     * Constructor.
     *
     * @param tbl The table this menu will work for.
     * @param user The UserSummary of the currently logged in user.
     */
    ResourceContextMenu(final ResourceTable tbl, final UserSummary user) {
        super(tbl);

        _table = tbl;

        _publishAction = new PublishAction(_table);
        _includeMainMenu = new IncludeInMainMenuAction(_table);
        _removeMainMenu = new RemoveFromMainMenuAction(_table);
        _unpublishAction = new UnpublishAction(_table);
        _createAliasAction = new CreateAliasAction(_table, _table.root());
        _updateMetadataAction = new UpdateMetadataAction(_table);
        _viewHistory = new ViewHistoryAction(_table);
        _updateTagsAction = new UpdateTagsAction(_table);
        _renameAction = new RenameAction(_table);
        _moveAction = new MoveAction(_table, _table.root());
        _unlockAction = new UnlockAction(_table);
        _lockAction = new LockAction(_table);
        _previewAction = new PreviewAction(_table, false);
        _updateSortAction = new UpdateSortOrderAction(_table);
        _clearWorkingCopyAction = new ClearWorkingCopyAction(_table);
        _previewWorkingCopyAction = new PreviewAction(_table, true);
        _chooseTemplateAction = new ChooseTemplateAction(_table);
        _createActionAction = new CreateActionAction(_table);
        _updateRolesAction = new UpdateResourceRolesAction(_table);
        _applyWorkingCopyAction = new ApplyWorkingCopyAction(_table);
        _editCacheAction = new EditCacheAction(_table);

        setWidth(CONTEXT_MENU_WIDTH);

        addListener(Events.BeforeShow,
            new Listener<MenuEvent>(){
                public void handleEvent(final MenuEvent be) {
                    refreshMenuItems(user, be);
                }
            }
        );
    }

    private void refreshMenuItems(final UserSummary user,
                              final MenuEvent be) {
        removeAll();
        final ResourceSummaryModelData item = _table.tableSelection();
        if (item == null) {
            // do not display context menu if no item is selected.
            be.doit = false;
            return;
        }

        addPreview();
        addViewHistory();
        if (item.get("locked") == null
            || "".equals(item.get("locked"))) {
            addLockResource();
        } else {
            if (item.get("locked").equals(user.getUsername())
                 || user.getRoles().contains(Globals.ADMINISTRATOR)) {
                addUnlockResource();
            }
            if (item.get("locked").equals(user.getUsername())) {
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
                    addFolderSortOrder();
                } else if ("TEMPLATE".equals(item.get("type"))) {
                    addEditResource();
                } else if ("FILE".equals(item.get("type"))) {
                    addEditResource();
                } else if ("SEARCH".equals(item.get("type"))) {
                    addChooseTemplate();
                }
                addMove();
                addRename();
                addUpdateRolesAction();
                addUpdateTags();
                addUpdateMetadata();
                addCreateAlias();
                addCreateAction();
                addEditCache(); // FIXME: ADMIN&BUILDER only?

                if (item.<Boolean>get("mmInclude")) {
                    addRemoveFromMainMenu();
                } else {
                    addIncludeInMainMenu();
                }
                if (item.<Boolean>get("workingCopy")) {
                    add(new SeparatorMenuItem());
                    addPreviewWorkingCopy();
                    addDeleteWorkingCopy();
                    if ("FILE".equals(item.get("type"))) {
                        addApplyWorkingCopy();
                    }
                }
            }
        }
    }


    /**
     * TODO: Add a description of this method.
     *
     */
    private void addApplyWorkingCopy() {
        addMenuItem(
            "apply-working-copy",
            _constants.applyWorkingCopy(),
            _applyWorkingCopyAction);
    }

    private void addUpdateRolesAction() {
        addMenuItem(
            "update-resource-roles",
            _constants.updateRoles(),
            _updateRolesAction);
    }


    private void addCreateAction() {
        addMenuItem(
            "create-action",
            _constants.createAction(),
            _createActionAction);
    }


    private void addFolderSortOrder() {
        addMenuItem(
            "sort-folder",
            _constants.changeSortOrder(),
            _updateSortAction);
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
        addMenuItem(
            "chooseTemplate-resource",
            _constants.chooseTemplate(),
            _chooseTemplateAction);
    }


    private void addEditResource() {
        final MenuItem update = new MenuItem();
        update.setId("edit-resource");
        update.setText(_constants.edit());
        update.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                    final ResourceSummaryModelData item = _table.tableSelection();
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
                        _qs.workingCopyDelta(
                            item.<String>get("id"),
                            new ErrorReportingCallback<PageDelta>() {
                                public void onSuccess(final PageDelta page) {
                                    if (null==page.getComputedTemplate()) {
                                        Globals.alert(
                                            _constants.noTemplateFound());
                                    } else {
                                        new UpdatePageDialog(
                                            page,
                                            page.getComputedTemplate(),
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
                                    new UpdateAliasDialog(
                                        result,
                                        _table,
                                        _table.root())
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
            _constants.lock(),
            _lockAction);
    }


    private void addUnlockResource() {
        addMenuItem(
            "unlock-resource",
            _constants.unlock(),
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
            _constants.viewHistory(),
            _viewHistory);
    }


    private void addUpdateMetadata() {
        addMenuItem(
            "update-metadata",
            _constants.updateMetadata(),
            _updateMetadataAction);
    }

    private void addDeleteWorkingCopy() {
        addMenuItem(
            "delete-workingCopy",
            _constants.deleteWorkingCopy(),
            _clearWorkingCopyAction);
    }

    private void addPreviewWorkingCopy() {
        addMenuItem(
            "preview-workingCopy",
            _constants.previewWorkingCopy(),
            _previewWorkingCopyAction);
    }

    private void addEditCache() {
        addMenuItem(
            "edit-cache",
            _constants.editCacheDuration(),
            _editCacheAction);
    }
}
