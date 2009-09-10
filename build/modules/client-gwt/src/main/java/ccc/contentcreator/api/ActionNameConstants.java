/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
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