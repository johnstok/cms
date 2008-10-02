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

import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.dto.UserDTO;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.TableBinder;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;
import com.extjs.gxt.ui.client.widget.table.TableItem;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UserTable extends ContentPanel {

    private final ListStore<UserDTO> _detailsStore =
        new ListStore<UserDTO>();
    private final ResourceServiceAsync _rsa;

    /**
     * Constructor.
     *
     * @param rsa ResourceServiceAsync
     */
    UserTable(final ResourceServiceAsync rsa) {
        _rsa = rsa;
        setHeading("User Details");
        setLayout(new FitLayout());

        final List<TableColumn> columns = new ArrayList<TableColumn>();

        TableColumn col = new TableColumn("username", "Username", .5f);
        columns.add(col);

        col = new TableColumn("email", "e-mail", .5f);
        columns.add(col);

        final TableColumnModel cm = new TableColumnModel(columns);

        final Table tbl = new Table(cm);
        tbl.setSelectionMode(SelectionMode.SINGLE);
        tbl.setHorizontalScroll(true);
        tbl.setBorders(false);

        final TableBinder<UserDTO> binder =
            new TableBinder<UserDTO>(tbl, _detailsStore) {

            /** {@inheritDoc} */
            @Override
            protected TableItem createItem(final UserDTO model) {

                TableItem ti = super.createItem(model);
                ti.setId(model.getUsername());
                return ti;
            }
        };
        binder.init();

        add(tbl);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param selectedItem The selected TreeItem.
     */
    public void displayUsersFor(final TreeItem selectedItem) {

        _detailsStore.removeAll();

        if ("All".equals(selectedItem.getText())) {
            _rsa.listUsers(
                new ErrorReportingCallback<List<UserDTO>>(new GwtApplication()) {
                    public void onSuccess(final List<UserDTO> result) {
                        _detailsStore.add(result);
                    }
                });
        } else if ("Content creator".equals(selectedItem.getText())){
            _rsa.listUsersWithRole(
                "CONTENT_CREATOR",
                new ErrorReportingCallback<List<UserDTO>>(new GwtApplication()) {
                    public void onSuccess(final List<UserDTO> result) {
                        _detailsStore.add(result);
                    }
                });
        } else if ("Site Builder".equals(selectedItem.getText())) {
            _rsa.listUsersWithRole(
                "SITE_BUILDER",
                new ErrorReportingCallback<List<UserDTO>>(new GwtApplication()) {
                    public void onSuccess(final List<UserDTO> result) {
                        _detailsStore.add(result);
                    }
                });
        } else if("Administrator".equals(selectedItem.getText())) {
            _rsa.listUsersWithRole(
                "ADMINISTRATOR",
                new ErrorReportingCallback<List<UserDTO>>(new GwtApplication()) {
                    public void onSuccess(final List<UserDTO> result) {
                        _detailsStore.add(result);
                    }
                });
        }
    }
}
