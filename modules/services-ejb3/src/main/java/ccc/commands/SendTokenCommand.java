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
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import ccc.api.core.File;
import ccc.api.core.UserCriteria;
import ccc.api.exceptions.CCException;
import ccc.api.exceptions.UsernameNotFoundException;
import ccc.api.synchronous.MemoryServiceLocator;
import ccc.api.types.CommandType;
import ccc.api.types.EmailAddress;
import ccc.api.types.MimeType;
import ccc.api.types.ResourcePath;
import ccc.api.types.ResourceType;
import ccc.api.types.SortOrder;
import ccc.domain.FileEntity;
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;
import ccc.persistence.IRepositoryFactory;
import ccc.plugins.PluginFactory;
import ccc.plugins.mail.Mailer;
import ccc.plugins.scripting.Context;
import ccc.plugins.scripting.ProcessingException;
import ccc.plugins.scripting.Script;
import ccc.plugins.scripting.TextProcessor;


/**
 * Assigns token to an user and sends an email for password reseting.
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

        final String token = UUID.randomUUID().toString();
        final UserEntity ue = setTokenForUser(token);

        sendResetTokenEmail(token, ue.getEmail());
        auditUserCommand(actor, happenedOn, ue);
        return null;
    }

    private void sendResetTokenEmail(final String token, final EmailAddress email) {

        //  send email to user's email address
        String fromAddress = "noreply@civicuk.com";
        String subject = "Password reset";
        String mailTemplate = "token: "+token;
        ResourceEntity resource = null;
        try {
            resource = getRepository().lookup(
                new ResourcePath("/assets/scripts/reset_email.txt"));
        } catch (final EntityNotFoundException e) {
            // no-op
        }

        final Mailer m = new PluginFactory().createMailer();
        // Get content from CMS.
        if (resource != null && resource.getType() == ResourceType.FILE) {
            final Map<String, String> meta = resource.getMetadata();
            if (meta.containsKey("fromAddress")) {
                fromAddress = meta.get("fromAddress");
            }
            if (meta.containsKey("subject")) {
                subject = meta.get("subject");
            }
            final FileEntity file = getRepository().find(FileEntity.class, resource.getId());
            final File f = file.mapTextFile(getData());
            mailTemplate = f.getContent();
        }
        final Script s =
            new Script(mailTemplate, "Mail template", MimeType.VELOCITY);
        final TextProcessor velocityProcessor = new PluginFactory().createTemplating();
        final Context context = new Context();
        context.add("services", new MemoryServiceLocator());
        context.add("token", token);

        String content;
        try {
            content = velocityProcessor.render(s, context);
        } catch (final ProcessingException e) {
            throw new CCException("Token sending failed", e);
        }
        m.send(email,
            new EmailAddress(fromAddress),
            subject,
            content);
    }

    private UserEntity setTokenForUser(final String token) {

        final UserCriteria uc = new UserCriteria();
        uc.setUsername(_username);
        final List<UserEntity> userList = new ArrayList<UserEntity>();
        userList.addAll(getUsers().listUsers(uc, "name", SortOrder.ASC, 1, 10));

        if (userList == null || userList.isEmpty() || userList.size() > 1) {
            throw new UsernameNotFoundException(_username);
        }

        final UserEntity ue = userList.get(0);
        final Map<String, String> meta = ue.getMetadata();
        meta.put(UserEntity.TOKEN_KEY, token);
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 1);
        meta.put(UserEntity.TOKEN_EXPIRY_KEY, ""+cal.getTime().getTime());
        ue.addMetadata(meta);
        return ue;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.USER_SET_TOKEN; }

}