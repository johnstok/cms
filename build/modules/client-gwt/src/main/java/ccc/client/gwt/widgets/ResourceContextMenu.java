/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.client.gwt.widgets;

import static ccc.client.gwt.views.gxt.AbstractBaseDialog.CONTEXT_MENU_WIDTH;
import ccc.api.core.Group;
import ccc.api.core.Page;
import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Template;
import ccc.api.core.User;
import ccc.api.types.Permission;
import ccc.api.types.ResourceType;
import ccc.api.types.SortOrder;
import ccc.client.actions.ApplyWorkingCopyAction;
import ccc.client.actions.ChooseTemplateAction;
import ccc.client.actions.ClearWorkingCopyAction;
import ccc.client.actions.ComputeTemplateAction;
import ccc.client.actions.DeleteResourceAction;
import ccc.client.actions.IncludeInMainMenuAction;
import ccc.client.actions.ListGroups;
import ccc.client.actions.LockAction;
import ccc.client.actions.OpenCreateActionAction;
import ccc.client.actions.OpenCreateAliasAction;
import ccc.client.actions.OpenEditCacheAction;
import ccc.client.actions.OpenEditTextFileAction;
import ccc.client.actions.OpenMoveAction;
import ccc.client.actions.OpenRenameAction;
import ccc.client.actions.OpenUpdateAliasAction;
import ccc.client.actions.OpenUpdateFolderAction;
import ccc.client.actions.OpenUpdateMetadataAction;
import ccc.client.actions.OpenUpdateResourceAclAction;
import ccc.client.actions.OpenUpdateTemplateAction;
import ccc.client.actions.PageDeltaAction;
import ccc.client.actions.PreviewAction;
import ccc.client.actions.PublishAction;
import ccc.client.actions.RemoveFromMainMenuAction;
import ccc.client.actions.UnlockAction;
import ccc.client.actions.UnpublishAction;
import ccc.client.actions.ViewHistoryAction;
import ccc.client.core.Action;
import ccc.client.core.DefaultCallback;
import ccc.client.core.Globals;
import ccc.client.core.InternalServices;
import ccc.client.gwt.views.gxt.UpdateFileDialog;
import ccc.client.gwt.views.gxt.UpdatePageDialog;

import com.extjs.gxt.ui.client.event.Events;
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

    private final ResourceTable _table;
    private final Globals _globals = InternalServices.globals;

    // Actions
    private final Action _publishAction;
    private final Action _includeMainMenu;
    private final Action _removeMainMenu;
    private final Action _unpublishAction;
    private final Action _createAliasAction;
    private final Action _updateMetadataAction;
    private final Action _viewHistory;
    private final Action _renameAction;
    private final Action _moveAction;
    private final Action _unlockAction;
    private final Action _lockAction;
    private final Action _previewAction;
    private final Action _editFolderAction;
    private final Action _clearWorkingCopyAction;
    private final Action _previewWorkingCopyAction;
    private final Action _chooseTemplateAction;
    private final Action _createActionAction;
    private final Action _updateAclAction;
    private final Action _applyWorkingCopyAction;
    private final Action _editCacheAction;
    private final Action _deleteResourceAction;
    private final Action _editTextFileAction;

    /**
     * Constructor.
     *
     * @param tbl The table this menu will work for.
     */
    ResourceContextMenu(final ResourceTable tbl) {
        _table = tbl;

        _publishAction = new PublishAction(_table);
        _includeMainMenu = new IncludeInMainMenuAction(_table);
        _removeMainMenu = new RemoveFromMainMenuAction(_table);
        _unpublishAction = new UnpublishAction(_table);
        _createAliasAction = new OpenCreateAliasAction(_table);
        _updateMetadataAction = new OpenUpdateMetadataAction(_table);
        _viewHistory = new ViewHistoryAction(_table);
        _renameAction = new OpenRenameAction(_table);
        _moveAction = new OpenMoveAction(_table, _table.root());
        _unlockAction = new UnlockAction(_table);
        _lockAction = new LockAction(_table);
        _previewAction = new PreviewAction(_table, false);
        _editFolderAction = new OpenUpdateFolderAction(_table);
        _clearWorkingCopyAction = new ClearWorkingCopyAction(_table);
        _previewWorkingCopyAction = new PreviewAction(_table, true);
        _chooseTemplateAction = new ChooseTemplateAction(_table);
        _createActionAction = new OpenCreateActionAction(_table);
        _updateAclAction = new ListGroups(1,
            Globals.MAX_FETCH,
            "name",
            SortOrder.ASC) {
            @Override
            public void execute() {
                execute(new DefaultCallback<PagedCollection<Group>>(UI_CONSTANTS.updateRoles()) {
                    @Override
                    public void onSuccess(final PagedCollection<Group> groups) {
                        new OpenUpdateResourceAclAction(_table, groups.getElements())
                        .execute();
                    }});
            }};
        _applyWorkingCopyAction = new ApplyWorkingCopyAction(_table);
        _editCacheAction = new OpenEditCacheAction(_table);
        _deleteResourceAction = new DeleteResourceAction(_table);
        _editTextFileAction = new OpenEditTextFileAction(_table);

        setWidth(CONTEXT_MENU_WIDTH);

        addListener(
            Events.BeforeShow,
            new Listener<MenuEvent>(){
                public void handleEvent(final MenuEvent be) {
                    refreshMenuItems(be);
                }
            }
        );
    }


    private void refreshMenuItems(final MenuEvent be) {
        removeAll();
        final ResourceSummary item = _table.tableSelection();
        if (item == null) { // don't display menu if no item is selected.
            be.setCancelled(false);
            return;
        }

        addPreview();
        addViewHistory();
        if (null==item.getLockedBy()
            || "".equals(item.getLockedBy().toString())) {
            addLockResource();
        } else {
            if (item.getLockedBy().equals(_globals.currentUser().getUsername())
                 || _globals.currentUser().hasPermission(
                     Permission.RESOURCE_UNLOCK)) {
                addUnlockResource();
            }
            if (item.getLockedBy().equals(_globals.currentUser().getUsername())) {
                if (item.getPublishedBy() == null
                        || "".equals(item.getPublishedBy().toString())) {
                    addPublishResource();
                } else {
                    addUnpublishResource();
                }
                switch (item.getType()) {
                    case FOLDER:
                        addEditFolder();
                        addChooseTemplate();
                        addDeleteResource();
                        break;
                    case PAGE:
                        addEditResource();
                        addChooseTemplate();
                        addDeleteResource();
                        break;
                    case TEMPLATE:
                        addEditResource();
                        addDeleteResource();
                        break;
                    case ALIAS:
                        addEditResource();
                        addDeleteResource();
                        break;
                    case FILE:
                        addEditResource();
                        addEditTextFile();
                        addDeleteResource();
                        break;
                    case SEARCH:
                        addChooseTemplate();
                        break;
                    default:
                        break;
                }
                addMove();
                addRename();
                addUpdateAclAction();
                addUpdateMetadata();
                addCreateAlias();
                addCreateAction();
                addEditCache();

                if (item.isIncludeInMainMenu()) {
                    addRemoveFromMainMenu();
                } else {
                    addIncludeInMainMenu();
                }
                if (item.isHasWorkingCopy()) {
                    add(new SeparatorMenuItem());
                    addPreviewWorkingCopy();
                    addDeleteWorkingCopy();
                    if (ResourceType.FILE==item.getType()) {
                        addApplyFileWorkingCopy();
                    }
                }
            }
        }
    }


    private void addApplyFileWorkingCopy() {
        addMenuItem(Permission.FILE_UPDATE,
            "apply-working-copy",
            getConstants().applyWorkingCopy(),
            _applyWorkingCopyAction);
    }


    private void addDeleteResource() {
        addMenuItem(Permission.RESOURCE_DELETE,
            "delete-resource",
            getConstants().delete(),
            _deleteResourceAction);
    }

    private void addUpdateAclAction() {
        addMenuItem(Permission.RESOURCE_ACL_UPDATE,
            "update-resource-roles",
            getConstants().updateRoles(),
            _updateAclAction);
    }

    private void addCreateAction() {
        addMenuItem(Permission.ACTION_CREATE,
            "create-action",
            getConstants().createAction(),
            _createActionAction);
    }

    private void addEditFolder() {
        addMenuItem(Permission.FOLDER_UPDATE,
            "edit-folder",
            getConstants().edit(),
            _editFolderAction);
    }

    private void addPublishResource() {
        addMenuItem(Permission.RESOURCE_PUBLISH,
            "publish-resource",
            getConstants().publish(),
            _publishAction);
    }

    private void addIncludeInMainMenu() {
        addMenuItem(Permission.RESOURCE_MM,
            "mmInclude-resource",
            getConstants().addToMainMenu(),
            _includeMainMenu);
    }

    private void addRemoveFromMainMenu() {
        addMenuItem(Permission.RESOURCE_MM,
            "mmRemove-resource",
            getConstants().removeFromMainMenu(),
            _removeMainMenu);
    }

    private void addUnpublishResource() {
        addMenuItem(Permission.RESOURCE_UNPUBLISH,
            "unpublish-resource",
            getConstants().unpublish(),
            _unpublishAction);
    }

    private void addChooseTemplate() {
        addMenuItem(Permission.RESOURCE_UPDATE,
            "chooseTemplate-resource",
            getConstants().chooseTemplate(),
            _chooseTemplateAction);
    }

    private void addCreateAlias() {
        addMenuItem(Permission.ALIAS_CREATE,
            "create-alias",
            getConstants().createAlias(),
            _createAliasAction);
    }

    private void addPreview() {
        addMenuItem(Permission.RESOURCE_READ,
            "preview-resource",
            getConstants().preview(),
            _previewAction);
    }

    private void addLockResource() {
        addMenuItem(Permission.RESOURCE_LOCK,
            "lock-resource",
            getConstants().lock(),
            _lockAction);
    }

    private void addUnlockResource() {
        addMenuItem(Permission.RESOURCE_UNLOCK,
            "unlock-resource",
            getConstants().unlock(),
            _unlockAction);
    }

    private void addMove() {
        addMenuItem(Permission.RESOURCE_MOVE,
            "move",
            getConstants().move(),
            _moveAction);
    }

    private void addRename() {
        addMenuItem(Permission.RESOURCE_RENAME,
            "rename",
            getConstants().rename(),
            _renameAction);
    }

    private void addViewHistory() {
        addMenuItem(Permission.RESOURCE_READ,
            "view-history",
            getConstants().viewHistory(),
            _viewHistory);
    }

    private void addUpdateMetadata() {
        addMenuItem(Permission.RESOURCE_UPDATE,
            "update-metadata",
            getConstants().updateMetadata(),
            _updateMetadataAction);
    }

    private void addDeleteWorkingCopy() {
        addMenuItem(Permission.RESOURCE_UPDATE,
            "delete-workingCopy",
            getConstants().deleteWorkingCopy(),
            _clearWorkingCopyAction);
    }

    private void addPreviewWorkingCopy() {
        addMenuItem(Permission.RESOURCE_READ,
            "preview-workingCopy",
            getConstants().previewWorkingCopy(),
            _previewWorkingCopyAction);
    }

    private void addEditCache() {
        addMenuItem(Permission.RESOURCE_CACHE_UPDATE,
            "edit-cache",
            getConstants().editCacheDuration(),
            _editCacheAction);
    }

    private void addEditResource() {
        final MenuItem update = new MenuItem();
        final ResourceSummary item = _table.tableSelection();
        update.setText(getConstants().edit());
        update.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                switch (item.getType()) {
                    case TEMPLATE:
                        updateTemplate(item);
                        break;
                    case PAGE:
                        updatePage(item);
                        break;
                    case ALIAS:
                        updateAlias(item);
                        break;
                    case FILE:
                        updateFile(item);
                        break;
                    default:
                        InternalServices.window.alert(
                            getConstants().noEditorForResource());
                }
            }
        });
        final User user = _globals.currentUser();
        if ((user.hasPermission(Permission.TEMPLATE_UPDATE)
                && item.getType() == ResourceType.TEMPLATE)
                || (user.hasPermission(Permission.PAGE_UPDATE)
                        && item.getType() == ResourceType.PAGE)
                || (user.hasPermission(Permission.ALIAS_UPDATE)
                        && item.getType() == ResourceType.ALIAS)
                || (user.hasPermission(Permission.FILE_UPDATE)
                        && item.getType() == ResourceType.FILE)) {
            add(update);
        }
    }


    private void addEditTextFile() {
        addMenuItem(Permission.FILE_UPDATE,
            "editTextFile",
            getConstants().editInline(),
            _editTextFileAction);
    }



    private void updateFile(final ResourceSummary item) {
        new UpdateFileDialog(item).show();
    }

    // TODO: Factor these methods to actions
    private void updateAlias(final ResourceSummary item) {
        new OpenUpdateAliasAction(item, _table.root()).execute();
    }

    private void updatePage(final ResourceSummary item) {
        // Get the template for the page.
        new ComputeTemplateAction(
            getConstants().updateContent(), item.uriTemplate())
        .execute(
            new DefaultCallback<Template>(getConstants().updateContent()) {

                @Override
                public void onSuccess(final Template template) {
                    if (null==template) {
                        InternalServices.window.alert(getConstants().noTemplateFound());
                    } else {
                        new PageDeltaAction(getConstants().updateContent(), item)
                        .execute(new DefaultCallback<Page>(
                                                       getConstants().updateContent()) {
                            @Override
                            public void onSuccess(final Page delta) {
                                new UpdatePageDialog(
                                    delta,
                                    template,
                                    _table)
                                .show(); // Ok, pop the dialog.
                            }});
                    }
                }
            }
        );
    }

    private void updateTemplate(final ResourceSummary item) {
        new OpenUpdateTemplateAction(item, _table).execute();
    }
}
