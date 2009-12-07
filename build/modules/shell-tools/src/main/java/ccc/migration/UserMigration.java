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

import java.util.Map;

import org.apache.log4j.Logger;

import ccc.rest.RestException;
import ccc.rest.Users;
import ccc.rest.dto.UserDto;


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

    /**
     * Constructor.
     *
     * @param legacyQueries The query API for CCC6.
     * @param userCommands User API implementation.
     */
    public UserMigration(final LegacyDBQueries legacyQueries,
                         final Users userCommands) {
        _legacyQueries = legacyQueries;
        _userCommands = userCommands;
    }



    /**
     * Migrate all users.
     *
     * @throws RestException If an error occurs during migration.
     */
    void migrateUsers() throws RestException {
        final Map<Integer, UserDto> mus = _legacyQueries.selectUsers();
        for (final Map.Entry<Integer, UserDto> mu : mus.entrySet()) {
            try {
                // TODO: improve reporting
                final UserDto ud = mu.getValue();

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

                _userCommands.createUser(ud);
            } catch (final RuntimeException e) {
                log.warn(
                    "Failed to create user "+mu.getKey()+": "+e.getMessage());
            }
        }
        log.info("Migrated users.");
    }

}
