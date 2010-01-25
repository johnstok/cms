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
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Selects the roles that will be applied to a resource.
 *
 * @author Civic Computing Ltd.
 */
public final class ResourceUsersSelector
    implements
        SqlQuery<Collection<Integer>> {

    /** {@inheritDoc} */
    @Override
    public Collection<Integer> handle(final ResultSet rs) throws SQLException {
        final Collection<Integer> resultList =
            new HashSet<Integer>();

        while (rs.next()) {
            final int userId = rs.getInt("user_id");
            resultList.add(Integer.valueOf(userId));
        }
        return resultList;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "select distinct u.user_id "
            + "from perm_attributes a, users u "
            + "where a.attribute=to_char(?) "
            + "and user_id=owner_id "
            + "and type='USER'"
            + "and (a.permission_name='folder_access' "
            + "OR a.permission_name='content_view')";
    }
}
