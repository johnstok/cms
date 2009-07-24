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

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;

/**
 * Adapter class - wires an action to a GWT selection listener.
 *
 * @author Civic Computing Ltd.
 */
public final class SelectionListenerAction
    extends
        SelectionListener<ComponentEvent> {

    private final Action _action;

    /**
     * Constructor.
     *
     * @param action The Action.
     */
    public SelectionListenerAction(final Action action) {
        _action = action;
    }

    /** {@inheritDoc} */
    @Override public void componentSelected(final ComponentEvent ce) {
        _action.execute();
    }
}
