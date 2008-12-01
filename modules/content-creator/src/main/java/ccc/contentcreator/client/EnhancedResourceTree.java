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

import ccc.contentcreator.client.dialogs.CreateFolderDialog;
import ccc.contentcreator.client.dialogs.CreatePageDialog;
import ccc.contentcreator.client.dialogs.UploadFileDialog;
import ccc.services.api.FolderSummary;
import ccc.services.api.TemplateSummary;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;


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
     * @param rsa ResourceServiceAsync.
     * @param root The root of the tree.
     * @param view LeftRightPane of the surrounding view.
     */
    EnhancedResourceTree(final FolderSummary root,
                         final LeftRightPane view) {

        super(root);

        _rt = new ResourceTable(root);
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
                    item.<String>get("name"),
                    EnhancedResourceTree.this).center();
            }
        });
        contextMenu.add(uploadFile);

        final MenuItem createFolder = new MenuItem();
        createFolder.setId("create-folder");
        createFolder.setText(Globals.uiConstants().createFolder());
        createFolder.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override public void componentSelected(final MenuEvent me) {
                final ModelData item = getSelectionModel()
                .getSelectedItem()
                .getModel();

                final CreateFolderDialog dialog =
                    new CreateFolderDialog(item, store());
                dialog.show();
            }
        });
        contextMenu.add(createFolder);

        final MenuItem createPage = new MenuItem();
        createPage.setId("create-page");
        createPage.setText(Globals.uiConstants().createPage());
        createPage.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override public void componentSelected(final MenuEvent me) {

                final ModelData item = getSelectionModel()
                .getSelectedItem()
                .getModel();

                Globals.queriesService().templates(
                    new AsyncCallback<Collection<TemplateSummary>>(){

                        public void onFailure(final Throwable arg0) {
                            Window.alert(Globals.uiConstants().error());
                        }
                        public void onSuccess(
                                      final Collection<TemplateSummary> list) {
                            new CreatePageDialog(list, item, _rt).show();
                        }});
            }
        });
        contextMenu.add(createPage);

        setContextMenu(contextMenu);
    }
}
