/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.contentcreator.widgets;

import ccc.contentcreator.binding.CccModelData;
import ccc.contentcreator.core.ImagePaths;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;


/**
 * Renderer for a table's contextual menu icon for comment table.
 *
 * @author Civic Computing Ltd.
 * @param <T> The model.
 */
public class ContextMenuRenderer<T extends CccModelData>
    implements
        GridCellRenderer<T> {


    /** {@inheritDoc} */
    @Override
    public Object render(final T model,
                         final String property,
                         final ColumnData d,
                         final int rowIndex,
                         final int colIndex,
                         final ListStore<T> store,
                         final Grid<T> grid) {

        d.cellAttr = "rowspan='2'";
        final StringBuilder html = new StringBuilder();
        html.append("<img class='action' id='");
        html.append(model.getId());
        html.append("_cog");
        html.append("' src='"+ImagePaths.COG_GO+"'/>&#160;");
        return html.toString();
    }
}
