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
import ccc.contentcreator.core.ImagePaths;

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
