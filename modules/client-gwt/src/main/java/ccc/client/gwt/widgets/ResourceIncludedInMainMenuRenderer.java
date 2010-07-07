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
package ccc.client.gwt.widgets;

import ccc.api.core.ResourceSummary;
import ccc.client.core.ImagePaths;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;


/**
 * Renderer for resource main menu inclusion.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceIncludedInMainMenuRenderer
    implements GridCellRenderer<BeanModel> {


    /** {@inheritDoc} */
    @Override
    public Object render(final BeanModel model,
                         final String property,
                         final ColumnData config,
                         final int rowIndex,
                         final int colIndex,
                         final ListStore<BeanModel> store,
                         final Grid<BeanModel> grid) {

        if (model.<ResourceSummary>getBean().isIncludeInMainMenu()) {
            return "<img src='"+ImagePaths.TICK+"'/>&#160;";
        }
        return "";
    }

}
