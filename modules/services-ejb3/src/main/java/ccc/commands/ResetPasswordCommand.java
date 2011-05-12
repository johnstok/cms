/*-----------------------------------------------------------------------------
 * Copyright © 2011 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ccc.api.core.UserCriteria;
import ccc.api.exceptions.CCException;
import ccc.api.types.CommandType;
import ccc.api.types.SortOrder;
import ccc.domain.UserEntity;
import ccc.persistence.IRepositoryFactory;


/**
 * Command to reset password using a preset token.
 *
 * @author Civic Computing Ltd.
 */
public class ResetPasswordCommand  extends
        Command<Void> {

    private final String _password;
    private final String _token;


    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param userId The id of the user to update.
     */
    public ResetPasswordCommand(final IRepositoryFactory repoFactory,
                                final String password1,
                                final String token) {
        super(repoFactory);
        _password = password1;
        _token = token;
    }

    /** {@inheritDoc} */
    @Override
    public Void doExecute(final UserEntity actor,
                          final Date happenedOn) {

        final UserCriteria uc = new UserCriteria();
        uc.setMetadataKey(UserEntity.TOKEN_KEY);
        uc.setMetadataValue(_token);

        final List<UserEntity> userList = new ArrayList<UserEntity>();
        userList.addAll(getUsers().listUsers(uc, "name", SortOrder.ASC, 1, 10));

        if (userList == null || userList.isEmpty()) {
            throw new CCException("No user found with the token.");
        } else if (userList.size() > 1) {
            throw new CCException("Too many users found with the token.");
        }

        final UserEntity ue = userList.get(0);

        final Map<String, String> meta = ue.getMetadata();
        if(meta.get(UserEntity.TOKEN_EXPIRY_KEY) == null) {
            throw new CCException("User doesn't have expiry set for the token.");
        }
        final Date now = new Date();
        final Date expiry =
            new Date(Long.parseLong(meta.get(UserEntity.TOKEN_EXPIRY_KEY)));
        if (now.after(expiry)) {
            throw new CCException("Token has expired.");
        }

        meta.remove(UserEntity.TOKEN_KEY);
        meta.remove(UserEntity.TOKEN_EXPIRY_KEY);
        ue.addMetadata(meta);
        ue.setPassword(_password);

        auditUserCommand(actor, happenedOn, ue);
        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.USER_RESET_PASSWORD; }

}