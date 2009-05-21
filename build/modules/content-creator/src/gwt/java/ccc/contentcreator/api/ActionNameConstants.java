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
}
