/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.views.gxt;

import java.util.ArrayList;
import java.util.List;

import ccc.api.core.PagedCollection;
import ccc.api.core.User;
import ccc.api.types.SortOrder;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.remoting.ListUsersAction;
import ccc.client.i18n.UIConstants;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A simple user selector used by resource Access control.
 *
 * @author Civic Computing Ltd.
 */
public class UserACLSelector extends Window {

    private final ListStore<BeanModel> _store;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 485;
    private static final int PANEL_HEIGHT = 450;
    private static final int VIEW_HEIGHT = 400;
    private static final int PAGER_LIMIT = 15;
    private final PagingToolBar _pagerBar;
    private final UIConstants _constants;

    private Grid<BeanModel> _addUserGrid;
    private CheckBoxSelectionModel<BeanModel> _addUserSM;


    /**
     * Constructor.
     *
     * @param store The parent store.
     * @param constants UI Constants.
     */
    public UserACLSelector(final ListStore<BeanModel> store,
                        final UIConstants constants) {
        _store = store;
        _constants = constants;
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setModal(true);
        setBodyStyle("backgroundColor: white;");
        setScrollMode(Scroll.AUTOY);
        setHeading(_constants.selectUsers());


        final RpcProxy<PagingLoadResult<BeanModel>> proxy =
            createProxy();

        final PagingLoader<PagingLoadResult<BeanModel>> loader =
            new BasePagingLoader<PagingLoadResult
            <BeanModel>>(proxy);

        _addUserGrid = new Grid<BeanModel>(
           new ListStore<BeanModel>(loader), defineAddUserCM());
        _addUserGrid.setAutoExpandColumn(DataBinding.UserBeanModel.USERNAME);
        _addUserGrid.setSelectionModel(_addUserSM);
        _addUserGrid.addPlugin(_addUserSM);
        _addUserGrid.setBorders(false);
        _addUserGrid.setHeight(VIEW_HEIGHT);

        final ContentPanel panel = new ContentPanel();
        panel.setCollapsible(false);
        panel.setAnimCollapse(false);
        panel.setFrame(true);
        panel.setHeaderVisible(false);
        panel.setBodyBorder(false);
        panel.setHeight(PANEL_HEIGHT);

        _pagerBar = new PagingToolBar(PAGER_LIMIT);
        loader.setRemoteSort(true);
        loader.setSortField(DataBinding.UserBeanModel.USERNAME);
        _pagerBar.bind(loader);
        loader.load(0, PAGER_LIMIT);

        panel.add(_addUserGrid);
        panel.addButton(new Button(_constants.add(),
            new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(final ButtonEvent ce) {
                final List<BeanModel> items =
                    _addUserGrid.getSelectionModel().getSelectedItems();
                for (final BeanModel m : items) {
                    _store.add(m);
                }
                hide();
            }
        }));
        panel.setBottomComponent(_pagerBar);
        add(panel);
    }


    private RpcProxy<PagingLoadResult<BeanModel>> createProxy() {
        return new RpcProxy<PagingLoadResult<BeanModel>>() {

            @Override
            protected void load(final Object loadConfig,
                                final AsyncCallback<PagingLoadResult
                                <BeanModel>> callback) {
                if (null==loadConfig
                    || !(loadConfig instanceof BasePagingLoadConfig)) {
                    final PagingLoadResult<BeanModel> plr =
                        new BasePagingLoadResult<BeanModel>(null);
                    callback.onSuccess(plr);
                } else {
                    final BasePagingLoadConfig config =
                        (BasePagingLoadConfig) loadConfig;

                    final int page =  config.getOffset()/ config.getLimit()+1;
                    final SortOrder order = (
                        config.getSortDir() == Style.SortDir.ASC
                        ? SortOrder.ASC : SortOrder.DESC);

                    new ListUsersAction(
                        null,
                        page,
                        config.getLimit(),
                        config.getSortField(),
                        order) {

                        @Override
                        protected void execute(
                                           final PagedCollection<User> users) {
                            final List<BeanModel> results =
                                DataBinding.bindUserSummary(
                                    users.getElements());

                            final PagingLoadResult<BeanModel> plr =
                                new BasePagingLoadResult<BeanModel>(
                                    results, config.getOffset(),
                                    (int) users.getTotalCount());
                            callback.onSuccess(plr);
                        }

                    }.execute();
                }
            }

        };
    }

    private ColumnModel defineAddUserCM() {
        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        _addUserSM = new CheckBoxSelectionModel<BeanModel>();
        _addUserSM.setSelectionMode(SelectionMode.MULTI);
        configs.add(_addUserSM.getColumn());
        final ColumnConfig keyColumn =
            new ColumnConfig(
                DataBinding.UserBeanModel.USERNAME,
                _constants.name(),
                100);
        final TextField<String> keyField = new TextField<String>();
        keyField.setReadOnly(true);
        configs.add(keyColumn);

        return new ColumnModel(configs);
    }
}
