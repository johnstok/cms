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
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import ccc.domain.User;


/**
 * A helper class for sending email messages.
 *
 * @author Civic Computing Ltd.
 */
public class Mailer
    extends
        Authenticator {
    private static final Logger LOG = Logger.getLogger(Mailer.class);

    private final String _username;
    private final String _password;
    private final String _fromAddress;
    private final String _smtpHost;


    /**
     * Constructor.
     *
     * @param username The username for the SMTP server.
     * @param password The password for the SMTP server.
     * @param smtpHost The host name for the SMTP server.
     * @param fromAddress The 'from' address for email messages.
     */
    public Mailer(final String username,
                  final String password,
                  final String fromAddress,
                  final String smtpHost) {
        _username = username;
        _password = password;
        _fromAddress = fromAddress;
        _smtpHost = smtpHost;
    }


    /** {@inheritDoc} */
    @Override public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(_username, _password);
    }

    /**
     * Sends a plain text message to the specified recipient.
     *
     * @param recipient The user who will receive the email.
     * @param subject The email's subject.
     * @param message The email's body.
     *
     * @return True if the mail was sent successfully, false otherwise.
     */
    public boolean send(final User recipient,
                        final String subject,
                        final String message) {
        try {
            final boolean debug = false;

            // Transport properties.
            final Properties props = new Properties();
            props.put("mail.smtp.host", _smtpHost);
            props.put("mail.smtp.auth", "true");

            // Create the session.
            final Session session =
                Session.getDefaultInstance(props, this);
            session.setDebug(debug);

            // Create the message.
            final Message msg = new MimeMessage(session);

            // Set the to / from addresses.
            msg.setRecipient(
                Message.RecipientType.TO,
                new InternetAddress(recipient.email().toString()));
            msg.setFrom(
                new InternetAddress(_fromAddress));

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
}
