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

import ccc.services.api.FolderSummary;

import com.extjs.gxt.ui.client.data.ModelData;


/**
 * A {@link ModelData} implementation for binding a {@link FolderSummary}.
 *
 * @author Civic Computing Ltd.
 */
public class FolderSummaryModelData
    extends ResourceSummaryModelData {

    /**
     * Constructor.
     *
     * @param fs The folder summary to convert to model data.
     */
    public FolderSummaryModelData(final FolderSummary fs) {
        super(fs);
        set("folderCount", Integer.valueOf(fs._folderCount));
    }

}
