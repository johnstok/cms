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
package ccc.contentcreator.widgets;

import static ccc.contentcreator.views.gxt.AbstractBaseDialog.*;

import java.util.Collection;

import ccc.contentcreator.actions.ChooseTemplateAction;
import ccc.contentcreator.actions.OpenCreateActionAction;
import ccc.contentcreator.actions.OpenCreateAliasAction;
import ccc.contentcreator.actions.OpenMoveAction;
import ccc.contentcreator.actions.OpenRenameAction;
import ccc.contentcreator.actions.OpenUpdateFolderAction;
import ccc.contentcreator.actions.PreviewAction;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.core.Action;
import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.remoting.ApplyWorkingCopyAction;
import ccc.contentcreator.remoting.ClearWorkingCopyAction;
import ccc.contentcreator.remoting.ComputeTemplateAction;
import ccc.contentcreator.remoting.DeleteResourceAction;
import ccc.contentcreator.remoting.IncludeInMainMenuAction;
import ccc.contentcreator.remoting.ListGroups;
import ccc.contentcreator.remoting.LockAction;
import ccc.contentcreator.remoting.OpenEditCacheAction;
import ccc.contentcreator.remoting.OpenEditTextFileAction;
import ccc.contentcreator.remoting.OpenUpdateAliasAction;
import ccc.contentcreator.remoting.OpenUpdateMetadataAction;
import ccc.contentcreator.remoting.OpenUpdateResourceRolesAction;
import ccc.contentcreator.remoting.OpenUpdateTemplateAction;
import ccc.contentcreator.remoting.PageDeltaAction;
import ccc.contentcreator.remoting.PublishAction;
import ccc.contentcreator.remoting.RemoveFromMainMenuAction;
import ccc.contentcreator.remoting.UnlockAction;
import ccc.contentcreator.remoting.UnpublishAction;
import ccc.contentcreator.remoting.ViewHistoryAction;
import ccc.contentcreator.views.gxt.UpdateFileDialog;
import ccc.contentcreator.views.gxt.UpdatePageDialog;
import ccc.rest.dto.GroupDto;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.TemplateSummary;
import ccc.rest.dto.UserDto;
import ccc.types.Permission;
import ccc.types.ResourceType;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;


/**
 * A context menu providing actions for a {@link ResourceTable}.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceContextMenu
    extends
        AbstractContextMenu {

    private final ResourceTable _table;

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
    private final Action _updateRolesAction;
    private final Action _applyWorkingCopyAction;
    private final Action _editCacheAction;
    private final Action _deleteResourceAction;
    private final Action _editTextFileAction;

    /**
     * Constructor.
     *
     * @param tbl The table this menu will work for.
     * @param user The UserSummary of the currently logged in user.
     */
    ResourceContextMenu(final ResourceTable tbl, final UserDto user) {
        _table = tbl;

        _publishAction = new PublishAction(_table);
        _includeMainMenu = new IncludeInMainMenuAction(_table);
        _removeMainMenu = new RemoveFromMainMenuAction(_table);
        _unpublishAction = new UnpublishAction(_table);
        _createAliasAction = new OpenCreateAliasAction(_table, _table.root());
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
        _updateRolesAction = new ListGroups() {
            @Override
            protected void execute(final Collection<GroupDto> g) {
                new OpenUpdateResourceRolesAction(_table, g)
                    .execute();
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
                    refreshMenuItems(user, be);
                }
            }
        );
    }


    private void refreshMenuItems(final UserDto user,
                                  final MenuEvent be) {
        removeAll();
        final ResourceSummaryModelData item = _table.tableSelection();
        if (item == null) { // don't display menu if no item is selected.
            be.setCancelled(false);
            return;
        }

        addPreview();
        addViewHistory();
        if (null==item.getLocked()
            || "".equals(item.getLocked().toString())) {
            addLockResource();
        } else {
            if (item.getLocked().equals(user.getUsername())
                 || user.hasPermission(Permission.RESOURCE_UNLOCK)) {
                addUnlockResource();
            }
            if (item.getLocked().equals(user.getUsername())) {
                if (item.getPublished() == null
                        || "".equals(item.getPublished().toString())) {
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
                if (user.hasPermission(Permission.RESOURCE_ACL_UPDATE)) {
                    addUpdateRolesAction();
                }
                addUpdateMetadata();
                addCreateAlias();
                addCreateAction();
                if (user.hasPermission(Permission.RESOURCE_CACHE_UPDATE)) {
                    addEditCache();
                }

                if (item.isIncludedInMainMenu()) {
                    addRemoveFromMainMenu();
                } else {
                    addIncludeInMainMenu();
                }
                if (item.hasWorkingCopy()) {
                    add(new SeparatorMenuItem());
                    addPreviewWorkingCopy();
                    addDeleteWorkingCopy();
                    if (ResourceType.FILE==item.getType()) {
                        addApplyWorkingCopy();
                    }
                }
            }
        }
    }


    private void addApplyWorkingCopy() {
        addMenuItem(
            "apply-working-copy",
            getConstants().applyWorkingCopy(),
            _applyWorkingCopyAction);
    }


    private void addDeleteResource() {
        addMenuItem(
            "delete-resource",
            getConstants().delete(),
            _deleteResourceAction);
    }

    private void addUpdateRolesAction() {
        addMenuItem(
            "update-resource-roles",
            getConstants().updateRoles(),
            _updateRolesAction);
    }

    private void addCreateAction() {
        addMenuItem(
            "create-action",
            getConstants().createAction(),
            _createActionAction);
    }

    private void addEditFolder() {
        addMenuItem(
            "edit-folder",
            getConstants().edit(),
            _editFolderAction);
    }

    private void addPublishResource() {
        addMenuItem(
            "publish-resource",
            getConstants().publish(),
            _publishAction);
    }

    private void addIncludeInMainMenu() {
        addMenuItem(
            "mmInclude-resource",
            getConstants().addToMainMenu(),
            _includeMainMenu);
    }

    private void addRemoveFromMainMenu() {
        addMenuItem(
            "mmRemove-resource",
            getConstants().removeFromMainMenu(),
            _removeMainMenu);
    }

    private void addUnpublishResource() {
        addMenuItem(
            "unpublish-resource",
            getConstants().unpublish(),
            _unpublishAction);
    }

    private void addChooseTemplate() {
        addMenuItem(
            "chooseTemplate-resource",
            getConstants().chooseTemplate(),
            _chooseTemplateAction);
    }

    private void addCreateAlias() {
        addMenuItem(
            "create-alias",
            getConstants().createAlias(),
            _createAliasAction);
    }

    private void addPreview() {
        addMenuItem(
            "preview-resource",
            getConstants().preview(),
            _previewAction);
    }

    private void addLockResource() {
        addMenuItem(
            "lock-resource",
            getConstants().lock(),
            _lockAction);
    }

    private void addUnlockResource() {
        addMenuItem(
            "unlock-resource",
            getConstants().unlock(),
            _unlockAction);
    }

    private void addMove() {
        addMenuItem(
            "move",
            getConstants().move(),
            _moveAction);
    }

    private void addRename() {
        addMenuItem(
            "rename",
            getConstants().rename(),
            _renameAction);
    }

    private void addViewHistory() {
        addMenuItem(
            "view-history",
            getConstants().viewHistory(),
            _viewHistory);
    }

    private void addUpdateMetadata() {
        addMenuItem(
            "update-metadata",
            getConstants().updateMetadata(),
            _updateMetadataAction);
    }

    private void addDeleteWorkingCopy() {
        addMenuItem(
            "delete-workingCopy",
            getConstants().deleteWorkingCopy(),
            _clearWorkingCopyAction);
    }

    private void addPreviewWorkingCopy() {
        addMenuItem(
            "preview-workingCopy",
            getConstants().previewWorkingCopy(),
            _previewWorkingCopyAction);
    }

    private void addEditCache() {
        addMenuItem(
            "edit-cache",
            getConstants().editCacheDuration(),
            _editCacheAction);
    }

    private void addEditResource() {
        final MenuItem update = new MenuItem();
        update.setId("edit-resource");
        update.setText(getConstants().edit());
        update.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final ResourceSummaryModelData item = _table.tableSelection();

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
                        getGlobals().alert(
                            getConstants().noEditorForResource());
                }
            }
        });
        add(update);
    }


    private void addEditTextFile() {
        addMenuItem(
            "editTextFile",
            getConstants().editInline(),
            _editTextFileAction);
    }



    private void updateFile(final ResourceSummaryModelData item) {
        new UpdateFileDialog(item.getId()).show();
    }

    // TODO: Factor these methods to actions
    private void updateAlias(final ResourceSummaryModelData item) {
        new OpenUpdateAliasAction(item, _table.root()).execute();
    }

    private void updatePage(final ResourceSummaryModelData item) {
        // Get the template for the page.
        new ComputeTemplateAction(
            getConstants().updateContent(), item.getId()) {

            /** {@inheritDoc} */
            @Override
            protected void onNoContent(final Response response) {
                getGlobals().alert(getConstants().noTemplateFound());
            }

            /** {@inheritDoc} */
            // Get a delta to edit.
            @Override protected void onOK(final Response response) {
            final TemplateSummary ts =
                new TemplateSummary(
                  new GwtJson(
                      JSONParser.parse(
                          response.getText()).isObject()));

                new PageDeltaAction(
                    getConstants().updateContent(), item.getId()){
                    @Override
                    protected void execute(final PageDelta delta) {
                        new UpdatePageDialog(
                            item.getId(),
                            delta,
                            item.getName(),
                            ts,
                            _table)
                        .show(); // Ok, pop the dialog.
                    }

                }.execute();
            }

        }.execute();
    }

    private void updateTemplate(final ResourceSummaryModelData item) {
        new OpenUpdateTemplateAction(item, _table).execute();
    }
}