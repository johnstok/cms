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

package ccc.view.contentcreator.dialogs;

import java.util.ArrayList;
import java.util.List;

import ccc.view.contentcreator.callbacks.ErrorReportingCallback;
import ccc.view.contentcreator.client.Application;
import ccc.view.contentcreator.client.ResourceServiceAsync;
import ccc.view.contentcreator.client.Root;
import ccc.view.contentcreator.dto.FolderDTO;
import ccc.view.contentcreator.dto.ResourceDTO;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.TableBinder;
import com.extjs.gxt.ui.client.binder.TreeBinder;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;
import com.extjs.gxt.ui.client.widget.table.TableItem;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class GXTResourceExplorerPanel implements ResourceExplorerPanel {

    private final Application            _app;
    private final ListStore<ResourceDTO> _detailsStore =
        new ListStore<ResourceDTO>();
    private final LayoutContainer        _view;
    private final Tree                   _contentTree;
    private final Tree                   _assetsTree;

    /**
     * Constructor.
     *
     * @param app The application used for this panel.
     */
    public GXTResourceExplorerPanel(final Application app) {

        _app = app;
        final ResourceServiceAsync rsa = _app.lookupService();

        final Listener<TreeEvent> treeSelectionListener =
            new Listener<TreeEvent>() {

                public void handleEvent(final TreeEvent te) {
                    final FolderDTO f =
                        (FolderDTO) te.tree.getSelectedItem().getModel();
                    rsa.getChildren(
                        f,
                        new ErrorReportingCallback<List<ResourceDTO>>(_app) {
                            @Override public void onSuccess(final List<ResourceDTO> result) {
                                _detailsStore.removeAll();
                                _detailsStore.add(result);
                            }
                    });
                }
            };

        _contentTree = createResourceTree(rsa, Root.CONTENT);
        _contentTree.addListener(
            Events.SelectionChange,
            treeSelectionListener
            );

        _assetsTree = createResourceTree(rsa, Root.ASSETS);
        _assetsTree.addListener(
            Events.SelectionChange,
            treeSelectionListener);

        _view = createLeftRightPane(createResourceNavigator(),
                                    createFolderViewer());

    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    private ContentPanel createFolderViewer() {

        final ContentPanel left = new ContentPanel();
        left.setHeading("Resource Details");
        left.setLayout(new FitLayout());

        final List<TableColumn> columns = new ArrayList<TableColumn>();

        TableColumn col = new TableColumn("name", "Name", 180);
        col.setMinWidth(75);
        col.setMaxWidth(300);
        columns.add(col);

        col = new TableColumn("title", "Title", 75);
        columns.add(col);

        final TableColumnModel cm = new TableColumnModel(columns);

        final Table tbl = new Table(cm);
        tbl.setSelectionMode(SelectionMode.SINGLE);
        tbl.setHorizontalScroll(true);
        tbl.setBorders(false);

        final TableBinder<ResourceDTO> binder =
            new TableBinder<ResourceDTO>(tbl, _detailsStore) {

            /** {@inheritDoc} */
            @Override
            protected TableItem createItem(final ResourceDTO model) {

                TableItem ti = super.createItem(model);
                ti.setId(model.getName());
                return ti;
            }
        };
        binder.init();

        final Menu contextMenu = new Menu();
        contextMenu.setWidth(130);

        final MenuItem insert = new MenuItem();
        insert.setText("Insert Item");
        insert.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override public void componentSelected(final MenuEvent ce) {
                final TableItem item = tbl.getSelectedItem();
                if (item != null) {
                    _app.alert("Clicked: " + item.getModel());
                }
            }
        });
        contextMenu.add(insert);

        tbl.setContextMenu(contextMenu);

        left.add(tbl);

        return left;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    private LayoutContainer createResourceNavigator() {

        final ContentPanel cp = new ContentPanel();
        cp.setLayout(new AccordionLayout());
        cp.setBodyBorder(false);
        cp.setHeading("Resource Navigator");

        final ContentPanel contentPanel = new ContentPanel();
        contentPanel.setScrollMode(Scroll.AUTO);
        contentPanel.setHeading("Content");
        contentPanel.add(_contentTree);
        cp.add(contentPanel);

        final ContentPanel assetsPanel = new ContentPanel();
        assetsPanel.setScrollMode(Scroll.AUTO);
        assetsPanel.setHeading("Assets");
        assetsPanel.add(_assetsTree);
        cp.add(assetsPanel);

        return cp;
    }

    /**
     * TODO: Add a description of this method.
     * @param root The root folder with which we'll create the tree.
     *
     * @return
     */
    private Tree createResourceTree(final ResourceServiceAsync rsa,
                                    final Root root) {

        final RpcProxy<FolderDTO, List<FolderDTO>> proxy =
            new RpcProxy<FolderDTO, List<FolderDTO>>() {
                @Override protected void load(
                                final FolderDTO loadConfig,
                                final AsyncCallback<List<FolderDTO>> callback) {
                    rsa.getFolderChildren(loadConfig, callback);
                }
            };

        final TreeLoader<FolderDTO> loader =
            new BaseTreeLoader<FolderDTO>(proxy) {
            @Override public boolean hasChildren(final FolderDTO parent) {
                return parent.getFolderCount() > 0;
            }
        };

        final TreeStore<FolderDTO> store = new TreeStore<FolderDTO>(loader);

        final Tree tree = new Tree();

        final TreeBinder<FolderDTO> binder =
            new TreeBinder<FolderDTO>(tree, store) {
            @Override protected void update(final TreeItem item,
                                            final FolderDTO model) {
                super.update(item, model);
                item.setId(model.getName());
            }
        };

        binder.setIconProvider(new ModelStringProvider<FolderDTO>() {
            public String getStringValue(final FolderDTO model,
                                         final String property) {
                return (null == model) ? null : "images/gxt/icons/folder.gif";
            }
        });

        rsa.getRoot(root, new ErrorReportingCallback<FolderDTO>(_app) {
            @Override public void onSuccess(final FolderDTO result) {
                loader.load(result);
            }
        });

        tree.setSelectionMode(SelectionMode.SINGLE);

        final Menu contextMenu = new Menu();

        final MenuItem uploadFile = new MenuItem();
        uploadFile.setText(_app.constants().uploadFile());
        uploadFile.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(final MenuEvent ce) {

                final FolderDTO item = (FolderDTO) tree.getSelectionModel()
                                                       .getSelectedItem()
                                                       .getModel();
                new UploadFileDialog(_app,
                                     item.getId(),
                                     item.getName()).center();
            }
        });
        contextMenu.add(uploadFile);

        final MenuItem createFolder = new MenuItem();
        createFolder.setText(_app.constants().createFolder());
        createFolder.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override public void componentSelected(final MenuEvent ce) {

                final FolderDTO item = (FolderDTO) tree.getSelectionModel()
                                                       .getSelectedItem()
                                                       .getModel();

                final Dialog complex = new Dialog();
                complex.setButtons(Dialog.OKCANCEL);
                complex.setAutoHeight(true);
                complex.setWidth(400);
                complex.setHeading("Create folder");
                complex.setHideOnButtonClick(true);
                complex.setLayout(new FormLayout());

                final TextField<String> text = new TextField<String>();
                text.setFieldLabel("Name");
                text.setEmptyText("The folder name");
                text.setAllowBlank(false);
                complex.add(text);

                complex.addWindowListener(
                    new WindowListener(){
                        /** {@inheritDoc} */
                        @Override public void windowHide(final WindowEvent we) {
                            final String action = complex.getButtonPressed().getText();
                            GWT.log(
                                "Button: "+complex.getButtonPressed().getText(),
                                null);
                            if ("Ok".equals(action)) {
                                rsa.createFolder(
                                    item,
                                    text.getValue(),
                                    new ErrorReportingCallback<Void>(_app){
                                        @Override public void onSuccess(final Void result) {
                                            // TODO: refresh the folder...
                                        }
                                    }
                                );
                            }
                        }
                    });

                complex.show();

            }
        });
        contextMenu.add(createFolder);

        tree.setContextMenu(contextMenu);

        return tree;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param left
     * @param right
     * @return
     */
    private LayoutContainer createLeftRightPane(final LayoutContainer left,
                                                final LayoutContainer right) {

        final LayoutContainer lc = new LayoutContainer();
        lc.setLayout(new BorderLayout());

        final BorderLayoutData westData =
            new BorderLayoutData(LayoutRegion.WEST, 400);
        westData.setSplit(true);
        westData.setCollapsible(true);
        westData.setMargins(new Margins(5, 0, 5, 5));

        final BorderLayoutData centerData =
            new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(5));

        lc.add(left, westData);
        lc.add(right, centerData);
        return lc;
    }

    /** {@inheritDoc} */
    public LayoutContainer view() {

        return _view;
    }
}
