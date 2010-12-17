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
package ccc.client.gwt.widgets;

import ccc.api.core.ResourceSummary;
import ccc.client.core.ImagePaths;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelStringProvider;

/**
 * Provides icon URLs for each type of resource.
 *
 * @author Civic Computing Ltd.
 */
final class ResourceIconProvider
    implements
        ModelStringProvider<BeanModel> {

    /** {@inheritDoc} */
    public String getStringValue(final BeanModel model,
                                 final String property) {
        switch (model.<ResourceSummary>getBean().getType()) {
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
            case RANGE_FOLDER:
                return ImagePaths.FOLDER_GO;
            default:
                return null;
        }
    }
}
