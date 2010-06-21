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
package ccc.client.i18n;


/**
 * Constants for i18n.
 *
 * @author Civic Computing Ltd
 */
public interface ActionNameConstants {

    /**
     * "Unknown action".
     *
     * @return The constant, in the appropriate locale.
     */
    @Deprecated
    String unknownAction();

    /**
     * "Check unique resource name".
     *
     * @return The constant, in the appropriate locale.
     */
    String checkUniqueResourceName();

    /**
     * "Validate page fields".
     *
     * @return The constant, in the appropriate locale.
     */
    String validatePageFields();

    /**
     * "Read properties".
     *
     * @return The constant, in the appropriate locale.
     */
    String readProperties();

    /**
     * "Check unique username".
     *
     * @return The constant, in the appropriate locale.
     */
    String checkUniqueUsername();

    /**
     * "Check unique template name".
     *
     * @return The constant, in the appropriate locale.
     */
    String checkUniqueTemplateName();

    /**
     * "View actions".
     *
     * @return The constant, in the appropriate locale.
     */
    String viewActions();

    /**
     * "View users".
     *
     * @return The constant, in the appropriate locale.
     */
    String viewUsers();

    /**
     * "Internal action".
     *
     * @return The constant, in the appropriate locale.
     */
    @Deprecated
    String internalAction();

    /**
     * "Load data".
     *
     * @return The constant, in the appropriate locale.
     */
    String loadData();

    /**
     * "View comments".
     *
     * @return The constant, in the appropriate locale.
     */
    String viewComments();
}
