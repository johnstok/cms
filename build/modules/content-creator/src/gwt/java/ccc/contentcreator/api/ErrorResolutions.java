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

import com.google.gwt.i18n.client.Messages;


/**
 * Messages for i18n.
 *
 * @author Civic Computing Ltd.
 */
public interface ErrorResolutions extends Messages {

    /**
     * "Lock the selected resource and then try to perform the action again.".
     *
     * @return The message, in the appropriate locale.
     */
    @DefaultMessage(
        "Lock the selected resource and then try to perform the action again.")
    String unlocked();

    /**
     * "Contact your system administrator.".
     *
     * @return The message, in the appropriate locale.
     */
    @DefaultMessage("Contact your system administrator.")
    String contactSysAdmin();
}
