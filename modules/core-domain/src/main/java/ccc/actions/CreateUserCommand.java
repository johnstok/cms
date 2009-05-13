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
package ccc.actions;

import java.util.Date;

import ccc.commons.EmailAddress;
import ccc.domain.Password;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.services.api.UserDelta;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CreateUserCommand {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public CreateUserCommand(final Dao dao, final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }

    /**
     * Create new user.
     *
     * @param delta The properties for the new user.
     * @param password The password to be used for the user.
     * @return Persisted user.
     */
    public User execute(final User actor,
                        final Date happenedOn,
                        final UserDelta delta,
                        final String password) {
        final User user = new User(delta.getUsername().toString());
        user.email(new EmailAddress(delta.getEmail()));
        user.roles(delta.getRoles());
        _dao.create(user);

        final Password defaultPassword = new Password(user, password);
        _dao.create(defaultPassword);

        _audit.recordUserCreate(user, actor, happenedOn);

        return user;
    }
}
