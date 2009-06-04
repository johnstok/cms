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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;


/**
 * This selection listener calls an edit controllers submit method when the
 * associated button is clicked.
 *
 * @author Civic Computing Ltd.
 */
public class SubmitControllerSelectionListener
    extends
        SelectionListener<ButtonEvent> {

    private final EditController _controller;

    /**
     * Constructor.
     *
     * @param controller The controller to notify.
     */
    public SubmitControllerSelectionListener(final EditController controller) {
        _controller = controller;
    }


    /** {@inheritDoc} */
    @Override
    public void componentSelected(final ButtonEvent ce) {
        _controller.submit();
    }
}
