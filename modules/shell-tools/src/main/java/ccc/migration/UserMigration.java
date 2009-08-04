/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.migration;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ccc.api.UserSummary;
import ccc.commands.CommandFailedException;
import ccc.services.Commands;


/**
 * Migrate users from CCC6.
 *
 * @author Civic Computing Ltd.
 */
public class UserMigration {
    private static final int MIN_PW_LENGTH = 6;
    private static Logger log = Logger.getLogger(Migrations.class);

    private final LegacyDBQueries _legacyQueries;
    private final Commands _commands;
    private final Map<Integer, UserSummary> _users =
        new HashMap<Integer, UserSummary>();



    /**
     * Constructor.
     *
     * @param legacyQueries The query API for CCC6.
     * @param commands The command API for the new system.
     */
    public UserMigration(final LegacyDBQueries legacyQueries,
                         final Commands commands) {
        _legacyQueries = legacyQueries;
        _commands = commands;
    }



    /**
     * Migrate all users.
     *
     * @throws CommandFailedException If an error occurs during migration.
     */
    void migrateUsers() throws CommandFailedException {
        final Map<Integer, ExistingUser> mus = _legacyQueries.selectUsers();
        for (final Map.Entry<Integer, ExistingUser> mu : mus.entrySet()) {
            try {
                // TODO: improve reporting
                final ExistingUser ud = mu.getValue();

                if (null == ud.getPassword()) {
                    log.warn(
                        "User: "+ud.getUser().getUsername()
                        +" has null password.");
                } else if (ud.getPassword().equals(
                               ud.getUser().getUsername().toString())) {
                    log.warn("User: "+ud.getUser().getUsername()
                        +" has username as a password.");
                } else if (ud.getPassword().length() < MIN_PW_LENGTH) {
                    log.warn("User: "+ud.getUser().getUsername()
                        +" has password with less than 6 characters.");
                }

                final UserSummary u =
                    _commands.createUser(ud.getUser(), ud.getPassword());
                _users.put(mu.getKey(), u);
            } catch (final RuntimeException e) {
                log.warn(
                    "Failed to create user "+mu.getKey()+": "+e.getMessage());
            }
        }
        log.info("Migrated users.");
    }



    /**
     * Lookup the new user for a CCC6 user id.
     *
     * @param actor The CCC6 user id.
     *
     * @return The corresponding user in the new system.
     */
    UserSummary getUser(final int actor) {
        return _users.get(Integer.valueOf(actor));
    }
}
