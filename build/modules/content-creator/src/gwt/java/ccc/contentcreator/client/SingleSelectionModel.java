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
public interface SingleSelectionModel<T extends ResourceSummaryModelData> {
    T tableSelection();
    T treeSelection();
    void update(T model);
    void move(T model, T newParent, final T oldParent);
    void create(T model, T newParent);
}
