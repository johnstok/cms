/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
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


/**
 * A helper class for sending email messages.
 *
 * @author Civic Computing Ltd.
 */
public class Mailer
    extends
        Authenticator {
    private static final Logger LOG = Logger.getLogger(Mailer.class);


    /**
     * Sends a plain text message to the specified recipient.
     *
     * @param location The location in registry, for example java:/mail/appname
     * @param toAddress The address to which the email will be sent.
     * @param subject The email's subject.
     * @param message The email's body.
     *
     * @return True if the mail was sent successfully, false otherwise.
     */
    public boolean send(final String location,
                        final String toAddress,
                        final String subject,
                        final String message) {
        try {

            // Create the session.
            final Registry r = new JNDI();
            final Session session = r.get(location);

            // Create the message.
            final Message msg = new MimeMessage(session);

            // Set the to / from addresses.
            msg.setRecipient(
                Message.RecipientType.TO, new InternetAddress(toAddress));

            // Set the content.
            msg.setSentDate(new Date());
            msg.setFrom();
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
}
