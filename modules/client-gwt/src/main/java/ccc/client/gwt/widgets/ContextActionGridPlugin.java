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
package ccc.client.gwt.widgets;


import java.util.Map;

import ccc.api.core.User;
import ccc.client.core.ImagePaths;
import ccc.client.core.InternalServices;
import ccc.client.core.Response;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.remoting.UpdateCurrentUserAction;
import ccc.client.gwt.remoting.UpdateUserAction;
import ccc.client.gwt.views.gxt.UpdateCurrentUserDialog;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;


/**
 * {@link ComponentPlugin} for displaying context menu.
 *
 * @author Civic Computing Ltd.
 */
public class ContextActionGridPlugin
    extends ColumnConfig implements ComponentPlugin {

    /** COLUMN_WIDTH : int. */
    private static final int COLUMN_WIDTH = 30;
    private Grid<ModelData> _grid;
    private final Menu _contextMenu;

    /**
     * Constructor.
     *
     * @param contextMenu Context menu
     * @param resourceTable 
     */
    public ContextActionGridPlugin(final Menu contextMenu,
    							   final ColumnConfigSupport ccs) {
        _contextMenu = contextMenu;
        setWidth(COLUMN_WIDTH);
        setSortable(false);
        setResizable(false);
        setFixed(true);
        setMenuDisabled(true);
        setDataIndex("");
        setId("ToolGridExtension");
        // This is GWT image with GWT click handler.
        // Replace with GXT classes if possible.
        Image btn = new Image(ImagePaths.BULLET_DISK);
        btn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                if (ccs.visibleColumns() != null) {
                    User user = new GlobalsImpl().currentUser();
                    Map<String, String> meta = user.getMetadata();
                    meta.put(ccs.preferenceName(), ccs.visibleColumns());
                    
                    new UpdateCurrentUserAction(user) {
                    	 @Override protected void onNoContent(
                                 final Response response) {
                    		InternalServices.WINDOW.alert(
                    				UI_CONSTANTS.columnsSaved());
                    	}
					}.execute();
                }
            }
        });
        setWidget(btn, "");
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public void init(final Component component) {
        _grid = (Grid) component;
        _grid.addListener(Events.RowClick, new Listener<GridEvent>() {
            public void handleEvent(final GridEvent be) {
                onMouseDown(be);
            }

        });
    }

    /**
     * Displays context menu.
     *
     * @param e GridEvent
     */
    protected void onMouseDown(final GridEvent e) {
        if (e.getTarget().getClassName().equals("action")) {
            e.stopEvent();
            final El row = e.getTarget(".x-grid3-row", 15);
            final int idx = row.dom.getPropertyInt("rowIndex");
            _grid.getSelectionModel().select(idx, false);

            _contextMenu.showAt(e.getTarget().getAbsoluteLeft()+7 ,
                e.getTarget().getAbsoluteTop()+7);
        }
    }

}
