/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;

import ccc.contentcreator.controls.PanelControl;


/**
 * API for all dialogs.
 *
 * @author Civic Computing Ltd.
 */
public interface ApplicationDialog {

    /**
     * Hide the dialog.
     */
    void hide();

    /**
     * Set the GUI for the dialog.
     *
     * @param control The control to use for the GUI.
     */
    void gui(PanelControl control);

    /**
     * TODO: Add a description of this method.
     *
     */
    void center();
}
