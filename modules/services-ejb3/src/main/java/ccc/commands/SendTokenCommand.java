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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Session;

import ccc.api.core.UserCriteria;
import ccc.api.types.CommandType;
import ccc.api.types.EmailAddress;
import ccc.api.types.SortOrder;
import ccc.domain.LogEntry;
import ccc.domain.UserEntity;
import ccc.persistence.IRepositoryFactory;
import ccc.plugins.mail.Mailer;
import ccc.plugins.mail.javamail.JavaMailMailer;
import ccc.plugins.mail.javamail.PropertiesAuthenticator;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * Assigns token to an user for password reseting.
 *
 * @author Civic Computing Ltd.
 */
public class SendTokenCommand  extends
        Command<Void> {

    private final String _username;


    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param userId The id of the user to update.
     */
    public SendTokenCommand(final IRepositoryFactory repoFactory,
                             final String username) {
        super(repoFactory);
        _username = username;
    }

    /** {@inheritDoc} */
    @Override
    public Void doExecute(final UserEntity actor,
                          final Date happenedOn) {

        UserCriteria uc = new UserCriteria();
        uc.setUsername(_username);
        final List<UserEntity> userList = new ArrayList<UserEntity>();
        userList.addAll(getUsers().listUsers(uc, "name", SortOrder.ASC, 1, 10));
        
        if (userList == null || userList.isEmpty() || userList.size() > 1) {
            throw new RuntimeException("problem with user search");
        }
        
        UserEntity ue = userList.get(0);
        Map<String, String> meta = ue.getMetadata();
        String token = UUID.randomUUID().toString();
        meta.put(UserEntity.TOKEN_KEY, token);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 1);
        meta.put(UserEntity.TOKEN_EXPIRY_KEY, ""+cal.getTime().getTime());
        ue.addMetadata(meta);
        
        
        //  send email to user's email address

        final Properties config =
            ccc.commons.Resources.readIntoProps("mail.properties");
        Session s = 
            Session.getInstance(config, new PropertiesAuthenticator(config));
        Mailer m = new JavaMailMailer(s);
        // FIXME configure from address, subject and message
        m.send(ue.getEmail(), new EmailAddress("cc7@civicuk.com"), "pw", token);

        getAudit().record(
            new LogEntry(
                actor,
                getType(),
                happenedOn,
                ue.getId(),
                new JsonImpl(ue).getDetail()));
        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.USER_SET_TOKEN; }
    
}