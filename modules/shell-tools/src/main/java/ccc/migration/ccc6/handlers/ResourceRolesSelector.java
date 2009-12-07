/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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
            + "OR a.permission_name='content_view')"
            + " UNION "
            + "select distinct 'USER_'||u.user_name "
            + "from perm_attributes a, users u "
            + "where a.attribute=to_char(?) "
            + "and user_id=owner_id "
            + "and type='USER'"
            + "and (a.permission_name='folder_access' "
            + "OR a.permission_name='content_view')";
    }
}
