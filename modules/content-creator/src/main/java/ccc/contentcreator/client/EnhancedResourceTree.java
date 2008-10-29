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

import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.api.Root;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.dialogs.UploadFileDialog;
import ccc.contentcreator.dto.FolderDTO;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;


/**
 * Resource tree with contextual menus.
 *
 * @author Civic Computing Ltd.
 */
public class EnhancedResourceTree extends ResourceTree {

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

                final Dialog complex = new Dialog();
                complex.setId("create-folder-dialog");
                complex.setAutoHeight(true);
                complex.setWidth(400);
                complex.setHeading("Create folder");
                complex.setHideOnButtonClick(true);
                complex.setLayout(new FormLayout());

                final TextField<String> text = new TextField<String>();
                text.setId("folder-name");
                text.setFieldLabel("Name");
                text.setEmptyText("The folder name");
                text.setAllowBlank(false);
                complex.add(text);

                complex.setButtons(Dialog.CANCEL);
                final Button ok =
                    new Button(
                        "Ok",
                        new SelectionListener<ComponentEvent>() {
                            @Override
                            public void componentSelected(final ComponentEvent ce) {
                                rsa.createFolder(
                                    item,
                                    text.getValue(),
                                    new ErrorReportingCallback<FolderDTO>(){
                                        public void onSuccess(final FolderDTO result) {
                                            fireEvent(Events.SelectionChange);
                                            store().add((FolderDTO) getSelectedItem().getModel(), result, false);
                                        }
                                    }
                                );
                            }
                        });
                ok.setId("create-folder-ok");
                complex.getButtonBar().add(ok);

                complex.show();
            }
        });
        contextMenu.add(createFolder);

        setContextMenu(contextMenu);
    }
}
