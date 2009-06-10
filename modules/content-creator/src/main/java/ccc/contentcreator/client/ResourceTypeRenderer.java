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

import ccc.api.ResourceType;
import ccc.contentcreator.binding.ResourceSummaryModelData;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;


/**
 * Renderer for resource type.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTypeRenderer
    implements GridCellRenderer<ResourceSummaryModelData> {

    /** {@inheritDoc} */
    @Override
    public String render(final ResourceSummaryModelData model,
                         final String property,
                         final ColumnData config,
                         final int rowIndex,
                         final int colIndex,
                         final ListStore<ResourceSummaryModelData> store) {

        if (model.getType().equals(ResourceType.PAGE)) {
            return "<img src='images/icons/page.png'/>&#160;";
        } else  if (model.getType().equals(ResourceType.FOLDER)) {
            return "<img src='images/icons/folder.png'/>&#160;";
        } else  if (model.getType().equals(ResourceType.ALIAS)) {
            return "<img src='images/icons/link.png'/>&#160;";
        } else  if (model.getType().equals(ResourceType.ALIAS)) {
            return "<img src='images/icons/link.png'/>&#160;";
        } else  if (model.getType().equals(ResourceType.TEMPLATE)) {
            return "<img src='images/icons/page_code.png'/>&#160;";
        } else  if (model.getType().equals(ResourceType.FILE)) {
            return "<img src='images/icons/image.png'/>&#160;";
        } else  if (model.getType().equals(ResourceType.SEARCH)) {
            return "<img src='images/icons/magnifier.png'/>&#160;";
        }
        return "unknown";
    }

}
