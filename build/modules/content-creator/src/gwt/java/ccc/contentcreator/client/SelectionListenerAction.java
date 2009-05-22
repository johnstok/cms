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

import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
final class SelectionListenerAction
    extends
        SelectionListener<MenuEvent> {

    private final Action _action;

    /**
     * Constructor.
     *
     * @param action The Action.
     */
    SelectionListenerAction(final Action action) {
        _action = action;
    }

    /** {@inheritDoc} */
    @Override public void componentSelected(final MenuEvent ce) {
        _action.execute();
    }
}
