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
package ccc.migration.ccc6.handlers;

import static ccc.api.types.DBC.*;

import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * A SQL query that retrieves 'is major edit' information.
 *
 * @author Civic Computing Ltd.
 */
public class IsMajorEditSelector implements SqlQuery<Boolean> {

    /** {@inheritDoc} */
    @Override
    public Boolean handle(final ResultSet rs) throws SQLException {
        if (rs.next()) {
            final String isMajorEdit = rs.getString("IS_MAJOR_EDIT");
            require().toBeFalse(rs.next());
            if (isMajorEdit != null && isMajorEdit.equalsIgnoreCase("Y")) {
                return Boolean.valueOf(true);
            }
        }
        return Boolean.valueOf(false);

    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return Messages.getString("IsMajorEditSelector.sql"); //$NON-NLS-1$
    }

}
