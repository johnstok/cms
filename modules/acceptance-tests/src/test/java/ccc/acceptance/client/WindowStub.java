/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.acceptance.client;

import java.util.ArrayList;
import java.util.List;

import ccc.client.core.Window;


/**
 * Stub implementation of the {@link Window} API.
 *
 * @author Civic Computing Ltd.
 */
public class WindowStub
    implements
        Window {

    private final List<String> _alerts = new ArrayList<String>();


    /** {@inheritDoc} */
    @Override
    public void alert(final String string) {
        _alerts.add(string);
    }


    /** {@inheritDoc} */
    @Override
    public boolean confirm(final String string) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void disableExitConfirmation() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void enableExitConfirmation() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public String getParameter(final String string) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void redirectTo(final String relativeURL) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void refresh() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /**
     * Accessor.
     *
     * @return The most recent alert, as a string.
     */
    public String getLatestAlert() { return _alerts.get(_alerts.size()-1); }
}
