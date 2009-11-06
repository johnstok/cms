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

import com.extjs.gxt.ui.client.data.ModelStringProvider;

/**
 * Provides icon URLs for each type of resource.
 *
 * @author Civic Computing Ltd.
 */
final class ResourceIconProvider
    implements
        ModelStringProvider<ResourceSummaryModelData> {

    /** {@inheritDoc} */
    public String getStringValue(final ResourceSummaryModelData model,
                                 final String property) {
        switch (model.getType()) {
            case FOLDER:
                return  ImagePaths.FOLDER;
            case PAGE:
                return ImagePaths.PAGE;
            case TEMPLATE:
                return ImagePaths.TEMPLATE;
            case ALIAS:
                return ImagePaths.ALIAS;
            case FILE:
                return ImagePaths.FILE;
            case SEARCH:
                return ImagePaths.SEARCH;
            default:
                return null;
        }
    }
}
