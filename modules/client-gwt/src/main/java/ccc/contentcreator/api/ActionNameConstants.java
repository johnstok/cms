/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
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
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.api;

import com.google.gwt.i18n.client.Constants;

/**
 * Constants for i18n.
 *
 * @author Civic Computing Ltd
 */
public interface ActionNameConstants extends Constants {

    /**
     * "Unknown action".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Unknown action")
    String unknownAction();

    /**
     * "Check unique resource name".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Check unique resource name")
    String checkUniqueResourceName();

    /**
     * "Validate page fields".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Validate page fields")
    String validatePageFields();

    /**
     * "Read properties".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Read properties")
    String readProperties();

    /**
     * "Check unique username".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Check unique username")
    String checkUniqueUsername();

    /**
     * "Check unique template name".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Check unique template name")
    String checkUniqueTemplateName();

    /**
     * "View actions".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("View actions")
    String viewActions();

    /**
     * "View users".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("View users")
    String viewUsers();

    /**
     * "Internal action".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Internal action")
    String internalAction();

    /**
     * "Load data".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Load data")
    String loadData();
}
