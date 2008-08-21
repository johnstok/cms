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
package ccc.view.contentcreator.callbacks;

import ccc.view.contentcreator.dialogs.ApplicationDialog;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class DisposingClickListener implements ClickListener {

    private final ApplicationDialog _dialog;

    /**
     * Constructor.
     *
     * @param dialog The dialog that will hidden.
     */
    public DisposingClickListener(final ApplicationDialog dialog) {
        _dialog = dialog;
    }

    /** {@inheritDoc} */
    public void onClick(final Widget sender) {
        _dialog.hide();
    }
}
