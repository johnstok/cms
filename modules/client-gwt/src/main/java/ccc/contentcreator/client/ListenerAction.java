/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.client;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
final class ListenerAction
    implements
        Listener<ComponentEvent> {

    private final Action _action;

    /**
     * Constructor.
     *
     * @param action The Action.
     */
    ListenerAction(final Action action) {
        _action = action;
    }

    /** {@inheritDoc} */
    public void handleEvent(final ComponentEvent be) {
        _action.execute();
    }
}
