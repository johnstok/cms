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
package ccc.client.gwt.views.gxt;

import ccc.api.core.API;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;


/**
 * About dialog for version information.
 *
 * @author Civic Computing Ltd.
 */
public class AboutDialog extends AbstractBaseDialog {

    /** ABOUT_WIDTH : int. */
    private static final int ABOUT_WIDTH = 220;
    private static final int ABOUT_HEIGHT = 150;
    private String _version = "unknown";
    private String _build = "unknown";
    private String _application = "unknown";

    /**
     * Constructor.
     *
     */
    public AboutDialog() {
        super(I18n.UI_CONSTANTS.about(), InternalServices.GLOBALS);

        setWidth(ABOUT_WIDTH);
        setMinWidth(ABOUT_WIDTH);
        setHeight(ABOUT_HEIGHT);

        _version =
            InternalServices.API.getProps().get(API.CCC_VERSION);
        _build =
            InternalServices.API.getProps().get(API.BUILD_NUMBER);
        _application =
            InternalServices.API.getProps().get(API.APPLICATION_NAME);

        writeAboutText();
    }

    private void writeAboutText() {

        final StringBuilder sb = new StringBuilder();
        sb.append("Civic Content Control<br/>");
        sb.append("Application: ");
        sb.append(_application);
        sb.append("<br/>");
        sb.append("Version: ");
        sb.append(_version);
        sb.append("<br/>");
        sb.append("Build: ");
        sb.append(_build);
        sb.append("<br/>");
        sb.append("<br/>");
        sb.append("(c) Civic Computing Limited 2008-2010.<br/>");
        sb.append("All rights reserved.");

        addText(sb.toString());
        layout();
    }
}