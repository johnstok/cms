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

import ccc.contentcreator.binding.ResourceSummaryModelData;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.menu.Menu;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ContextActionGridPlugin
    extends ColumnConfig implements ComponentPlugin {

    private Grid<ResourceSummaryModelData> _grid;
    private Menu _contextMenu;

    /**
     * Constructor.
     *
     * @param contextMenu Context menu
     */
    public ContextActionGridPlugin(final Menu contextMenu) {
        _contextMenu = contextMenu;
        setHeader("");
        setWidth(30);
        setSortable(false);
        setResizable(false);
        setFixed(true);
        setMenuDisabled(true);
        setDataIndex("");
        setId("ToolGridExtension");


        setRenderer(new GridCellRenderer<ResourceSummaryModelData>() {
            public String render(final ResourceSummaryModelData model,
                                 final String property,
                                 final ColumnData d,
                                 final int rowIndex,
                                 final int colIndex,
                                 final ListStore<ResourceSummaryModelData> store) {
                d.cellAttr = "rowspan='2'";
                final StringBuilder html = new StringBuilder();
                html.append("<img class='action' id='");
                html.append(model.getName());
                html.append("_cog");
                html.append("' src='images/icons/cog_go.png'/>&#160;");
                return html.toString();
                }
        });
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
            _grid.getSelectionModel().select(idx);

            _contextMenu.showAt(e.getTarget().getAbsoluteLeft()+7 ,
                e.getTarget().getAbsoluteTop()+7);
        }
    }

}
