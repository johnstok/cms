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

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
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

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        final ColumnConfig usernameColumn = new ColumnConfig();
        usernameColumn.setId("username");
        usernameColumn.setHeader("Username");
        usernameColumn.setWidth(400);
        configs.add(usernameColumn);

        final ColumnConfig emailColumn = new ColumnConfig();
        emailColumn.setId("email");
        emailColumn.setHeader("e-mail");
        emailColumn.setWidth(400);
        configs.add(emailColumn);

        final ColumnModel cm = new ColumnModel(configs);

        final Grid<UserDTO> grid = new Grid<UserDTO>(_detailsStore, cm);
        grid.setLoadMask(true);

        add(grid);
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
