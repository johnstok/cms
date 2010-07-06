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

import ccc.api.core.ActionSummary;
import ccc.api.types.ResourceType;
import ccc.client.core.I18n;
import ccc.client.core.ImagePaths;
import ccc.client.gwt.binding.ResourceSummaryModelData;

import com.extjs.gxt.ui.client.data.BeanModel;
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
            GridCellRenderer<BeanModel> {


        /** {@inheritDoc} */
        @Override
        public Object render(final BeanModel model,
                             final String property,
                             final ColumnData config,
                             final int rowIndex,
                             final int colIndex,
                             final ListStore<BeanModel> store,
                             final Grid<BeanModel> grid) {
            if (ActionSummary.TYPE.equals(property)) {
                return
                    I18n.getLocalisedType(
                        model.<ActionSummary>getBean().getType());
            } else if (ActionSummary.STATUS.equals(property)) {
                return
                    I18n.getLocalisedStatus(
                        model.<ActionSummary>getBean().getStatus());
            }
            return resolveIcon(model.<ActionSummary>getBean().getSubjectType());
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
    public static GridCellRenderer<BeanModel>
            rendererForActionSummary() {
        return new ActionSummaryRenderer();
    }


    private static String resolveIcon(final ResourceType type) {
        if (type.equals(ResourceType.PAGE)) {
            return "<img src='"+ImagePaths.PAGE+"'/>&#160;";
        } else  if (type.equals(ResourceType.FOLDER)) {
            return "<img src='"+ImagePaths.FOLDER+"'/>&#160;";
        } else  if (type.equals(ResourceType.ALIAS)) {
            return "<img src='"+ImagePaths.ALIAS+"'/>&#160;";
        } else  if (type.equals(ResourceType.TEMPLATE)) {
            return "<img src='"+ImagePaths.TEMPLATE+"'/>&#160;";
        } else  if (type.equals(ResourceType.FILE)) {
            return "<img src='"+ImagePaths.FILE+"'/>&#160;";
        } else  if (type.equals(ResourceType.SEARCH)) {
            return "<img src='"+ImagePaths.SEARCH+"'/>&#160;";
        }
        return "unknown";
    }
}
