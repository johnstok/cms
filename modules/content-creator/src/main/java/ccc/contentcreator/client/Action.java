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
package ccc.contentcreator.client;

import ccc.contentcreator.api.ActionNameConstants;
import ccc.contentcreator.api.UIConstants;

import com.google.gwt.core.client.GWT;


/**
 * A GUI action.
 *
 * @author Civic Computing Ltd.
 */
public interface Action {
    /** USER_ACTIONS : ActionNameConstants. */
    ActionNameConstants USER_ACTIONS =
        GWT.create(ActionNameConstants.class);
    /** UI_CONSTANTS : UIConstants. */
    UIConstants UI_CONSTANTS =
        GWT.create(UIConstants.class);

    /**
     * Perform the action.
     */
    void execute();
}
