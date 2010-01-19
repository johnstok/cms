/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.contentcreator.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ccc.contentcreator.actions.remote.ListComments;
import ccc.contentcreator.binding.CommentModelData;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.client.Event.Type;
import ccc.contentcreator.controllers.CommentUpdatedEvent;
import ccc.contentcreator.controllers.UpdateCommentPresenter;
import ccc.contentcreator.views.CommentView;
import ccc.rest.dto.CommentDto;
import ccc.serialization.JsonKeys;
import ccc.types.CommentStatus;
import ccc.types.SortOrder;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * A panel that displays users using {@link Grid}.
 *
 * @author Civic Computing Ltd.
 */
public class CommentTable extends TablePanel implements EventBus {

    private ListStore<CommentModelData> _detailsStore =
        new ListStore<CommentModelData>();

    private final Grid<CommentModelData> _grid;
    private final PagingToolBar _pagerBar;

    private static final int COLUMN_WIDTH = 200;


    /**
     * Constructor.
     */
    CommentTable() {
        setHeading(UI_CONSTANTS.commentDetails());
        setLayout(new FitLayout());

        final Menu contextMenu = new Menu();
        final ContextActionGridPlugin gp =
            new ContextActionGridPlugin(contextMenu);
        gp.setRenderer(new CommentContextRenderer());
        final List<ColumnConfig> configs = createColumnConfigs(gp);

        final ColumnModel cm = new ColumnModel(configs);

        _grid = new Grid<CommentModelData>(_detailsStore, cm);

        contextMenu.add(createUpdateCommentMenu(_grid));

        _grid.setContextMenu(contextMenu);
        _grid.addPlugin(gp);
        add(_grid);

        _pagerBar = new PagingToolBar(PAGING_ROW_COUNT);
        setBottomComponent(_pagerBar);
    }


    private MenuItem createUpdateCommentMenu(
                                         final Grid<CommentModelData> grid) {
        final MenuItem updateComment =
            new MenuItem(UI_CONSTANTS.updateComment());
        updateComment.addSelectionListener(
            new SelectionListener<MenuEvent>() {
                @Override public void componentSelected(final MenuEvent ce) {
                    final CommentModelData commentModel =
                        grid.getSelectionModel().getSelectedItem();

                    new UpdateCommentPresenter(
                        GLOBALS,
                        CommentTable.this,
                        new CommentView(
                            UI_CONSTANTS.updateComment(), GLOBALS),
                        commentModel);
                }
            }
        );
        return updateComment;
    }


    private List<ColumnConfig> createColumnConfigs(
        final ContextActionGridPlugin gp) {

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        configs.add(gp);

        final ColumnConfig authorColumn = new ColumnConfig();
        authorColumn.setId(JsonKeys.AUTHOR);
        authorColumn.setHeader(UI_CONSTANTS.author());
        authorColumn.setWidth(COLUMN_WIDTH);
        configs.add(authorColumn);

        final ColumnConfig urlColumn = new ColumnConfig();
        urlColumn.setId(JsonKeys.URL);
        urlColumn.setHeader(UI_CONSTANTS.url());
        urlColumn.setWidth(COLUMN_WIDTH);
        configs.add(urlColumn);

        final ColumnConfig timestampColumn = new ColumnConfig();
        timestampColumn.setDateTimeFormat(
            DateTimeFormat.getMediumDateTimeFormat());
        timestampColumn.setId(JsonKeys.DATE_CREATED);
        timestampColumn.setHeader(UI_CONSTANTS.dateCreated());
        timestampColumn.setWidth(COLUMN_WIDTH);
        configs.add(timestampColumn);

        final ColumnConfig statusColumn = new ColumnConfig();
        statusColumn.setId(JsonKeys.STATUS);
        statusColumn.setHeader(UI_CONSTANTS.status());
        statusColumn.setWidth(COLUMN_WIDTH);
        configs.add(statusColumn);

        return configs;
    }


    /**
     *  Displays comments based on selected item.
     *
     * @param status Filter the comments to display on status.
     */
    public void displayComments(final CommentStatus status) {
        _detailsStore.removeAll();

        final DataProxy<PagingLoadResult<CommentModelData>> proxy =
            new RpcProxy<PagingLoadResult<CommentModelData>>() {

                @Override
                protected void load(final Object loadConfig,
                                    final AsyncCallback<PagingLoadResult<CommentModelData>> callback) {

                    if (null==loadConfig
                        || !(loadConfig instanceof BasePagingLoadConfig)) {
                        final PagingLoadResult<CommentModelData> plr =
                           new BasePagingLoadResult<CommentModelData>(null);
                        callback.onSuccess(plr);

                    } else {
                        final BasePagingLoadConfig config =
                            (BasePagingLoadConfig) loadConfig;

                        final int page =
                            config.getOffset()/ config.getLimit()+1;
                        final SortOrder order =
                            (Style.SortDir.ASC==config.getSortDir())
                                ? SortOrder.ASC
                                : SortOrder.DESC;

                        new ListComments(status,
                                         page,
                                         config.getLimit(),
                                         config.getSortField(),
                                         order) {

                            /** {@inheritDoc} */
                            @Override
                            protected void onFailure(final Throwable t) {
                                callback.onFailure(t);
                            }

                            @Override
                            protected void execute(
                                       final Collection<CommentDto> comments,
                                       final int totalCount) {

                                final List<CommentModelData> results =
                                    DataBinding.bindCommentSummary(comments);

                                final PagingLoadResult<CommentModelData> plr =
                                    new BasePagingLoadResult<CommentModelData>
                                (results, config.getOffset(), totalCount);
                                callback.onSuccess(plr);
                            }
                        }.execute();
                    }
                }
            };

        updatePager(proxy);
    }


    @SuppressWarnings("unchecked")
    private void updatePager(final DataProxy proxy){
        final PagingLoader loader = new BasePagingLoader(proxy);
        loader.setRemoteSort(true);
        _detailsStore = new ListStore<CommentModelData>(loader);
        _pagerBar.bind(loader);
        loader.load(0, PAGING_ROW_COUNT);
        final ColumnModel cm = _grid.getColumnModel();
        _grid.reconfigure(_detailsStore, cm);
    }


    /** {@inheritDoc} */
    @Override
    public void put(final Event event) {
        if (Type.COMMENT_UPDATED==event.getType()) {
            _detailsStore.update(
                ((CommentUpdatedEvent) event).getComment());
        }
    }
}
