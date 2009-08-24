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

import ccc.contentcreator.binding.ActionSummaryModelData;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.types.ResourceType;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;


/**
 * Renderer factory for resource type.
 *
 * @author Civic Computing Ltd.
 */
public final class ResourceTypeRendererFactory  {

    /**
     * GridCellRenderer for ActionSummaryRenderer.
     *
     * @author Civic Computing Ltd.
     */
    private static final class ActionSummaryRenderer
        implements
            GridCellRenderer<ActionSummaryModelData> {

        /** {@inheritDoc} */
        @Override
        public Object render(final ActionSummaryModelData model,
                             final String property,
                             final ColumnData config,
                             final int rowIndex,
                             final int colIndex,
                             final ListStore<ActionSummaryModelData> store,
                             final Grid<ActionSummaryModelData> grid) {

            return resolveIcon(model.getSubjectType());
        }
    }

    /**
     * GridCellRenderer for ResourceSummaryModelData.
     *
     * @author Civic Computing Ltd.
     */
    private static final class ResourceSummaryRenderer
        implements
            GridCellRenderer<ResourceSummaryModelData> {


        /** {@inheritDoc} */
        @Override
        public String render(final ResourceSummaryModelData model,
                             final String property,
                             final ColumnData config,
                             final int rowIndex,
                             final int colIndex,
                             final ListStore<ResourceSummaryModelData> store,
                             final Grid<ResourceSummaryModelData> grid) {

            return resolveIcon(model.getType());
        }
    }

    private ResourceTypeRendererFactory() {
        // no-op
    }


    /**
     * Creates a GridCellRenderer for ResourceSummaryModelData.
     *
     * @return Renderer.
     */
    public static GridCellRenderer<ResourceSummaryModelData>
            rendererForResourceSummary() {
        return new ResourceSummaryRenderer();
    }

    /**
     * Creates a  GridCellRenderer for ResourceSummaryModelData.
     *
     * @return Renderer.
     */
    public static GridCellRenderer<ActionSummaryModelData>
            rendererForActionSummary() {
        return new ActionSummaryRenderer();
    }

    private static String resolveIcon(final ResourceType type) {

        if (type.equals(ResourceType.PAGE)) {
            return "<img src='images/icons/page.gif'/>&#160;";
        } else  if (type.equals(ResourceType.FOLDER)) {
            return "<img src='images/icons/folder.gif'/>&#160;";
        } else  if (type.equals(ResourceType.ALIAS)) {
            return "<img src='images/icons/link.gif'/>&#160;";
        } else  if (type.equals(ResourceType.ALIAS)) {
            return "<img src='images/icons/link.gif'/>&#160;";
        } else  if (type.equals(ResourceType.TEMPLATE)) {
            return "<img src='images/icons/page_code.gif'/>&#160;";
        } else  if (type.equals(ResourceType.FILE)) {
            return "<img src='images/icons/image.gif'/>&#160;";
        } else  if (type.equals(ResourceType.SEARCH)) {
            return "<img src='images/icons/magnifier.gif'/>&#160;";
        }
        return "unknown";
    }

}
