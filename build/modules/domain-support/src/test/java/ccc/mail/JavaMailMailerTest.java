/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;

import junit.framework.TestCase;

import org.jvnet.mock_javamail.Mailbox;

import ccc.plugins.mail.JavaMailMailer;
import ccc.plugins.mail.Mailer;
import ccc.types.EmailAddress;


/**
 * Tests for the {@link JavaMailMailer} class.
 *
 * @author Civic Computing Ltd.
 */
public class JavaMailMailerTest
    extends
        TestCase {

    /**
     * Test.
     * @throws Exception If the test fails.
     */
    public void testSuccessfulSend() throws Exception {

        // ARRANGE
        final Session session = Session.getInstance(new Properties());
        final Mailer m = new JavaMailMailer(session);

        // ACT
        m.send(
            new EmailAddress("to@example.com"),
            new EmailAddress("from@example.com"),
            "subject",
            "message");


        // ASSERT
        final List<Message> inbox = Mailbox.get("to@example.com");
        assertEquals(1, inbox.size());
        final Message msg = inbox.get(0);
        assertEquals("subject", msg.getSubject());
        assertEquals("message", (String) msg.getContent());
        assertEquals("from@example.com", msg.getFrom()[0].toString());
    }


    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Mailbox.clearAll();
    }


}
