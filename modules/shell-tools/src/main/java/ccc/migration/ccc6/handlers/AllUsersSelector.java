package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import ccc.migration.LegacyDBQueries;
import ccc.migration.MigrationException;
import ccc.rest.dto.UserDto;
import ccc.types.Username;

/**
 * A SQL query to export all users from CCC6.
 *
 * @author Civic Computing Ltd.
 */
public final class AllUsersSelector
    implements
        SqlQuery<Map<Integer, UserDto>> {

    private static Logger log = Logger.getLogger(AllUsersSelector.class);

    private final LegacyDBQueries _legacyDBQueries;

    /**
     * Constructor.
     *
     * @param legacyDBQueries A query class to perform sub-queries.
     */
    public AllUsersSelector(final LegacyDBQueries legacyDBQueries) {
        _legacyDBQueries = legacyDBQueries;
    }

    /** {@inheritDoc} */
    @Override public Map<Integer, UserDto> handle(final ResultSet rs)
                                                           throws SQLException {
        final Map<Integer, UserDto> resultList =
            new HashMap<Integer, UserDto>();
        while (rs.next()) {
            final String userName = rs.getString("user_name");
            final String password = rs.getString("user_passwd");
            final int userId = rs.getInt("user_id");
            try {
                final Map<String, String> metamap =
                    _legacyDBQueries.selectMetadataForUser(userId);


                String email = "";
                if (metamap.containsKey("Email address")) {
                    email = metamap.get("Email address");
                } else if (metamap.containsKey("Email")) {
                    email = metamap.get("Email");
                }

                final Set<String> roles =
                    _legacyDBQueries.selectRolesForUser(userId);
                roles.add("USER_"+userName);
                final UserDto user =
                    new UserDto(
                        email,
                        new Username(userName),
                        roles,
                        metamap,
                        password);
                resultList.put(Integer.valueOf(userId), user);
            } catch (final MigrationException e) {
                log.warn("Error selecting user: "+e.getMessage());
            }
        }
        return resultList;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return "SELECT user_id, user_name, user_passwd, name FROM users";
    }
}
