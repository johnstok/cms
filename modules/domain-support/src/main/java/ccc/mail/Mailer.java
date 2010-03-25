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

import ccc.types.EmailAddress;

/**
 * API for sending email.
 *
 * @author Civic Computing Ltd.
 */
public interface Mailer {

    String NAME = "imailer";

    /**
     * Sends a plain text message to the specified recipient.
     *
     * @param toAddress The address to which the email will be sent.
     * @param fromAddress The address from which the email will be sent.
     * @param subject The email's subject.
     * @param message The email's body.
     *
     * @return True if the mail was sent successfully, false otherwise.
     */
    boolean send(final EmailAddress toAddress,
                 final EmailAddress fromAddress,
                 final String subject,
                 final String message);

}