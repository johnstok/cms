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
                return "static/gxt/2.0.4/resources/images/gxt/icons/folder.gif";
            case PAGE:
                return "static/images/icons/page.gif";
            case TEMPLATE:
                return "static/images/icons/page_code.gif";
            case ALIAS:
                return "static/images/icons/link.gif";
            case FILE:
                return "static/images/icons/image.gif";
            case SEARCH:
                return "static/images/icons/magnifier.gif";
            default:
                return null;
        }
    }
}
