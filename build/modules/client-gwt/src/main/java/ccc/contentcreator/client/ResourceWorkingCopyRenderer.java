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

import ccc.contentcreator.binding.ResourceSummaryModelData;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;


/**
 * Renderer for resource working copy status.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceWorkingCopyRenderer
    implements GridCellRenderer<ResourceSummaryModelData> {

    /** {@inheritDoc} */
    @Override
    public Object render(final ResourceSummaryModelData model,
                         final String property,
                         final ColumnData config,
                         final int rowIndex,
                         final int colIndex,
                         final ListStore<ResourceSummaryModelData> store,
                         final Grid<ResourceSummaryModelData> grid) {

        if (model.hasWorkingCopy()) {
            return "<img src='static/images/icons/tick.gif'/>&#160;";
        }
        return "";
    }

}
