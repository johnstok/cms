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
package ccc.migration;

import java.util.Date;


/**
 * A file from CCC6.
 *
 * @author Civic Computing Ltd.
 */
public class LegacyFile {
    private final String _title;
    private final String _description;
    private final Date _lastUpdate;

    /**
     * Constructor.
     *
     * @param title The file's title.
     * @param description The file's description.
     * @param lastUpdate The file's last update.
     */
    public LegacyFile(final String title,
                      final String description,
                      final Date lastUpdate) {
        _title = title;
        _description = description;
        _lastUpdate =
            (lastUpdate != null) ? new Date(lastUpdate.getTime()) : null;
    }


    /**
     * Accessor.
     *
     * @return Returns the title.
     */
    String getTitle() {
        return _title;
    }


    /**
     * Accessor.
     *
     * @return Returns the description.
     */
    String getDescription() {
        return _description;
    }

    /**
     * Accessor.
     *
     * @return Returns the last update.
     */
    Date getLastUpdate() {
        return _lastUpdate;
    }
}
