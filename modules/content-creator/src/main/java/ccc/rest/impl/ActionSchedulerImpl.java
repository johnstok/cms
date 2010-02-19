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
package ccc.rest.impl;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ccc.rest.ActionScheduler;
import ccc.rest.SearchEngine;


/**
 * JAX-RS implementation of the {@link SearchEngine} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure/actions")
@Produces({"text/html", "application/json"})
public class ActionSchedulerImpl
    extends
        JaxrsCollection
    implements
        ActionScheduler {

    /** {@inheritDoc} */
    @Override
    public boolean isRunning() {
        return lookupActionScheduler().isRunning();
    }

    /** {@inheritDoc} */
    @Override
    public void start() {
        lookupActionScheduler().start();
    }

    /** {@inheritDoc} */
    @Override
    public void stop() {
        lookupActionScheduler().stop();
    }
}
