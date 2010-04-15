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
package ccc.plugins.security.jboss;

import org.jboss.web.tomcat.security.login.WebAuthentication;

import ccc.plugins.security.Sessions;


/**
 * JBoss implementation of the sessions API.
 *
 * @author Civic Computing Ltd.
 */
public class JbossSession
    implements
        Sessions {

    /** {@inheritDoc} */
    @Override
    public boolean login(final String username, final String password) {
        final WebAuthentication pwl = new WebAuthentication();
        return pwl.login(username, password);
    }
}
