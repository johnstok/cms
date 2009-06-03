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
package ccc.contentcreator.api;

import com.google.gwt.i18n.client.ConstantsWithLookup;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface ActionStatusConstants extends ConstantsWithLookup {

    /**
     * "Scheduled".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Scheduled")
    String Scheduled();

    /**
     * "Complete".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Complete")
    String Complete();

    /**
     * "Failed".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Failed")
    String Failed();

    /**
     * "Cancelled".
     *
     * @return The constant, in the appropriate locale.
     */
    @DefaultStringValue("Cancelled")
    String Cancelled();

}
