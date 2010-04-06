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
public final class ResourceRolesSelector
    implements
        SqlQuery<Collection<String>> {

    /** {@inheritDoc} */
    @Override
    public Collection<String> handle(final ResultSet rs) throws SQLException {
        final Collection<String> resultList =
            new HashSet<String>();

        while (rs.next()) {
            final String profile = rs.getString("profile_name");
            resultList.add(profile);
        }
        return resultList;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "SELECT DISTINCT p.profile_name "
            + "FROM "
            + "perm_attributes a, profiles p "
            + "WHERE "
            + "a.type='PROFILE' AND "
            + "to_number(a.owner_id)=p.profile_id AND "
            + "a.attribute=to_char(?) AND "
            + "(a.permission_name='folder_access' "
            + "OR a.permission_name='content_view')";
    }
}
