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

import ccc.types.DBC;
import ccc.types.EmailAddress;


/**
 * JavaMail implementation of the {@link Mailer} interface.
 *
 * @author Civic Computing Ltd.
 */
public class JavaMailMailer
    extends
        Authenticator
    implements
        Mailer {

    private static final Logger LOG = Logger.getLogger(JavaMailMailer.class);

    private final Session _session;


    /**
     * Constructor.
     *
     * @param session The JavaMail session to use.
     */
    public JavaMailMailer(final Session session) {
        _session = DBC.require().notNull(session);
    }




    /** {@inheritDoc} */
    @Override
    public boolean send(final EmailAddress toAddress,
                        final EmailAddress fromAddress,
                        final String subject,
                        final String message) {
        try {

            // Create the message.
            final Message msg = new MimeMessage(_session);

            // Set the to / from addresses.
            msg.setRecipient(
                Message.RecipientType.TO,
                new InternetAddress(toAddress.getText()));
            msg.setFrom(
                new InternetAddress(fromAddress.getText()));

            // Set the content.
            msg.setSentDate(new Date());
            msg.setSubject(subject);
            msg.setContent(message, "text/plain");

            Transport.send(msg);

            LOG.info(
                "Sent mail."
                + "\n\tto: "+toAddress.getText()
                + "\n\tfrom: "+fromAddress.getText()
                + "\n\tsubject: "+subject
                + "\n\tmessage: "+message);

            return true;

        } catch (final AddressException e) {
            LOG.warn("Failed to send email.", e);
            return false;
        } catch (final MessagingException e) {
            LOG.warn("Failed to send email.", e);
            return false;
        }
    }
}
