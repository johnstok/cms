/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.client.gwt.core;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import ccc.api.core.ACL;
import ccc.api.core.Group;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Revision;
import ccc.api.core.Template;
import ccc.api.core.User;
import ccc.api.types.Duration;
import ccc.api.types.ResourceType;
import ccc.client.core.DialogFactory;
import ccc.client.core.InternalServices;
import ccc.client.core.LegacyView;
import ccc.client.core.SingleSelectionModel;
import ccc.client.gwt.views.gxt.AboutDialog;
import ccc.client.gwt.views.gxt.ChooseTemplateDialog;
import ccc.client.gwt.views.gxt.CreateActionDialog;
import ccc.client.gwt.views.gxt.CreateAliasDialog;
import ccc.client.gwt.views.gxt.CreateFolderDialog;
import ccc.client.gwt.views.gxt.CreatePageDialog;
import ccc.client.gwt.views.gxt.CreateTextFileDialog;
import ccc.client.gwt.views.gxt.CreateUserDialog;
import ccc.client.gwt.views.gxt.EditCacheDialog;
import ccc.client.gwt.views.gxt.EditTemplateDialog;
import ccc.client.gwt.views.gxt.EditTextFileDialog;
import ccc.client.gwt.views.gxt.EditUserDialog;
import ccc.client.gwt.views.gxt.HistoryDialog;
import ccc.client.gwt.views.gxt.LoginDialog;
import ccc.client.gwt.views.gxt.MoveDialog;
import ccc.client.gwt.views.gxt.PreviewTemplateDialog;
import ccc.client.gwt.views.gxt.RenameDialog;
import ccc.client.gwt.views.gxt.ResourceMetadataDialog;
import ccc.client.gwt.views.gxt.UpdateAliasDialog;
import ccc.client.gwt.views.gxt.UpdateCurrentUserDialog;
import ccc.client.gwt.views.gxt.UpdateFolderDialog;
import ccc.client.gwt.views.gxt.UpdateResourceAclDialog;
import ccc.client.gwt.views.gxt.UploadFileDialog;
import ccc.client.gwt.widgets.LeftRightPane;
import ccc.client.gwt.widgets.MainMenu;
import ccc.client.gwt.widgets.ResourceNavigator;
import ccc.client.views.ChangeResourceTemplate;
import ccc.client.views.CreateAction;
import ccc.client.views.CreateAlias;
import ccc.client.views.CreateFolder;
import ccc.client.views.CreatePage;
import ccc.client.views.CreateTextFile;
import ccc.client.views.CreateUser;
import ccc.client.views.EditTextFile;
import ccc.client.views.RenameResource;
import ccc.client.views.UpdateFolder;
import ccc.client.views.UpdateResourceAcl;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * GXT implementation of the {@link DialogFactory} API.
 *
 * @author Civic Computing Ltd.
 */
class GWTDialogFactory
    implements
        DialogFactory {


    /** {@inheritDoc} */
    @Override
    public UpdateFolder updateFolder() {
        return new UpdateFolderDialog();
    }


    /** {@inheritDoc} */
    @Override
    public LegacyView updateCurrentUser() {
        return new UpdateCurrentUserDialog();
    }


    /** {@inheritDoc} */
    @Override
    public RenameResource renameResource() {
        return new RenameDialog();
    }


    /** {@inheritDoc} */
    @Override
    public LegacyView moveResource(final ResourceSummary item,
                                   final SingleSelectionModel selectionModel,
                                   final ResourceSummary root) {
        return new MoveDialog(item, selectionModel, root);
    }


    /** {@inheritDoc} */
    @Override
    public CreateUser createUser(final List<Group> groups) {
        return new CreateUserDialog(groups);
    }


    /** {@inheritDoc} */
    @Override
    public CreateTextFile creatTextFile() {
        return new CreateTextFileDialog();
    }


    /** {@inheritDoc} */
    @Override
    public LegacyView createTemplate(
                                 final UUID id,
                                 final SingleSelectionModel selectionModel) {
        return new EditTemplateDialog(id, selectionModel);
    }


    /** {@inheritDoc} */
    @Override
    public CreatePage createPage(final Collection<Template> templates,
                                 final ResourceSummary root) {
        return new CreatePageDialog(templates, root);
    }


    /** {@inheritDoc} */
    @Override
    public CreateFolder createFolder() {
        return new CreateFolderDialog();
    }


    /** {@inheritDoc} */
    @Override
    public LegacyView uploadFile(final ResourceSummary parent,
                                 final SingleSelectionModel selectionModel) {
        return new UploadFileDialog(parent, selectionModel);
    }


    /** {@inheritDoc} */
    @Override
    public CreateAlias createAlias() {
        return new CreateAliasDialog();
    }


    /** {@inheritDoc} */
    @Override
    public CreateAction createAction() {
        return new CreateActionDialog();
    }


    /** {@inheritDoc} */
    @Override
    public LegacyView about() {
        return new AboutDialog();
    }


    /** {@inheritDoc} */
    @Override
    public ChangeResourceTemplate chooseTemplate() {
        return new ChooseTemplateDialog();
    }


    /** {@inheritDoc} */
    @Override
    public LegacyView viewHistory(final List<Revision> elements,
                                  final ResourceType type,
                                  final SingleSelectionModel selectionModel) {
        return new HistoryDialog(elements, type, selectionModel);
    }


    /** {@inheritDoc} */
    @Override
    public UpdateResourceAcl updateAcl(final ACL acl,
                                final Collection<Group> groups) {
        return new UpdateResourceAclDialog(acl, groups);
    }


    /** {@inheritDoc} */
    @Override
    public LegacyView updateAlias(final ResourceSummary alias,
                                  final String targetName,
                                  final ResourceSummary targetRoot) {
        return new UpdateAliasDialog(alias, targetName, targetRoot);
    }


    /** {@inheritDoc} */
    @Override
    public LegacyView previewTemplate(final Template delta) {
        return new PreviewTemplateDialog(delta);
    }


    /** {@inheritDoc} */
    @Override
    public LegacyView editCaching(final ResourceSummary tableSelection,
                                  final Duration duration) {
        return new EditCacheDialog(tableSelection, duration);
    }


    /** {@inheritDoc} */
    @Override
    public LegacyView login() {
        return new LoginDialog();
    }


    /** {@inheritDoc} */
    @Override
    public LegacyView editTemplate(final Template delta,
                                   final ResourceSummary template,
                                   final SingleSelectionModel table) {
        return new EditTemplateDialog(delta, template, table);
    }


    /** {@inheritDoc} */
    @Override
    public EditTextFile editTextFile() {
        return new EditTextFileDialog();
    }


    /** {@inheritDoc} */
    @Override
    public void mainWindow(final User user) {
        final LeftRightPane contentPane = new LeftRightPane();
        contentPane.setRightHandPane(new ContentPanel());
        ResourceSummary root = null;
        for (final ResourceSummary rr : InternalServices.roots.getElements()) {
            if (rr.getName().toString().equals("content")) {
                root = rr;
            }
        }
        contentPane.setLeftHandPane(
            new ResourceNavigator(contentPane,
                root,
                user));

        final Viewport vp =
            layoutMainWindow(new MainMenu(user), contentPane);

        RootPanel.get().add(vp);
    }


    /**
     * Lay out the GUI components of the main window.
     *
     * @param menu The tool-bar for the main menu.
     * @param content The layout container for the content panel.
     * @return The layout as a GXT view-port for rendering by the browser.
     */
    private Viewport layoutMainWindow(final ToolBar menu,
                                      final LayoutContainer content) {

        final LayoutContainer vp = new LayoutContainer();
         vp.setLayout(new RowLayout());
         vp.add(menu, new RowData(1, -1));
         vp.add(content, new RowData(1, 1));

         final Viewport v = new Viewport();
         v.setLayout(new FitLayout());
         v.add(vp);

        return v;
    }


    /** {@inheritDoc} */
    @Override
    public LegacyView editUser(final User delta,
                               final Collection<Group> groups) {
        return new EditUserDialog(delta, groups);
    }


    /** {@inheritDoc} */
    @Override
    public LegacyView updateMetadata(
                                 final ResourceSummary tableSelection,
                                 final Set<Entry<String, String>> entrySet,
                                 final SingleSelectionModel selectionModel) {
        return new ResourceMetadataDialog(
            tableSelection, entrySet, selectionModel);
    }
}
