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
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface SingleSelectionModel {
    ResourceSummaryModelData tableSelection();
    ResourceSummaryModelData treeSelection();
    void update(ResourceSummaryModelData model);
    void move(ResourceSummaryModelData model,
              ResourceSummaryModelData newParent,
              ResourceSummaryModelData oldParent);
    void create(ResourceSummaryModelData model,
                ResourceSummaryModelData newParent);
}
