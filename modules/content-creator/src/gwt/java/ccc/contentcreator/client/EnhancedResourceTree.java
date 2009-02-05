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

import java.util.Collection;

import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.dialogs.ChooseTemplateDialog;
import ccc.contentcreator.dialogs.CreateFolderDialog;
import ccc.contentcreator.dialogs.CreatePageDialog;
import ccc.contentcreator.dialogs.EditTemplateDialog;
import ccc.contentcreator.dialogs.UploadFileDialog;
import ccc.services.api.ResourceDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;


/**
 * Resource tree with contextual menus.
 *
 * @author Civic Computing Ltd.
 */
public class EnhancedResourceTree extends FolderResourceTree {

    private final LeftRightPane _view;
    private final ResourceTable _rt;


    /**
     * Constructor.
     *
     * @param root The root of the tree.
     * @param view LeftRightPane of the surrounding view.
     */
    EnhancedResourceTree(final ResourceSummary root,
                         final LeftRightPane view) {

        super(root);

        _rt = new ResourceTable(root, this);
        _view = view;

        final Listener<TreeEvent> treeSelectionListener =
            new Listener<TreeEvent>() {

            public void handleEvent(final TreeEvent te) {
                _rt.displayResourcesFor(te.tree.getSelectedItem());
                _view.setRightHandPane(_rt);
            }
        };

        addListener(
            Events.SelectionChange,
            treeSelectionListener
        );

        final Menu contextMenu = new Menu();
        contextMenu.setId("navigator-menu");

        final MenuItem uploadFile = new MenuItem();
        uploadFile.setText(Globals.uiConstants().uploadFile());
        uploadFile.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(final MenuEvent ce) {

                final ModelData item = getSelectionModel()
                .getSelectedItem()
                .getModel();
                new UploadFileDialog(item.<String>get("id"),
                    item.<String>get("name"), _rt).show();
            }
        });
        contextMenu.add(uploadFile);

        final MenuItem createFolder = new MenuItem();
        createFolder.setId("create-folder");
        createFolder.setText(Globals.uiConstants().createFolder());
        createFolder.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override public void componentSelected(final MenuEvent me) {
                final ModelData item =
                    getSelectionModel().getSelectedItem().getModel();

                new CreateFolderDialog(item).show();
            }
        });
        contextMenu.add(createFolder);

        final MenuItem createPage = new MenuItem();
        createPage.setId("create-page");
        createPage.setText(Globals.uiConstants().createPage());
        createPage.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override public void componentSelected(final MenuEvent me) {

                final ModelData item =
                    getSelectionModel().getSelectedItem().getModel();

                Globals.queriesService().templates(
                    new ErrorReportingCallback<Collection<TemplateDelta>>(){
                        public void onSuccess(
                                      final Collection<TemplateDelta> list) {
                            // TODO: Need deltas here...
                            new CreatePageDialog(list, item, _rt).show();
                        }});
            }
        });
        contextMenu.add(createPage);


        final MenuItem createTemplate = new MenuItem();
        createTemplate.setId("create-template");
        createTemplate.setText(Globals.uiConstants().createTemplate());
        createTemplate.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {

                final ModelData item =
                    getSelectionModel().getSelectedItem().getModel();

                new EditTemplateDialog(item.<String>get("id"),
                                       _rt.detailsStore())
                    .show();
            }
        });
        contextMenu.add(createTemplate);

        final MenuItem chooseTemplate = new MenuItem();
        chooseTemplate.setId("choose-template");
        chooseTemplate.setText(Globals.uiConstants().chooseTemplate());
        chooseTemplate.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final ModelData item =
                    getSelectionModel().getSelectedItem().getModel();

                qs.folderDelta(item.<String>get("id"), new ErrorReportingCallback<ResourceDelta>(){
                    public void onSuccess(final ResourceDelta delta) {
                        qs.templates(new ErrorReportingCallback<Collection<TemplateDelta>>(){
                            public void onSuccess(final Collection<TemplateDelta> templates) {
                                new ChooseTemplateDialog(delta, templates).show();
                            }
                        });
                    }
                });
            }
        });
        contextMenu.add(chooseTemplate);

        setContextMenu(contextMenu);
    }
}
