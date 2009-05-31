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

import ccc.api.CommandFailedException;
import ccc.api.Commands;
import ccc.api.UserSummary;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UserMigration {
    private static Logger log = Logger.getLogger(Migrations.class);

    private final LegacyDBQueries _legacyQueries;
    private final Commands _commands;
    private final Map<Integer, UserSummary> _users =
        new HashMap<Integer, UserSummary>();



    /**
     * Constructor.
     *
     * @param legacyQueries
     * @param commands
     */
    public UserMigration(final LegacyDBQueries legacyQueries, final Commands commands) {
        _legacyQueries = legacyQueries;
        _commands = commands;
    }



    void migrateUsers() throws CommandFailedException {
        final Map<Integer, ExistingUser> mus = _legacyQueries.selectUsers();
        for (final Map.Entry<Integer, ExistingUser> mu : mus.entrySet()) {
            try {
                // TODO: improve reporting
                final ExistingUser ud = mu.getValue();

//                if (null == ud._password) {
//                    log.warn(
//                        "User: "+ud._user.getUsername()+" has null password.");
//                } else if (ud._password.equals(
//                               ud._user.getUsername().toString())) {
//                    log.warn("User: "+ud._user.getUsername()
//                        +" has username as a password.");
//                } else if (ud._password.length() < MIN_PW_LENGTH) {
//                    log.warn("User: "+ud._user.getUsername()
//                        +" has password with less than 6 characters.");
//                }

                final UserSummary u =
                    _commands.createUser(ud._user, ud._password);
                _users.put(mu.getKey(), u);
            } catch (final RuntimeException e) {
                log.warn(
                    "Failed to create user "+mu.getKey()+": "+e.getMessage());
            }
        }
        log.info("Migrated users.");
    }



    /**
     * TODO: Add a description for this method.
     *
     * @param actor
     * @return
     */
    UserSummary getUser(final int actor) {
        return _users.get(actor);
    }
}
