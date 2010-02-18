/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.client;

import ccc.contentcreator.core.EditController;

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
