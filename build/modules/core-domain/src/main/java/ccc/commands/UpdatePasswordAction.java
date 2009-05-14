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
package ccc.commands;

import static ccc.services.QueryNames.*;

import java.util.Date;
import java.util.UUID;

import ccc.domain.Password;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UpdatePasswordAction {

    private final Dao      _dao;
    private final AuditLog _audit;

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public UpdatePasswordAction(final Dao dao, final AuditLog audit) {
        _dao = dao;
        _audit = audit;
    }


    /**
     * Update a user's password.
     *
     * @param userId The user's id.
     * @param password The new password.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID userId,
                        final String password) {
        final Password p =
                _dao.find(PASSWORD_FOR_USER, Password.class, userId);
        p.password(password);

        _audit.recordUserChangePassword(null, actor, happenedOn); // FIXME: Broken.
    }
}
