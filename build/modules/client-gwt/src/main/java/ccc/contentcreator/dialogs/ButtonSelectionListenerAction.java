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
package ccc.contentcreator.dialogs;

import ccc.contentcreator.client.Action;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;

/**
 * Adapter class - wires an action to a GWT selection listener.
 *
 * @author Civic Computing Ltd.
 */
final class ButtonSelectionListenerAction
extends
SelectionListener<ButtonEvent> {

    private final Action _action;

    /**
     * Constructor.
     *
     * @param action The Action.
     */
    ButtonSelectionListenerAction(final Action action) {
        _action = action;
    }

    /** {@inheritDoc} */
    @Override public void componentSelected(final ButtonEvent ce) {
        _action.execute();
    }
}
