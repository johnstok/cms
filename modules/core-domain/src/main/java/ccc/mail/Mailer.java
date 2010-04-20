/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.mail;

import java.util.Date;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.types.DBC;
import ccc.types.EmailAddress;


/**
 * A helper class for sending email messages.
 *
 * @author Civic Computing Ltd.
 */
public class Mailer
    extends
        Authenticator {
    private static final Logger LOG = Logger.getLogger(Mailer.class);
    private final Session _session;

    /**
     * Constructor.
     *
     * @param location The location in registry, for example java:/mail/appname
     */
    public Mailer(final String location) {
        final Registry r = new JNDI();
        final Session session = r.get(location);
        _session = DBC.require().notNull(session);
    }

    /**
     * Sends a plain text message to the specified recipient.
     *
     * @param fromAddress The address from which the email will be sent.
     * @param toAddress The address to which the email will be sent.
     * @param subject The email's subject.
     * @param message The email's body.
     *
     * @return True if the mail was sent successfully, false otherwise.
     */
    public boolean send(final EmailAddress fromAddress,
                        final EmailAddress toAddress,
                        final String subject,
                        final String message) {
        try {

            // Create the message.
            final Message msg = new MimeMessage(_session);

            // Set the to / from addresses.
            msg.setRecipient(
                Message.RecipientType.TO,
                new InternetAddress(toAddress.getText()));
            msg.setFrom(new InternetAddress(fromAddress.getText()));

            // Set the content.
            msg.setSentDate(new Date());
            msg.setSubject(subject);
            msg.setContent(message, "text/plain");

            Transport.send(msg);

            return true;

        } catch (final AddressException e) {
            LOG.warn("Failed to send email.", e);
            return false;
        } catch (final MessagingException e) {
            LOG.warn("Failed to send email.", e);
            return false;
        }
    }
    
    /**
     * Enable debugging for this mailer.
     *
     * @param debug True if debugging should be enabled; false otherwise.
     */
    public void setDebug(final boolean debug) {
        _session.setDebug(debug);
    }

    
}
