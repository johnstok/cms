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
package ccc.commons;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;


/**
 * Helper class used to describe the server environment.
 *
 * @author Civic Computing Ltd.
 */
public class Environment {
    private static final Logger LOG = Logger.getLogger(Environment.class);

    private static final String HOSTNAME = cacheHostname();

    /**
     * Query the hostname for this server.
     *
     * @return The server's hostname, as a string.
     */
    public static String getHostname() { return HOSTNAME; }


    private static String cacheHostname() {
        try {
            final InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostName();
        } catch (final UnknownHostException e) {
            LOG.warn("Failed to determine host address.", e);
            return "<unknown>";
        }
    }
}
