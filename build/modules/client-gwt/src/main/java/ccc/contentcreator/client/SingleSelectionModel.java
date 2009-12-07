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
package ccc.contentcreator.client;

import ccc.contentcreator.binding.ResourceSummaryModelData;


/**
 * API for accessing a dual-pane resource view.
 *
 * @author Civic Computing Ltd.
 */
public interface SingleSelectionModel {

    /**
     * Access the currently selected table record.
     *
     * @return The selected model data.
     */
    ResourceSummaryModelData tableSelection();


    /**
     * Access the currently selected tree node.
     *
     * @return The selected model data.
     */
    ResourceSummaryModelData treeSelection();


    /**
     * Update the specified model data if it exists in the data store.
     *
     * @param model The model to update
     */
    void update(ResourceSummaryModelData model);


    /**
     * Move a resource to a new location.
     *
     * @param model The resource to move.
     * @param newParent The new parent.
     * @param oldParent The old parent.
     */
    void move(ResourceSummaryModelData model,
              ResourceSummaryModelData newParent,
              ResourceSummaryModelData oldParent);


    /**
     * Create a new model resource.
     *
     * @param model The model to create.
     * @param newParent The model's parent folder.
     */
    void create(ResourceSummaryModelData model,
                ResourceSummaryModelData newParent);


    /**
     * Remove a model resource.
     *
     * @param item The model to remove.
     */
    void delete(ResourceSummaryModelData item);
}
