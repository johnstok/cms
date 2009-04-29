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
public interface UIMessages extends Messages {

    /**
     * "User with username ''{0}'' already exists.".
     *
     * @param username The param to display.
     * @return The constant, in the appropriate locale.
     */
    @DefaultMessage("User with username ''{0}'' already exists.")
    String userWithUsernameAlreadyExists(String username);

    /**
     * "A resource with name ''{0}'' already exists in the parent folder.".
     *
     * @param name The param to display.
     * @return The constant, in the appropriate locale.
     */
    @DefaultMessage("A resource with name ''{0}'' already exists in the parent folder.")
    String resourceWithNameAlreadyExistsInTheParentFolder(String name);

    /**
     * "A resource with name ''{0}'' already exists in this folder.".
     *
     * @param name The param to display.
     * @return The constant, in the appropriate locale.
     */
    @DefaultMessage("A resource with name ''{0}'' already exists in this folder.")
    String resourceWithNameAlreadyExistsInThisFolder(String name);
}
