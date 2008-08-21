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
package ccc.view.contentcreator.dialogs;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
final class HidingClickListener implements ClickListener {

    private final AppDialog _dialog;

    /**
     * Constructor.
     *
     * @param dialog The dialog that will hidden.
     */
    public HidingClickListener(final AppDialog dialog) {
        _dialog = dialog;
    }

    /** {@inheritDoc} */
    public void onClick(final Widget sender) {
        _dialog.hide();
    }
}
