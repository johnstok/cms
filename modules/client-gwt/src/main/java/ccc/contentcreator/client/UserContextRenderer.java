/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import ccc.contentcreator.binding.UserSummaryModelData;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;


/**
 * Renderer for the contextual menu icon for user table.
 *
 * @author Civic Computing Ltd.
 */
public class UserContextRenderer implements
    GridCellRenderer<UserSummaryModelData> {


    /** {@inheritDoc} */
    @Override
    public Object render(final UserSummaryModelData model,
                         final String property,
                         final ColumnData d,
                         final int rowIndex,
                         final int colIndex,
                         final ListStore<UserSummaryModelData> store,
                         final Grid<UserSummaryModelData> grid) {

        d.cellAttr = "rowspan='2'";
        final StringBuilder html = new StringBuilder();
        html.append("<img class='action' id='");
        html.append(model.getUsername());
        html.append("_cog");
        html.append("' src='"+ImagePaths.COG_GO+"'/>&#160;");
        return html.toString();
    }
}
