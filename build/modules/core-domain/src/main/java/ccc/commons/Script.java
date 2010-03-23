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
 * Revision      $Rev: 2564 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-03-22 14:09:24 +0000 (Mon, 22 Mar 2010) $
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commons;

import ccc.types.DBC;


/**
 * A text-based script executed by a {@link TextProcessor}.
 *
 * @author Civic Computing Ltd.
 */
public class Script {

    private final String _body;
    private final String _title;


    /**
     * Constructor.
     *
     * @param body The script's body.
     * @param title The script's title.
     */
    public Script(final String body, final String title) {
        _body = DBC.require().notNull(body);
        _title = DBC.require().notEmpty(title);
    }


    /**
     * Accessor.
     *
     * @return Returns the body.
     */
    public final String getBody() {
        return _body;
    }


    /**
     * Accessor.
     *
     * @return Returns the title.
     */
    public final String getTitle() {
        return _title;
    }
}
