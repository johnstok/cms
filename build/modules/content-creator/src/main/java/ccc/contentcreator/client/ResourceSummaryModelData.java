/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.data.BaseModelData;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceSummaryModelData
    extends
        BaseModelData {

    /**
     * Constructor.
     *
     * @param rs The folder summary to convert to model data.
     */
    public ResourceSummaryModelData(final ResourceSummary rs) {
        set("id", rs._id);
        set("name", rs._name);
        set("publishedBy", rs._publishedBy);
        set("title", rs._title);
        set("lockedBy", rs._lockedBy);
        set("type", rs._type);
    }
}
