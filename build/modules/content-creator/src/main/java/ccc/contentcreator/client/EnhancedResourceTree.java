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

import java.util.List;

import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.api.Root;
import ccc.contentcreator.client.dialogs.CreateFolderDialog;
import ccc.contentcreator.client.dialogs.CreatePageDialog;
import ccc.contentcreator.client.dialogs.UploadFileDialog;
import ccc.contentcreator.dto.FolderDTO;
import ccc.contentcreator.dto.TemplateDTO;

import com.extjs.gxt.ui.client.Events;
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

    /** _view : LeftRightPane. */
    private LeftRightPane _view;
    /** _rt : ResourceTable. */
    private ResourceTable _rt = new ResourceTable();


    /**
     * Constructor.
     *
     * @param rsa ResourceServiceAsync.
     * @param root The root of the tree.
     * @param view LeftRightPane of the surrounding view.
     */
    EnhancedResourceTree(final ResourceServiceAsync rsa,
        final Root root,
        final LeftRightPane view) {

        super(rsa, root);

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

                final FolderDTO item = (FolderDTO) getSelectionModel()
                .getSelectedItem()
                .getModel();
                new UploadFileDialog(item.getId(),
                    item.getName(),
                    EnhancedResourceTree.this).center();
            }
        });
        contextMenu.add(uploadFile);

        final MenuItem createFolder = new MenuItem();
        createFolder.setId("create-folder");
        createFolder.setText(Globals.uiConstants().createFolder());
        createFolder.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override public void componentSelected(final MenuEvent me) {
                final FolderDTO item = (FolderDTO) getSelectionModel()
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

                final FolderDTO item = (FolderDTO) getSelectionModel()
                .getSelectedItem()
                .getModel();

                final ResourceServiceAsync resourceService =
                    Globals.resourceService();
                resourceService.listTemplates(
                    new AsyncCallback<List <TemplateDTO>>(){

                        public void onFailure(final Throwable arg0) {
                            Window.alert(Globals.uiConstants().error());
                        }
                        public void onSuccess(final List<TemplateDTO> list) {
                            new CreatePageDialog(list, item, _rt).show();
                        }});
            }
        });
        contextMenu.add(createPage);

        setContextMenu(contextMenu);
    }
}
