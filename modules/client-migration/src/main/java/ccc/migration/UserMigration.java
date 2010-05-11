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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.api.core.Group;
import ccc.api.core.Groups;
import ccc.api.core.PagedCollection;
import ccc.api.core.User;
import ccc.api.core.Users;
import ccc.api.exceptions.CCException;
import ccc.api.types.EmailAddress;


/**
 * Migrate users from CCC6.
 *
 * @author Civic Computing Ltd.
 */
public class UserMigration {
    private static final int MIN_PW_LENGTH = 6;
    private static Logger log = Logger.getLogger(Migrations.class);

    private final LegacyDBQueries _legacyQueries;
    private final Users _userCommands;
    private final Groups _groups;
    private Map<String, Group> _cachedGroups =
        new HashMap<String, Group>();

    /**
     * Constructor.
     *
     * @param legacyQueries The query API for CCC6.
     * @param userCommands User API implementation.
     */
    public UserMigration(final LegacyDBQueries legacyQueries,
                         final Users userCommands,
                         final Groups groups) {
        _legacyQueries = legacyQueries;
        _userCommands = userCommands;
        _groups = groups;
    }



    /**
     * Migrate all users.
     *
     * @throws CCException If an error occurs during migration.
     */
    void migrateUsers() throws CCException {

        final Map<Integer, User> mus = _legacyQueries.selectUsers();

        for (final Map.Entry<Integer, User> mu : mus.entrySet()) {
            try {
                // TODO: improve reporting
                final User ud = mu.getValue();

                final Set<String> roles =
                    _legacyQueries.selectRolesForUser(mu.getKey().intValue());
                final Set<UUID> groupList =
                    migrateGroups(roles, _cachedGroups, _groups);

                ud.setGroups(groupList);

                if (null == ud.getPassword()) {
                    log.warn(
                        "User: "+ud.getUsername() + " has null password.");
                } else if (ud.getPassword().equals(
                               ud.getUsername().toString())) {
                    log.warn("User: "+ud.getUsername()
                        +" has username as a password.");
                } else if (ud.getPassword().length() < MIN_PW_LENGTH) {
                    log.warn("User: "+ud.getUsername()
                        +" has password with less than 6 characters.");
                }

                correctEmail(ud);

                _userCommands.create(ud);
            } catch (final RuntimeException e) {
                log.warn(
                    "Failed creating user "+mu.getKey()+": "+e.getMessage(), e);
            }
        }
        log.info("Migrated users.");
    }


    private void correctEmail(final User ud) {
        if (!EmailAddress.isValidText(ud.getEmail())) {
            log.warn("Correcting invalid email: "+ud.getEmail());
            ud.setEmail("unknown@example.com");
        }
    }


    static Set<UUID> migrateGroups(final Collection<String> roles,
                                   final Map<String, Group> cachedGroups,
                                   final Groups groups) throws CCException {

        final Set<UUID> groupList = new HashSet<UUID>();

        for (final String role : roles) {
            if (cachedGroups.containsKey(role)) {
                groupList.add(cachedGroups.get(role).getId());

            } else { // Group not cached
                final PagedCollection<Group> gs = groups.list(role, 1, 999);
                if (0==gs.getTotalCount()) { // Doesn't exist.
                    final Group g = new Group();
                    g.setName(role);
                    final Group created = groups.create(g);
                    cachedGroups.put(role, created);
                    groupList.add(created.getId());
                } else {
                    final Group retrieved = gs.getElements().iterator().next();
                    cachedGroups.put(role, retrieved);
                    groupList.add(retrieved.getId());
                }
            }
        }
        return groupList;
    }
}
