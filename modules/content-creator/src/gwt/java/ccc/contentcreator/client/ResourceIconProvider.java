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

    public String getStringValue(final ResourceSummaryModelData model,
                                 final String property) {
        switch (model.getType()) {
            case FOLDER:
                return "images/gxt/icons/folder.gif";
            case PAGE:
                return "images/icons/page.png";
            case TEMPLATE:
                return "images/icons/page_code.png";
            case ALIAS:
                return "images/icons/link.png";
            case FILE:
                return "images/icons/image.png";
            case SEARCH:
                return "images/icons/magnifier.png";
            default:
                return null;
        }
    }
}