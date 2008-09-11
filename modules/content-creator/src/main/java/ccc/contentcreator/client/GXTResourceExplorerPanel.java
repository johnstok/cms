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

import java.util.ArrayList;
import java.util.List;

import ccc.contentcreator.api.Application;
import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.api.Root;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.callbacks.OneItemListCallback;
import ccc.contentcreator.dto.FolderDTO;
import ccc.contentcreator.dto.ResourceDTO;
import ccc.contentcreator.dto.TemplateDTO;

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
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
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
                            public void onSuccess(
                                              final List<ResourceDTO> result) {
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

        TableColumn col = new TableColumn("type", "Type", .1f);
        columns.add(col);

        col = new TableColumn("name", "Name", .45f);
        columns.add(col);

        col = new TableColumn("title", "Title", .45f);
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

        final MenuItem preview = new MenuItem();
        preview.setId("preview-resource");
        preview.setText(_app.constants().preview());
        preview.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                final ResourceDTO item =
                    (ResourceDTO) tbl.getSelectedItem().getModel();
                _app.lookupService().getAbsolutePath(
                    item,
                    new ErrorReportingCallback<String>(_app) {
                        public void onSuccess(final String arg0) {
                            new PreviewContentDialog(_app, arg0).center();
                        }
                });
            }
        });
        contextMenu.add(preview);

        final MenuItem update = new MenuItem();
        update.setId("edit-resource");
        update.setText(_app.constants().edit());
        update.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override public void componentSelected(final MenuEvent ce) {
                    final ResourceDTO item =
                        (ResourceDTO) tbl.getSelectedItem().getModel();
                     if ("TEMPLATE".equals(item.getType())) {
                         new CreateContentTemplateDialog(_app, (TemplateDTO) item, _detailsStore).center();
                     } else if ("PAGE".equals(item.getType())) {
                         new UpdateContentDialog(item.getId()).center();
                     } else {
                         _app.alert("No editor available for this resource.");
                     }
                }
            }
        );
        contextMenu.add(update);

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
        contentPanel.getHeader().setId("content-navigator");
        contentPanel.setScrollMode(Scroll.AUTO);
        contentPanel.setHeading("Content");
        contentPanel.add(_contentTree);
        cp.add(contentPanel);

        final ContentPanel assetsPanel = new ContentPanel();
        assetsPanel.getHeader().setId("assets-navigator");
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
                    if (null==loadConfig){
                        rsa.getRoot(
                            root,
                            new OneItemListCallback<FolderDTO>(callback));
                    } else {
                     rsa.getFolderChildren(loadConfig, callback);
                    }
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
        tree.setSelectionMode(SelectionMode.SINGLE);

        final TreeBinder<FolderDTO> binder =
            new TreeBinder<FolderDTO>(tree, store) {
            @Override protected void update(final TreeItem item,
                                            final FolderDTO model) {
                super.update(item, model);
                item.setId(model.getName());
            }
        };
        binder.setCaching(false);

        binder.setIconProvider(new ModelStringProvider<FolderDTO>() {
            public String getStringValue(final FolderDTO model,
                                         final String property) {
                return (null == model) ? null : "images/gxt/icons/folder.gif";
            }
        });
        loader.load(null);


        final Menu contextMenu = new Menu();
        contextMenu.setId("navigator-menu");

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
        createFolder.setId("create-folder");
        createFolder.setText(_app.constants().createFolder());
        createFolder.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override public void componentSelected(final MenuEvent me) {

                final FolderDTO item = (FolderDTO) tree.getSelectionModel()
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
                            public void componentSelected(ComponentEvent ce) {
                                rsa.createFolder(
                                    item,
                                    text.getValue(),
                                    new ErrorReportingCallback<FolderDTO>(_app){
                                        public void onSuccess(final FolderDTO result) {
                                            tree.fireEvent(Events.SelectionChange);
                                            store.add((FolderDTO) tree.getSelectedItem().getModel(), result, false);
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
