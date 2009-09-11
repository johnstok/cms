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
}
