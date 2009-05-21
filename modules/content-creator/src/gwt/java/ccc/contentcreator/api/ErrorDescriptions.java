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
public interface ErrorDescriptions extends Messages {

    /**
     * "The resource that you tried to update is not locked.".
     *
     * @return The message, in the appropriate locale.
     */
    @DefaultMessage("The resource that you tried to update is not locked.")
    String unlocked();

    /**
     * "{0}Your action could not be completed.{1}".
     *
     *
     * @param htmlPrefix HTML appended before the message.
     * @param htmlSuffix HTML appended after the message.
     * @return The message, in the appropriate locale.
     */
    @DefaultMessage("{0}Your action could not be completed.{1}")
    String couldNotComplete(String htmlPrefix, String htmlSuffix);

    /**
     * "Unknown error.".
     *
     * @return The message, in the appropriate locale.
     */
    @DefaultMessage("Unknown error.")
    String unknown();
}
