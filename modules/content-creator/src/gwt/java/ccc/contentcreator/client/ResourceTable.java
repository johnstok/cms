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

import static ccc.contentcreator.dialogs.AbstractBaseDialog.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.TableBinder;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;
import com.extjs.gxt.ui.client.widget.table.TableItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;


/**
 * TODO: Add Description for this type.
 * TODO: Extend simpler LayoutPanel rather than ContentPanel.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTable
    extends
        TablePanel
    implements
        SingleSelectionModel {

    private final UIConstants _constants = Globals.uiConstants();
    private final ListStore<ModelData> _detailsStore =
        new ListStore<ModelData>();
    private TreeItem _parentFolder = null;
    final ResourceSummary _root;
    private final ToolBar _toolBar = new FolderToolBar(this);
    final FolderResourceTree _tree;
    final Table _tbl;


    /**
     * Constructor.
     *
     * @param root ResourceSummary
     * @param tree FolderResourceTree
     */
    ResourceTable(final ResourceSummary root, final FolderResourceTree tree) {

        _root = root;
        _tree = tree;

        setTopComponent(_toolBar);
        setHeading("Resource Details"); // TODO: I18n.
        setLayout(new FitLayout());


        _tbl = new Table(createColumnModel());
        _tbl.setSelectionMode(SelectionMode.SINGLE);
        _tbl.setHorizontalScroll(true);
        _tbl.setBorders(false);
        _tbl.setBulkRender(false);

        final Menu contextMenu = new ResourceContextMenu(this);
        _tbl.setContextMenu(contextMenu);

        final TableBinder<ModelData> binder =
            new ResourceTableBinder(_tbl, _detailsStore);
        binder.init();

        add(_tbl);
    }


    /**
     * Updated this table to render the children of the specified TreeItem.
     *
     * @param selectedItem The item whose children we should display.
     */
    public void displayResourcesFor(final TreeItem selectedItem) {
        _parentFolder = selectedItem;
        _detailsStore.removeAll();

        // TODO: handle getSelectedItem() being null.
        final ModelData f = selectedItem.getModel();

        qs.getChildren(
            f.<String>get("id"),
            new ErrorReportingCallback<Collection<ResourceSummary>>() {
                public void onSuccess(
                                  final Collection<ResourceSummary> result) {
                    detailsStore().add(DataBinding.bindResourceSummary(result));
                }
        });
    }


    /**
     * Refresh view.
     */
    public void refreshTable() {
        if (_parentFolder  != null) {
            displayResourcesFor(_parentFolder);
        }
    }


    /**
     * Accessor for the details store.
     *
     * @return This table's details store.
     */
    protected ListStore<ModelData> detailsStore() {
        return _detailsStore;
    }


    private TableColumnModel createColumnModel() {

        final List<TableColumn> columns = new ArrayList<TableColumn>();
        TableColumn col;

        col = new TableColumn("action", "", .04f);
        columns.add(col);

        col = new TableColumn("type", _constants.type(), .08f);
        columns.add(col);

        col = new TableColumn("locked", _constants.lockedBy(), .08f);
        columns.add(col);

        col = new TableColumn("mmInclude", _constants.mainMenu(), PERCENT_10);
        columns.add(col);

        col = new TableColumn("published",
            _constants.publishedBy(),
            PERCENT_10);
        columns.add(col);

        col = new TableColumn("name", _constants.name(), PERCENT_30);
        columns.add(col);

        col = new TableColumn("title", _constants.title(), PERCENT_30);
        columns.add(col);

        final TableColumnModel cm = new TableColumnModel(columns);
        return cm;
    }


    /**
     * TODO: Add Description for this type.
     *
     * @author Civic Computing Ltd.
     */
    private final class ResourceTableBinder
        extends
            TableBinder<ModelData> {

        /**
         * Constructor.
         *
         * @param table
         * @param store
         */
        ResourceTableBinder(final Table table,
                                    final ListStore<ModelData> store) {
            super(table, store);
        }

        /** {@inheritDoc} */
        @Override protected void update(final ModelData model) {
            super.update(model);
            final TableItem ti = (TableItem) findItem(model);
            setActionButton(_tbl, model, ti);
        }

        /** {@inheritDoc} */
        @Override protected TableItem createItem(final ModelData model) {
            final TableItem ti = super.createItem(model);
            setActionButton(_tbl, model, ti);
            return ti;
        }

        private void setActionButton(final Table tbl,
                                     final ModelData model,
                                     final TableItem ti) {

            final Image tool = new Image("images/icons/cog_go.png");
            tool.addClickListener(new ClickListener() {

                public void onClick(final Widget sender) {
                    tbl.setSelectedItem(ti);
                    tbl.getContextMenu().showAt(sender.getAbsoluteLeft()+7,
                        sender.getAbsoluteTop()+7);
                }
            });
            ti.setId(model.<String>get("name"));
            ti.setWidget(0, tool);
        }
    }

    // --

    /** {@inheritDoc} */
    public ModelData getSelectedModel() {
        return _tbl.getSelectedItem().getModel();
    }


    /** {@inheritDoc} */
    public void notifyUpdate(final ModelData model) {
        detailsStore().update(model);
    }

    /** {@inheritDoc} */
    public ModelData getSelectedFolder() {
        return _parentFolder.getModel();
    }

    /** {@inheritDoc} */
    public void refresh() {
        refreshTable();
    }
}
