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

import com.extjs.gxt.ui.client.data.ModelData;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface SingleSelectionModel {
    ModelData tableSelection();
    ModelData treeSelection();
    void update(ModelData model);
    void move(ModelData model, ModelData newParent, final ModelData oldParent);
    void create(ModelData model, ModelData newParent);
}
